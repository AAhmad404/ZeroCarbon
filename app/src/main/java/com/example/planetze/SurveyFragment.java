package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.Constants;
import utilities.UserData;

public class SurveyFragment extends Fragment {

    private FirebaseDatabase db;
    private String userId;
    int current_cat = 0;  //0-transportation,1-food, 2-housing,3-consumption
    int current_q = 0;  //index of current question
    double[] co2PerCategory = {0.0, 0.0, 0.0, 0.0};
    final String[] categories = Constants.categories1;
    //final int total_cats = categories.length;
    final String[][] questions = Constants.questions;
    final int num_qs = size(questions);
    final int num_transport_qs = Constants.transport_qs;
    final int num_food_qs = Constants.food_qs;
    final int num_housing_qs = Constants.housing_qs;
    final int num_consumption_qs = Constants.consumption_qs;
    final double[][][][][] housing_emissions = Constants.housing_emissions;
    final double[][] public_transport_emissions = Constants.public_trans_emissions;
    final double[][][] recycling_reduction = Constants.recycling_reduction;
    //elements of arrays below specify which answer option
    //(represented as int) is selected for a particular question
    int[] transport_ans = new int[num_transport_qs];  //7 qs in transportation category
    int[] food_ans = new int[num_food_qs];
    int[] housing_ans = new int[num_housing_qs];
    int[] consumption_ans = new int[num_consumption_qs];

    private TextView pleaseAnswer1;
    private TextView pleaseAnswer2;
    private Button nextBtn;
    private Button backBtn;
    private TextView category;
    private TextView question;
    private RadioGroup options;

