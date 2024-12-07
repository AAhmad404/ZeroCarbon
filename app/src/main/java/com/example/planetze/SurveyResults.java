package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import utilities.Constants;
import utilities.UserData;


public class SurveyResults extends Fragment {

    private FirebaseDatabase db;
    private String userId;

    final String[] country = Constants.country;
    final double[] countryEmissions = Constants.country_emissions;
    //this is set to Canada just in case Firebase messes up. Really, it is fetched from Firebase.
    String defaultCountry = "Canada";
    double userE = 0.0;  //total user emissions in tons
    double globalTarget = 2.0;  //global target emissions (tons per year for 1 person)

    List<Double> results = new ArrayList<>();
    private boolean returnToEcoTracker = false;

    public SurveyResults(boolean b) {
        returnToEcoTracker = b;
    }
    public SurveyResults(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_results, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //for if we arrive from ecoTracker
        if (returnToEcoTracker) initHomeBtn(view, 1);
        else initHomeBtn(view, 0);

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userId = UserData.getUserID(getContext());
        DatabaseReference userArrayRef = db.getReference(USER_DATA)
                .child(userId).child("survey_results");
        userArrayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Object> a = (List<Object>) dataSnapshot.getValue();
                    for (int i = 0; i < a.size(); i++) {
                        results.add(Double.valueOf(String.valueOf(a.get(i))));
                    }  //conversion necessary because Firebase stores doubles weirld
                    // (sometimes integers, sometimes Longs, etc.)
                    results = kgToTons(results);
                    userE = sum(results);  //saves user result immediately
                    setGlobalTargetComparison(view);
                }

                //initializes some basics
                final TextView your_emissions = view.findViewById(R.id.your_emissions);
                String sum = String.valueOf(round(userE));
                your_emissions.setText(sum);
                final TextView total_bar = view.findViewById(R.id.total_bar);
                String msg = "You emitted " + sum + " tons of CO₂ annually.";
                total_bar.setText(msg);
                setCategoryGraph(view, results);  //sets category graph

                initComparisonGraph(view);  //initializes comparison graph
                setUserDataComparisonGraph(view, userE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        //spinner change listener
        Spinner s = view.findViewById(R.id.spinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                setComparisonGraph(view, selectedItem);

                setCountryStats(view, position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setComparisonGraph(view, defaultCountry);
            }
        });

