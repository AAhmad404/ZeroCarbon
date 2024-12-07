package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utilities.Constants;
import utilities.UserData;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddActivity extends Fragment {
    private final String msg1 = "Select a Category";
    private final String msg2 = "Select an Activity";
    private final String catactPrompt = "Please select a category and activity";
    private final String inputPrompt = "Please select an option for input";
    public final String[] activityCats = Constants.activityCats;
    public final String[][] activities = Constants.activities;
    public String date = "date";  //used to store the selected date
    public int edit = 0;  //for edit mode
    public List<String> activityToEdit = new ArrayList<>();  //for edit mode
    private boolean spinnerListeners = false;
    private int id = 0;
    private String default_car = "none";
    static FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
    static String userId;  //this should be changed to the particular logged in user once everything works
    int recursionLimiter = 0;

    public AddActivity() {
        // Required empty public constructor
    }

    /**
     * this constructor used for adding activities
     * @param d
     */
    public AddActivity(String d) {
        date = d;  //d passed as an argument from eco tracker (current calendar date)
    }

    /**
     * this constructor used for editing activities. Notice that the activity that was selected for
     * editing is passed in the parameters so that it can be edited.
     * @param d
     * @param activity
     */
    public AddActivity(String d, List<String> activity, int i) {
        date = d;
        edit = 1;  //used to determine if we are in edit mode or add mode (1 -> edit mode)
        activityToEdit = activity;
        id = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_activity, container, false);

        userId = UserData.getUserID(getContext());

        //occurs if we open AddActivity, then navigate elsewhere in the app,
        //then navigate back to ecotracker. AddActivity will auto-close then
        if (date.equals("date")) getParentFragmentManager().popBackStack();

        final Spinner catSpinner = view.findViewById(R.id.catSpinner);
        final Spinner actSpinner = view.findViewById(R.id.actSpinner);
        final Button backBtn = view.findViewById(R.id.backBtn);
        final Button saveBtn = view.findViewById(R.id.saveBtn);
        final RadioGroup box1 = view.findViewById(R.id.inputBox1);
        final RadioGroup box2 = view.findViewById(R.id.inputBox2);
        final TextView txt1 = view.findViewById(R.id.input1Text);
        final TextView txt2 = view.findViewById(R.id.input2Text);
        final TextView issuePrompt1 = view.findViewById(R.id.issuePrompt1);
        final TextView issuePrompt2 = view.findViewById(R.id.issuePrompt2);

        //"back" & "save" button implementations
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EcoTrackerFragment.fetchSnapshot();
                getParentFragmentManager().popBackStack();
                //requireActivity().getOnBackPressedDispatcher().onBackPressed();

                /*Bundle bundle = new Bundle();
                bundle.putString("date", date);
                NavController navController = NavHostFragment.findNavController(requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragment));
                navController.popBackStack(R.id.EcoTrackerFragment, false);*/
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = (String) catSpinner.getSelectedItem();
                String act = (String) actSpinner.getSelectedItem();
                int input1 = box1.getCheckedRadioButtonId();
                int input2 = box2.getCheckedRadioButtonId();
                if (cat.equals(msg1) || act.equals(msg2)) {
                    issuePrompt2.setText(catactPrompt);
                    issuePrompt1.setVisibility(View.VISIBLE);
                    issuePrompt2.setVisibility(View.VISIBLE);
                    return;
                } else if (input1 == -1 || (input2 == -1 && box2.getVisibility() == View.VISIBLE)) {
                    issuePrompt2.setText(inputPrompt);
                    issuePrompt1.setVisibility(View.VISIBLE);
                    issuePrompt2.setVisibility(View.VISIBLE);
                    return;
                }

                //compute values and save in a list
                List<String> activity = saveActivity(cat, act, input1, input2);
                if (edit == 1) {
                    updateFirebase(date, activity, id);  //update firebase (for edit mode)
                    Toast.makeText(getContext(), "Edit Saved", Toast.LENGTH_SHORT).show();
                } else {
                    writeToFirebase(date, activity, userId);  //write list to firebase
                    Toast.makeText(getContext(), "Activity Added", Toast.LENGTH_SHORT).show();
                }
                EcoTrackerFragment.fetchSnapshot();  //update ecotracker info
                getParentFragmentManager().popBackStack();
            }
        });

        spinnerListeners = false;  //technical stuff related to edit mode. Don't read unless debugging.
        // Summary:
        //category is set first, then activity (both manually in initEditingProcess). Thus,
        //category listener will be invoked first. This variable being false prevents the listener
        //from doing anything (since, normally, category listener overrides activity listener data).
        //Then, only after the first category setting is done and everything is set up, we give
        //permission for category listener to run for future category changes
        initSpinners(catSpinner, actSpinner, activityCats);  //inits spinners to default
        if (edit == 1) initEditingProcess(activityToEdit, catSpinner, actSpinner,
                box1, box2, txt1, txt2);  //if in editing mode, also init data of activity to edit

        //listener for category selection (sets activity spinner accordingly)
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (spinnerListeners) {
                    String selectedCat = (String) parent.getItemAtPosition(position);
                    txt1.setVisibility(View.INVISIBLE); txt2.setVisibility(View.INVISIBLE);
                    setActivitySpinner(selectedCat, actSpinner);

                    box1.clearCheck(); box2.clearCheck();
                    box1.removeAllViews(); box2.removeAllViews();
                } spinnerListeners = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //listener for activity selection (sets input boxes accordingly)
        actSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedActivity = (String) parent.getItemAtPosition(position);
                DatabaseReference carRef = db.getReference().child(USER_DATA)
                        .child(userId).child("default_car");
                carRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        default_car = (String) snapshot.getValue();
                        displayInputs(box1, box2, txt1, txt2, selectedActivity);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Sets activity spinner based on selected category (i.e. transport, food, etc.)
     * @param selectedCat
     * @param actSpinner
     */
    public void setActivitySpinner(String selectedCat, Spinner actSpinner) {
        if (selectedCat.equals("Select a Category")) {
            String[] arr = {msg2};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, arr);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            actSpinner.setAdapter(adapter);
            int x = adapter.getPosition(msg2);  //inits spinner to  "Select an Activity"
            actSpinner.setSelection(x);
            return;
        }

        for (int i = 1; i < activityCats.length; i++) {
            if (selectedCat.equals(activityCats[i])) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, activities[i-1]);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                actSpinner.setAdapter(adapter);
                int x = adapter.getPosition(msg2);  //inits spinner to  "Select an Activity"
                actSpinner.setSelection(x);
            }
        }
    }


    /**
     * default; for adding activities
     */
    public void initSpinners(Spinner catSpinner, Spinner actSpinner, String[] activityCats) {
        //spinner initializations
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, activityCats);  //used to load spinner
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter1);  //loads spinner
        int init = adapter1.getPosition(msg1);  //initializes spinner to default "Select a Category"
        catSpinner.setSelection(init);

        //initialize second spinner based on first spinner selection
        String[] x = {msg2};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, x);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actSpinner.setAdapter(adapter2);
        init = adapter1.getPosition(msg2);  //initializes spinner to default "Select an Activity"
        actSpinner.setSelection(init);
    }

    /**
     * for editing. Makes it so that, upon clicking "edit" button from ecotracker,
     * AddActivity initializes with the selected activity preset
     * @param activityToEdit
     * @param catSpinner category spinner
     * @param actSpinner activity spinner
     * @param box1 radiogroup hosting the buttons of first input question
     * @param box2 radiogroup hosting the buttons of second input question (if necessary)
     * @param txt1 textbox describing first input question
     * @param txt2 "" for second question
     */
    public void initEditingProcess(List<String> activityToEdit, Spinner catSpinner,
                                   Spinner actSpinner, RadioGroup box1, RadioGroup box2,
                                   TextView txt1, TextView txt2) {

        //sets category spinner
        ArrayAdapter<String> i = (ArrayAdapter<String>) catSpinner.getAdapter();
        int j = i.getPosition(activityToEdit.get(0));
        catSpinner.setSelection(j);

        //sets activity spinner
        setActivitySpinner(activityToEdit.get(0), actSpinner);  //sets spinner to "Select an activity" by default
        i = (ArrayAdapter<String>) actSpinner.getAdapter();
        j = i.getPosition(activityToEdit.get(1));

        actSpinner.setSelection(j, false);

        DatabaseReference carRef = db.getReference().child(USER_DATA)
                .child(userId).child("default_car");
        carRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //this is what we need the listener for: to retrieve default_car value
                default_car = (String) snapshot.getValue();
                //sets input boxes
                //activityToEdit.get(1) corresponds to activity string of the activity
                displayInputs(box1, box2, txt1, txt2, activityToEdit.get(1));

                //sets checked button of input boxes
                //note that index 2 of activityToEdit is the total emissions of the activity
                //Indices 3 and 4 are what contain the input selections, which is what we set below
                box1.check(Integer.parseInt(activityToEdit.get(3)));
                if (box2.getChildCount() != 0)  //only init second input box if the question demands input
                    box2.check(Integer.parseInt(activityToEdit.get(4)));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

       }

    /**
     * Displays input options for user once activity has been selected
     * @param box1 the radiogroup for first input
     * @param box2 the radiogroup for second input (if necessary)
     * @param act the selected activity
     */
    public void displayInputs(RadioGroup box1, RadioGroup box2,
                              TextView txt1, TextView txt2, String act) {
        box1.clearCheck(); box2.clearCheck();
        box1.removeAllViews(); box2.removeAllViews();
        box1.setVisibility(View.INVISIBLE); box2.setVisibility(View.INVISIBLE);
        txt1.setVisibility(View.INVISIBLE); txt2.setVisibility(View.INVISIBLE);
        if (act.equals(msg2)) return;  //if user selected the default "Select an Activity"

        String[] inpt1 = {}; String[] inpt2 = {};
        String text1 = ""; String text2 = "";
        if (act.equals("Drive personal vehicle")) {
            inpt1 = new String[]{"< 15km", "15-40km", "40-80km", "80-200km", "> 200km"};  //distance driven
            inpt2 = new String[]{"Gasoline", "Diesel", "Hybrid", "Electric"};  //optionally: change vehicle type
            text1 = "Distance driven"; text2 = "Vehicle type (default: "+ default_car + ")";
        } else if (act.equals("Take public transportation")) {
            inpt1 = new String[]{"Bus", "Train", "Subway"};  //type of public transport
            inpt2 = new String[]{"< 0.5 hours", "0.5-1 hours", "1-2 hours", "> 2 hours"};  //time spent on public transport
            text1 = "Type of public transport"; text2 = "Time spent on public transport";
        } else if (act.equals("Cycling/Walking")) {
            inpt1 = new String[]{"< 2km", "2-4km", "4-8km", "> 8km"};  //distance cycled/walked
            text1 = "Distance cycled/walked";
        } else if (act.equals("Flight (< 1,500km)") || act.equals("Flight (> 1,500km)")) {
            inpt1 = new String[]{"1", "2", "3", "> 3"};  //number of flights taken
            text1 = "Number of flights taken";
        }else if (act.equals("Meal")) {
            inpt1 = new String[]{"Beef", "Pork", "Chicken", "Fish", "Plant-based"};  //type of meal (beef pork chicken fish plant based)
            inpt2 = new String[]{"1", "2", "3-4", "> 4"};  //number of servings
            text1 = "Type of meal"; text2 = "Number of servings";
        }else if (act.equals("Buy new clothes")) {
            inpt1 = new String[]{"1", "2", "3-4", "> 4"};  //number of clothing items purchased
            text1 = "Number of clothing items purchased";
        }else if (act.equals("Buy electronics")) {
            inpt1 = new String[]{"Smartphone", "Laptop/Computer", "T/V"};  //type of electronic device
            inpt2 = new String[]{"1", "2", "3-4", "> 4"};  //number of devices purchased
            text1 = "Type of electronic device"; text2 = "Number of devices purchased";
        }else if (act.equals("Other purchases")) {
            inpt1 = new String[]{"Furniture", "Appliances"};  //type of purchase
            inpt2 = new String[]{"1", "2", "3-4", "> 4"};  //# of purchases
            text1 = "Type of purchase"; text2 = "Number of purchases";
        }else if (act.equals("Electricity") || act.equals("Gas") || act.equals("Water")) {
            inpt1 = new String[]{"Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"};  //bill amount $
            text1 = "Bill amount";
        }

        box1.setVisibility(View.VISIBLE);  //set input option boxes visible
        txt1.setVisibility(View.VISIBLE);
        txt1.setText(text1);
        if (inpt2.length != 0) {
            box2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            txt2.setText(text2);
        }

        //load input option boxes with appropriate answer options
        RadioButton btn;
        for (int i = 0; i < inpt1.length; i++) {
            btn = new RadioButton(getContext());
            btn.setId(i);
            btn.setText(inpt1[i]);
            box1.addView(btn);
        }
        for (int i = 0 ; i < inpt2.length; i++) {
            btn = new RadioButton(getContext());
            btn.setId(i + 100);  //to avoid same id as btns of first radiogroup (which would cause
                                //issues in the xml file for this fragment)
            btn.setText(inpt2[i]);
            box2.addView(btn);
        }
        if (act.equals("Drive personal vehicle") && !default_car.equals("none")) {
            int id = 0;
            switch(default_car) {
                case "gasoline": id = 0; break;
                case "diesel": id = 1; break;
                case "hybrid": id = 2; break;
                default: id = 3; break;
            }
            box2.check(id + 100);
        }
    }


    public List<String> saveActivity(String cat, String act, int input1, int input2) {
        List<String> activity = new ArrayList<>();
        activity.add(cat); activity.add(act);  //add category and activity names
        double activityEmissions = computeEmissions(cat, act, input1, input2);
        activity.add(String.valueOf(activityEmissions));  //add total emissions for the activity
        activity.add(String.valueOf(input1));  //input1 and input2 saved for edit feature implementation
        if (input1 != -1)  //input2 only saved if the activity actually had two required answer inputs
            activity.add(String.valueOf(input2));
        return activity;
    }


    public void updateFirebase(String date, List<String> activity, int id) {
        DatabaseReference dateRef = db.getReference(USER_DATA)
                .child(userId).child("calendar").child(date);
        dateRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Object rawData = currentData.getValue();
                if (rawData == null && recursionLimiter < 100) {
                    recursionLimiter++;  //prevents infinite recursion
                    updateFirebase(date, activity, id);  //retries recursively
                    return Transaction.abort();
                }

                //find date in the firebase
                List<Object> a = currentData.getValue(new GenericTypeIndicator<List<Object>>() {});
                a.set(id, activity);
                currentData.setValue(a);  //writes to firebase
                return Transaction.success(currentData);  //required for Transactions
            }

            @Override  //gives logcat message for success/failure
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot dataSnapshot) {}
        });

    }


    public static void writeToFirebase(String date, List<String> activity, String userId) {
        DatabaseReference calendarRef = db.getReference(USER_DATA)
                .child(userId).child("calendar");

        calendarRef.child(date).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                //find date in the firebase
                List<Object> a = currentData.getValue(new GenericTypeIndicator<List<Object>>() {});
                if (a == null)  //true if the current date does not already exist in the database
                    a = new ArrayList<>();  //creates new arraylist for the date
                //otherwise, a is nonnull (date exists), so we just add the new activity
                a.add(activity);
                currentData.setValue(a);  //writes to firebase
                return Transaction.success(currentData);  //required for Transactions
            }

            @Override  //gives logcat message for success/failure
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot dataSnapshot) {}
        });

    }


    public double computeEmissions(String cat, String act, int input1, int input2) {
        double e = 0.0;
        switch(act) {
            case "Drive personal vehicle":
                double x = 0.0, y = 0.0;
                switch (input1) {
                    case 0: x = 15; break;
                    case 1: x = 40; break;
                    case 2: x = 80; break;
                    case 3: x = 200; break;
                    default: x = 500; break;
                }
                switch (input2) {  //values taken from "formulas" document
                    case 100: y = 0.24; break;
                    case 101: y = 0.27; break;
                    case 102: y = 0.16; break;
                    default: y = 0.05; break;
                }
                return x * y;  //km driven * kg of CO2 per km
            case "Take public transportation":
                x = 0.0; y = 0.0;
                //values below hardcoded based on assumptions.
                //They can be stored in some sort of data file if necessary later. Not a priority atm
                switch (input1) {
                    case 0: x = 0.75; break;
                    case 1: x = 0.5; break;
                    default: x = 0.5; break;
                }
                switch (input2) {
                    case 100: y = 0.5; break;
                    case 101: y = 1; break;
                    case 102: y = 2; break;
                    default: y = 3; break;
                }
                return x * y;
            case "Cycling/Walking":
                return 0;  //no emissions are produced from cycling/walking
            case "Flight (< 1,500km)":
                x = 225;  //kg of CO2
                switch (input2) {
                    case 100: y = 1; break;  //num of flights
                    case 101: y = 2; break;
                    case 102: y = 3; break;
                    default: y = 4; break;
                }
                return x * y;
            case "Flight (> 1,500km)":
                x = 550;  //kg of CO2
                switch (input2) {
                    case 100: y = 1; break;  //num of flights
                    case 101: y = 2; break;
                    case 102: y = 3; break;
                    default: y = 4; break;
                }
                return x * y;
            case "Meal":
                switch (input1) {
                    //these values calculated from formulas file:
                    // ~(#kg if you consume the meal daily) / 350 days per year
                    //i.e. for beef; daily consumption gives 2500kgco2/year. 2500/350 ~= 7
                    case 0: x = 7; break;  //beef
                    case 1: x = 4; break;  //pork
                    case 2: x = 2.8; break;  //chicken
                    case 3: x = 2.3; break;  //fish
                    default: x = 2; break;  //plant-based
                }
                switch (input2) {
                    case 100: y = 1; break;  //num of servings
                    case 101: y = 2; break;
                    case 102: y = 4; break;
                    default: y = 5; break;
                }
                return x * y;
            case "Buy new clothes":
                x = 6;  //kg per clothing item; computed using formulas file:
                //monthly buyers of clothes get 360kgco2/year
                //sps. 5 clothing items per month @6kgco2 per clothing item. This
                //gets the given 360kgco2/year number. So we use 6kgco2 per clothing item
                switch (input1) {
                    case 0: y = 1; break;  //num of clothing items purchased
                    case 1: y = 2; break;
                    case 2: y = 4; break;
                    default: y = 6; break;
                }
                return x * y;
            case "Buy electronics":
                switch (input1) {
                    //values taken from formulas file:
                    //300kg for one electronic device, we set this as the default
                    //for smartphone. Then safe to assume computer and TV should be higher emissions
                    //since they take more resources to make
                    case 0: x = 300; break;  //smartphone
                    case 1: x = 600; break;  //computer
                    default: x = 900; break;  //tv
                }
                switch (input2) {
                    case 100: y = 1; break;  //num of devices purchased
                    case 101: y = 2; break;
                    case 102: y = 4; break;
                    default: y = 5; break;
                }
                return x * y;
            case "Other purchases":
                switch (input1) {
                    case 0: x = 100; break;  //furniture
                    default: x = 900; break;  //appliances; set to same emissions (in kg) as buying a TV
                }
                switch (input2) {
                    case 100: y = 1; break;  //num of items purchased
                    case 101: y = 2; break;
                    case 102: y = 4; break;
                    default: y = 5; break;
                }
                return x * y;

            //values for electricity & gas are from formulas file:
            //they are (emissions for 2 occupants, detached house under 1000sqft) / ~350 days in a year
            //these are to be added to the past 30 days, starting at the day of the added activity
            //since they are monthly bills (i.e. these emissions occur each day for a month, resulting
            //in the given bill of x dollars)
            //water values are the mean of the corresponding electricity & gas values
            case "Electricity":
                switch (input1) {
                    case 0: e = 0.7; break;
                    case 1: e = 1.4; break;
                    case 2: e = 4.1; break;
                    case 3: e = 5.4; break;
                    default: e = 7.1; break;
                } logPast29Days(cat, act, e, input1, input2);
                return e;
            case "Gas":
                switch (input1) {
                    case 0: e = 7.4; break;
                    case 1: e = 7.5; break;
                    case 2: e = 8.0; break;
                    case 3: e = 8.5; break;
                    default: e = 8.9; break;
                } logPast29Days(cat, act, e, input1, input2);
                return e;
            default:  //case "Water"
                switch (input1) {
                    case 0: e = 4.0; break;
                    case 1: e = 4.5; break;
                    case 2: e = 6.1; break;
                    case 3: e = 7.0; break;
                    default: e = 8.0; break;
                } logPast29Days(cat, act, e, input1, input2);
                return e;
        }
    }


    /**
     * When a monthly bill is added as an activity, this logs the bill across the past month
     * as daily energy use activities with some constant daily rate [e] of kgco2
     * @param cat
     * @param act
     * @param e
     * @param input1
     * @param input2
     */
    public void logPast29Days(String cat, String act, double e, int input1, int input2) {
        List<String> past29Days = getPast29Days(date);
        List<String> activity = new ArrayList();
        activity.add(cat);activity.add(act);
        activity.add(String.valueOf(e));
        activity.add(String.valueOf(input1)); activity.add(String.valueOf(input2));

        for (int i = 0; i < past29Days.size(); i++) {
            writeToFirebase(past29Days.get(i), activity, userId);
        }
    }


    /**
     * Gets the past 29 days of the calendar.'
     * Need this to be static for use in AddHabit
     * @return
     */
    public static List<String> getPast29Days(String date) {
        List<String> past29Days = new ArrayList<>();

        //convert date string to year, month, day integers
        String[] ymdStrings = date.split("-");
        int[] ymdInts = new int[3];
        for (int i = 0; i < ymdStrings.length; i++)
            ymdInts[i] = Integer.parseInt(ymdStrings[i]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(ymdInts[0],
                ymdInts[1] - 1,  //note here that months in Calendar class go from 0-11,
                                        //but we have them set from 1-12. So subtract 1 from ours
                        ymdInts[2]);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //iterates back one day since saveActivity
        //already adds the activity for the current day

        //adds past 29 days
        for (int i = 0; i < 29; i++) {
            String d = calendar.get(Calendar.YEAR) +"-"+(calendar.get(Calendar.MONTH)+1)+"-"
                    +calendar.get(Calendar.DAY_OF_MONTH);
            past29Days.add(d);
            calendar.add(Calendar.DAY_OF_MONTH, -1);  //iterates back one day
        }

        return past29Days;
    }




}//end of class