    public SurveyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_init_survey, container, false);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userId = UserData.getUserID(getContext());

        initSurvey(view);
        return view;
    }


    /**
     * Updates survey UI to reflect current question being asked
     * @param options answer options component (RadioGroup)
     * @param question question text component (TextView)
     * @param category category text component (TextView)
     * @param q current question
     * @param c current category
     */
    private void updateSurvey(RadioGroup options, TextView question, TextView category,
                              int q, int c) {
        category.setText(categories[c]);
        question.setText(questions[q][0]);  //displays next q

        options.removeAllViews(); options.clearCheck();  //remove previous answer options
        for (int i = 1; i < questions[q].length; i++) { //loading answer options for the new q
            RadioButton btn = new RadioButton(getContext());
            btn.setId(i - 1);  //standard btn configurations
            btn.setText(questions[q][i]);
            btn.setTextColor(Color.BLACK);
            btn.setTextSize(16);
            options.addView(btn);  //adds btn to the RadioGroup (btn container)
        }
    }

    /**
     * Updates the global arrays containing user answers for each category
     * @param options ; button container for answer option buttons
     * @param cat ; the current category of questions being asked in the survey
     * @return true if answer selected, false if no answer selected
     */
    protected boolean saveAnswer(RadioGroup options, int cat, int q) {
        int btnId = options.getCheckedRadioButtonId();
        if (btnId == -1) return false;

        switch(cat) {
            case 0:
                saveDefaultCountry(btnId);
                break;
            case 1:
                transport_ans[q-2] = btnId;
                if (q == 2 && transport_ans[0] == 1) current_q += 2;  //skips follow-ups if user says no to car
                if (q == 5 && transport_ans[3] == 0) current_q += 1;  //same but for public transport
                break;
            case 2:
                //index subtractions rely on construction of questions array
                food_ans[q - num_transport_qs - 3] = btnId;
                if (q == 10 && food_ans[0] != 3) current_q += 4;  //skips follow-ups if user says no to meat
                break;
            case 3:
                housing_ans[q - num_transport_qs - num_food_qs - 4] = btnId;
                break;
            default:
                consumption_ans[q - num_transport_qs - num_food_qs - num_housing_qs - 5] = btnId;
                break;
        }
        return true;
    }

    /**
     * Computes emissions per category, given the category of questions just finished
     * @param cat the category of qs just finished
     */
    protected void computeCatEmissions(int cat) {
        switch(cat) {
            case 1:
                co2PerCategory[0] = transportEmissions();
                break;
            case 2:
                co2PerCategory[1] = foodEmissions();
                break;
            case 3:
                co2PerCategory[2] = housingEmissions();
                break;
            default:
                co2PerCategory[3] = consumptionEmissions();
                break;
        }
    }

    /**
     * Computes user's total annual carbon emissions (in kg) based on initialization survey answers
     * @return double representing total annual transport emissions (in kg)
     */
    protected double transportEmissions() {
        //firebase stuff used to store what car uses drives by default (needed for "Drive personal
        //vehicle" activity in EcoTracker)
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference(USER_DATA)
                .child(userId);
        Map<String, Object> c = new HashMap<>();

        double totalkg = 0.0;
        double r = 0.0;
        if (transport_ans[0] != 1) {  //true corresponds to user saying "yes" to "do u use car?"
            switch(transport_ans[1]) {  //which car they drive
                case 0: r = 0.24;
                    c.put("default_car", "gasoline");
                    break;  //gas emissions rate
                case 1: r = 0.27;
                    c.put("default_car", "diesel");
                    break;  //diesel, etc.
                case 2: r = 0.16;
                    c.put("default_car", "hybrid");
                    break;  //hybrid
                case 3: r = 0.05;
                    c.put("default_car", "electric");
                    break;  //electric
                default: r = 0.16;
                    c.put("default_car", "none");
                    break;  //"i don't know" answer (rate of emissions defaults to that of hybrid)
            }
            switch(transport_ans[2]) {  //how much they drive
                case 0: totalkg += r * 5000; break;  //constants are distances driven
                case 1: totalkg += r * 10000; break;
                case 2: totalkg += r * 15000; break;
                case 3: totalkg += r * 20000; break;
                case 4: totalkg += r * 25000; break;
                default: totalkg += r * 35000; break;
            }
        } else {c.put("default_car", "none");}
        userRef.updateChildren(c);  //adds default car component to user data for use in EcoTracker

        totalkg += public_transport_emissions[transport_ans[3]][transport_ans[4]];  //see Constants.java

        switch(transport_ans[5]) {  //short haul flight emissions
            case 0: break;
            case 1: totalkg += 225; break;
            case 2: totalkg += 600; break;
            case 3: totalkg += 1200; break;
            default: totalkg += 1800; break;
        }
        switch(transport_ans[6]) {  //long haul flight emissions
            case 0: break;
            case 1: totalkg += 825; break;
            case 2: totalkg += 2200; break;
            case 3: totalkg += 4400; break;
            default: totalkg += 6600; break;
        }

        return totalkg;
    }

    /**
     * Computes total annual user emissions from food consumption based on survey input
     * @return double representing total annual user emissions from food consumption
     */
    protected double foodEmissions() {
        double totalkg = 0.0;
        boolean meat = false;
        switch (food_ans[0]) {
            case 0: totalkg += 1000;break;
            case 1: totalkg += 500;break;
            case 2: totalkg += 1500;break;
            default: meat = true;break;
        }
        if (meat) {
            switch (food_ans[1]) {
                case 0: totalkg += 2500;break;
                case 1: totalkg += 1900;break;
                case 2: totalkg += 1300;break;
                default: break;
            }
            switch (food_ans[2]) {
                case 0: totalkg += 1450;break;
                case 1: totalkg += 860;break;
                case 2: totalkg += 450;break;
                default: break;
            }
            switch (food_ans[3]) {
                case 0: totalkg += 950;break;
                case 1: totalkg += 600;break;
                case 2: totalkg += 200;break;
                default: break;
            }
            switch (food_ans[4]) {
                case 0: totalkg += 800;break;
                case 1: totalkg += 500;break;
                case 2: totalkg += 150;break;
                default: break;
            }
        }
        switch (food_ans[5]) {
            case 0: break;
            case 1: totalkg += 23.4;break;
            case 2: totalkg += 70.2;break;
            default: totalkg += 140.4;break;
        }

        return totalkg;
    }

    /**
     * Computes total annual emissions from housing related energy use based on survey input
     * @return double representing total emissions (in kg) from housing
     */
    protected double housingEmissions() {
        double totalkg = 0.0;
        int[] i = housing_ans;
        if (i[3] != i[5]) totalkg += 233;

        if (i[0] == 4) i[0] = 2;  //sets "other" answer option to "townhouse" (as instructed in formula spreadsheet)
        if (i[3] == 5) i[3] = 1;  //sets "other" answer option to "electricity" (by assumption)
        if (i[5] == 5) i[5] = 1;  //^^ Piazza post SHOULD but has not yet clarified if this is what we are to do.
                                //profs have not yet specified what to do in this scenario so we will assume
                                //that it is reasonable to default "other" to electricity
        totalkg += housing_emissions[i[0]][i[1]][i[2]][i[4]][i[3]];  //home heating (i[3]; fourth question of category)
        totalkg += housing_emissions[i[0]][i[1]][i[2]][i[4]][i[5]];  //water heating (i[5]; sixth question of category)
        switch(i[6]) {  //7th question of housing category; re: renewable energy use
            case 0:
                totalkg -= 6000; break;  //primarily use renewable energy
            case 1:
                totalkg -= 4000; break;  //partially use " ... "
            default: break;  //no use of renewable energy
        }
        return totalkg;
    }

    /**
     * Computes total annual user carbon emissions related to consumption based on init survey results
     * @return double representing total annual user consumption-related emissions
     */
    protected double consumptionEmissions() {
        double totalkg = 0.0;
        int[] i = consumption_ans;  //for convenience
        switch(i[0]) {  //how often they buy clothes
            case 0: totalkg += 360; break;
            case 1: totalkg += 120; break;
            case 2: totalkg += 100; break;
            default: totalkg += 5; break;
        }
        switch(i[1]) {  //how often they recycle
            case 0: totalkg *= 0.5; break;
            case 1: totalkg *= 0.7; break;
            default: break;
        }
        switch(i[2]) {  //how many electronic devices they buy
            case 0: break;
            case 1: totalkg += 300; break;
            case 2: totalkg += 600; break;
            case 3: totalkg += 900; break;
            default: totalkg += 1200; break;
        }
        totalkg -= recycling_reduction[i[2]][i[0]][i[3]];  //how often they recycle (see Constants.java)

        return totalkg;
    }

    private double sum(double[] arr) {
        double s = 0.0;
        for (int i = 0; i < arr.length; i++) {
            s += arr[i];
        }
        return s;
    }

    private int size(String[][] arr) {
        int s = 0;
        String x = "";
        for (int i = 0; true; i++) {
            try {
                x = questions[i][0];
                s++;
            } catch (Exception e) {
                break;
            }
        }
        return s;
    }




    private void initSurvey(View view) {
        TextView getStartedPrompt = view.findViewById(R.id.getStartedPrompt);
        Button beginSurveyBtn = view.findViewById(R.id.beginSurveyBtn);
        TextView beingWithSurveyText = view.findViewById(R.id.beginWithSurveyText);

        nextBtn = view.findViewById(R.id.nextBtn);
        category = view.findViewById(R.id.category);

        beginSurveyBtn.setOnClickListener(v -> {
            getStartedPrompt.setVisibility(View.INVISIBLE);
            beginSurveyBtn.setVisibility(View.INVISIBLE);
            beingWithSurveyText.setVisibility(View.INVISIBLE);


            nextBtn.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
            beginFirstQuestion(view);
        });
    }

    private void beginFirstQuestion(View view) {

        pleaseAnswer1 = view.findViewById(R.id.pleaseAnswer1);
        pleaseAnswer2 = view.findViewById(R.id.pleaseAnswer2);
        nextBtn = view.findViewById(R.id.nextBtn);
        backBtn = view.findViewById(R.id.backBtn);
        category = view.findViewById(R.id.category);
        question = view.findViewById(R.id.question);
        options = view.findViewById(R.id.options);

        //nested code initializes survey at first question
        backBtn.setVisibility(View.INVISIBLE);
        category.setText(categories[current_cat]);
        question.setText(questions[current_q][0]);
        for (int i = 1; i < questions[current_q].length; i++) {
            RadioButton btn = new RadioButton(getContext());
            btn.setId(i - 1);
            btn.setText(questions[current_q][i]);
            btn.setTextColor(Color.BLACK);
            btn.setTextSize(16);
            options.addView(btn);
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Updates survey info (model of MVP) and UI (view) after each click of "next" by user
             * @param v The view that was clicked.
             */
            public void onClick(View v) {
                System.out.println(current_q);
                //if no answer selected, should not proceed. Should prompt user to answer
                if (!saveAnswer(options, current_cat, current_q)) {  //saves user's answer to prev question
                    pleaseAnswer1.setVisibility(View.VISIBLE); pleaseAnswer2.setVisibility(View.VISIBLE);
                    return;
                } else backBtn.setVisibility(View.VISIBLE);  // this is here to avoid back being visible on first question
                pleaseAnswer1.setVisibility(View.INVISIBLE); pleaseAnswer2.setVisibility(View.INVISIBLE);

                current_q++;  //iterates to next q
                if (current_q >= num_qs) {  //true if survey is finished
                    computeCatEmissions(current_cat);
                    List<Double> list = new ArrayList<>();
                    for (int i = 0; i < co2PerCategory.length; i++) {
                        list.add(co2PerCategory[i]);
                    }

                    DatabaseReference userRef = db.getReference(USER_DATA)
                            .child(userId);
                    //send survey results to firebase as Map<String, List<Double>>
                    Map<String, Object> c = new HashMap<>();
                    c.put("survey_results", list);
                    userRef.updateChildren(c);

                    userRef.child("is_new_user").setValue(false);

                    //"false" makes it so that pressing home button on survey results goes to app homepage
                    loadFragment(new SurveyResults(false));
                    return;
                }
                if (questions[current_q][0].equals("-")) {  //iter'n to next category if necessary
                    if (current_cat != 0)
                        computeCatEmissions(current_cat);  //computes emissions for the finished category (unless we just did first question)
                    current_q++; current_cat++;
                }
                updateSurvey(options, question, category, current_q, current_cat);  //updates UI
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            /**
             * Goes back to previous question when back button clicked
             * @param v The view that was clicked.
             */
            public void onClick(View v) {
                System.out.println(current_q);
                if (current_q == 5 && transport_ans[0] == 1) current_q -= 2;  //skips follow-ups if user says no to car
                if (current_q == 7 && transport_ans[3] == 0) current_q -= 1;  //same but for public transport
                if (current_q == 15 && food_ans[0] != 3) current_q -= 4;  //skips follow-ups if user says no to meat

                current_q--;
                if (questions[current_q][0].equals("-")) {
                    current_q--;
                    current_cat--;
                }
                if (current_q == 0) backBtn.setVisibility(View.INVISIBLE);
                updateSurvey(options, question, category, current_q, current_cat);
            }
        });

    }


    /**
     * saves the country chosen in "area of residence" as default country of the user.
     * @param btnId
     */
    private void saveDefaultCountry(int btnId) {
        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        DatabaseReference userRef = db.getReference(USER_DATA)
                .child(userId);
        Map<String, Object> c = new HashMap<>();
        c.put("default_country", questions[0][btnId + 1]);
        userRef.updateChildren(c);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}