        return view;
    }


    /**
     * Sets the first graph depicting breakdown of user emissions by category
     * "public void setCategoryGraph(List<Double> results) {" was the previous method declaration,
     * may be useful for future.
     */
    public void setCategoryGraph(View view, List<Double> results) {
        TextView[] bars = {
                view.findViewById(R.id.transport),
                view.findViewById(R.id.food),
                view.findViewById(R.id.housing),
                view.findViewById(R.id.consumption)};
        TextView[] extra = {
                view.findViewById(R.id.t_negligible),
                view.findViewById(R.id.f_negligible),
                view.findViewById(R.id.h_negligible),
                view.findViewById(R.id.c_negligible)};
        double max_result = max(results);

        //preprocessing
        //230 is based on container width/bar width calculations in XML
        //230 = 358 (container width) - 104 (x value of start of bar) - 24 (extra spacing)
        // Converts container width to pixels
        int container_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                230, getResources().getDisplayMetrics());
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        // Convert dp to pixels. 35dp chosen based on UI
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                25, display_metrics);

        //implementing bar length (updating ui)
        for (int i = 0; i < bars.length; i++) {
            //computes pixel width for current bar
            int p = Math.max(min_p, (int) (container_width * (results.get(i) / max_result)));
            if (p == min_p) extra[i].setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutParams = bars[i].getLayoutParams();
            layoutParams.width = p;  //sets width of bar
            bars[i].setLayoutParams(layoutParams);
            bars[i].requestLayout();  //updates UI

            String txt = String.valueOf(round(results.get(i)));
            bars[i].setText(txt);
        }
    }


    /**
     * Initializes second graph (comparison of user emissions with a selected country)
     * to default country and loads spinner with list of countries
     */
    private void initComparisonGraph(View view) {
        DatabaseReference countryRef = db.getReference().child(USER_DATA)
                .child(userId).child("default_country");

        countryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                defaultCountry = (String) snapshot.getValue();

                //initialize spinner to country c
                Spinner s = view.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, country);  //used to load spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);  //loads spinner
                int init = adapter.getPosition(defaultCountry);  //initializes spinner to default country
                s.setSelection(init);  //^^

                setComparisonGraph(view, defaultCountry);  //set UI
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    /**
     * Updates the comparison graph based on current spinner setting
     * @param c the country selected by spinner for which we display average emissions
     */
    private void setComparisonGraph(View view, String c) {
        int i = indexOf(country, c);
        double countryE = countryEmissions[indexOf(country, c)];

        assert view.findViewById(R.id.user_bar) != null;

        //computes desired bar height of compared country
        double height_user_in_p = view.findViewById(R.id.user_bar).getHeight();
        // Convert dp to pixels. 13dp and 125dp chosen based on UI
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        int min_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                13, display_metrics);
        int max_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                125, display_metrics);
        //next line is main formula. Math.abs() used in the case that user emissions are < 0
        int height_in_p =
                (int) Math.min(max_p, Math.max(min_p, Math.abs((countryE/userE) * height_user_in_p)));

        //sets bar height of compared country
        TextView bar = view.findViewById(R.id.country_bar);
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = height_in_p;
        bar.setLayoutParams(params);
        //bar.requestLayout();  //possibly necessary. For now no issues without this

        //sets text of bar height textbox
        TextView bar_height = view.findViewById(R.id.country_emissions);
        String x = String.valueOf(round(countryE)) + "   ";
        bar_height.setText(x);
    }


    /**
     * Sets the bar representing user emissions in the comparison graph.
     * Reason we need this is to adjust user emissions bar to be very low if needed.
     * Otherwise default setting of user bar is about half the size of the container
     * @param e user emissions
     */
    private void setUserDataComparisonGraph(View view, double e) {
        TextView user_bar = view.findViewById(R.id.user_bar);
        ViewGroup.LayoutParams params = user_bar.getLayoutParams();
        DisplayMetrics display_metrics = getResources().getDisplayMetrics();
        int height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                65, display_metrics);  //default user bar height
        if (e < 0) {
            height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    13, display_metrics);
        } else if (e < 1) {
            height_in_p = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    16, display_metrics);
        }
        params.height = height_in_p;
        user_bar.setLayoutParams(params);
    }


    public void initHomeBtn(View view, int i) {
        Button homeBtn = view.findViewById(R.id.homeBtn);
        Button retakeSurveyBtn = view.findViewById(R.id.retakeSurveyBtn);

        if (i == 0) {
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), HomeActivity.class);

                    // Prevent the user from being able to navigate back the login page using the return action.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        } else {
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getParentFragmentManager().popBackStack();
                }
            });
        }

        retakeSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadFragment(new SurveyFragment());

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("retakeSurvey", true);
                startActivity(intent);
            }
        });
    }


    /**
     * shows what percent the user's emissions are of the national average for the selected country
     * @param view
     * @param index the index of the selected country in the country array
     */
    private void setCountryStats(View view, int index) {
        TextView countryStats = view.findViewById(R.id.countryStat);
        String countryToCompare = country[index];
        double emissionsToCompare = countryEmissions[index];
        double percentDiff = Math.round((userE / emissionsToCompare) * 100.0);

        String s;
        if (percentDiff == 100)
            s = "Your emissions are <font color='#009999'>equal</font> to those of the national average for " + countryToCompare;
        else if (percentDiff < 100)
            s = "Your emissions are " + (100 - percentDiff)
                    + "% <font color='#03AC13'>lower</font> than that of the national average for " + countryToCompare;
        else s = "Your emissions are " + (percentDiff - 100)
                    + "% <font color='#FF0000'>higher</font> than that of the national average for " + countryToCompare;
        countryStats.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY));
    }


    private void setGlobalTargetComparison(View view) {
        TextView globalTargetStats = view.findViewById(R.id.globalTargetStat);
        double percentDiff = Math.round((userE / globalTarget) * 100.0);

        String s;
        if (percentDiff == 100)
            s = "Your emissions are <font color='#009999'>equal</font> to the global emissions target (in tons of CO2 per year, per person)";
        else if (percentDiff < 100)
            s = "Your emissions are " + (100 - percentDiff)
                    + "% <font color='#03AC13'>lower</font> than that of the global emissions target (in tons of CO2 per year, per person)";
        else s = "Your emissions are " + (percentDiff - 100)
                    + "% <font color='#FF0000'>higher</font> than that of the global emissions target (in tons of CO2 per year, per person)";
        globalTargetStats.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY));
    }


    /**
     * Converts array of carbon emissions in kg to tons
     * @param list array of carbon emissions in kg
     * @return array of carbon emissions in tons
     */
    private List<Double> kgToTons(List<Double> list) {
        list.replaceAll(value -> value / 1000.0);
        return list;
    }

    private double sum(List<Double> list) {
        double total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i);
        }
        return total;
    }

    private double max(List<Double> list) {
        double m = 0.0;
        for (int i = 0; i < list.size(); i++) {
            if (m < list.get(i)) m = list.get(i);
        }
        return m;
    }

    private double round(double x) {
        x = x * 10;
        int y = (int) x;
        return y / 10.0;
    }

    /**
     * Self-explanatory. Java has no built-in array.indexOf function,
     * need one to convert spinner selection to country emissions
     * @param arr
     * @param item
     * @return
     */
    private int indexOf(String[] arr, String item) {
        int x = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(item)) {
                x = i; break;
            }
        }
        return x;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction()
                .replace(R.id.main, fragment);
        transaction.commit();
    }
}