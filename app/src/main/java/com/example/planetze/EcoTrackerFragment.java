package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import utilities.Constants;
import utilities.UserData;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EcoTrackerFragment extends Fragment {

    static View globalView;
    final static String[] months = Constants.months;
    private FirebaseDatabase db;
    public static String userId;
    private static DatabaseReference calendarRef;  //this is static so that we can call fetchSnapshot()
            //from addActivity fragment when returning to ecotrackerfragment in order to update
            //ecotracker activity info upon return
    private static DatabaseReference habitsRef;  //^^same with this
    private static ValueEventListener activitiesListener;  //^^same with this
    private static ValueEventListener habitsListener;
    private static View.OnClickListener addListener;
    private static View.OnClickListener editListener;

    HashMap<String, Object> days = new HashMap<>();  //used to store the days of a user calendar
    List<List<String>> acts = new ArrayList<>();  //used to store the activities of a day
    List<List<String>> currentHabits = new ArrayList<>();
    List<String> activityToEdit = new ArrayList<>();  //used to store the activity selected for editing
    public static String date = "";
    int presetCalendar = 0;
    boolean habitsToggled = false;

    public EcoTrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        final Button activitiesBtn = globalView.findViewById(R.id.activitiesBtn);
        activitiesBtn.performClick();  //by default, loads activities & not habits
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey("date")) {
            presetCalendar = 1;
            date = args.getString("date");
            if (args.containsKey("habitsToggled"))
                habitsToggled = args.getBoolean("habitsToggled");
        } else {presetCalendar = 0;}

        AddHabit.getEmissionsSnapshot(userId);
    }

    /**
     * MAIN INITIALIZATION CODE HERE
     *
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AddHabit.getEmissionsSnapshot(userId);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eco_tracker, container, false);
        globalView = view;

        userId = UserData.getUserID(getContext());

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        calendarRef = db.getReference(USER_DATA)
                .child(userId).child("calendar");
        habitsRef = db.getReference().child(USER_DATA)
                        .child(userId).child("current_habits");

        final Button calendarToggle = view.findViewById(R.id.calendarToggle);  //button to toggle calendar
        final TextView dateText = view.findViewById(R.id.dateText);
        final TextView yearText = view.findViewById(R.id.yearText);
        final TextView dailyTotal = view.findViewById(R.id.dailyTotal);

        final TextView noActivities = view.findViewById(R.id.emptyMsg);
        final RadioGroup activities = view.findViewById(R.id.activitiesGroup);
        final Button addBtn = view.findViewById(R.id.addBtn);
        final Button editBtn = view.findViewById(R.id.editBtn);
        final Button delBtn = view.findViewById(R.id.delBtn);
        final TextView issuePrompt1 = view.findViewById(R.id.issuePrompt1);
        final TextView issuePrompt2 = view.findViewById(R.id.issuePrompt2);
        final Button activitiesBtn = view.findViewById(R.id.activitiesBtn);
        final Button habitsBtn = view.findViewById(R.id.habitsBtn);
        activitiesBtn.performClick();  //by default, loads activities & not habits
        setAnnualEmissionsBtn(view);

        String prompt;
        if (habitsToggled && activities.getChildCount() != 0) {
            prompt = "Log a habit to reduce your carbon emissions!";
            issuePrompt2.setText(prompt);
            issuePrompt2.setVisibility(View.VISIBLE);
        } else if (habitsToggled) {
            prompt = "Click 'edit' to adopt a habit";
            issuePrompt2.setText(prompt);
            issuePrompt2.setVisibility(View.VISIBLE);
        }
        issuePrompt1.setVisibility(View.INVISIBLE);


        activitiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the array exists
                if (dataSnapshot.exists()) {
                    // Convert the snapshot into a List
                    days = (HashMap<String, Object>) dataSnapshot.getValue();
                    updateDisplay();
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        habitsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentHabits = (List<List<String>>) snapshot.getValue();
                } else currentHabits = new ArrayList<>();  //makes it empty
                updateDisplay();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        //initializes everything
        initUI(activitiesListener, dateText, yearText, activities, noActivities, dailyTotal);

        //Toggles calendar view
        calendarToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment cal = new CalendarFragment();
                cal.show(getParentFragmentManager(), "Calendar");
            }
        });

        //show user activities
        activitiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Please select an activity to edit/delete";
                issuePrompt2.setText(prompt);
                issuePrompt2.setVisibility(View.INVISIBLE);
                issuePrompt1.setVisibility(View.INVISIBLE);

                habitsToggled = false;
                switchToActivities();
            }
        });
        //show user habits
        habitsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habitsToggled = true;
                switchToHabits();

                String prompt;
                if (activities.getChildCount() != 0)
                    prompt = "Log a habit to reduce your carbon emissions!";
                else prompt = "Click 'edit' to adopt a habit";
                issuePrompt2.setText(prompt);
                issuePrompt2.setVisibility(View.VISIBLE);
                issuePrompt1.setVisibility(View.INVISIBLE);
            }
        });
        //add activities
        addListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //puts these away in case they were brought up, no need for them
                issuePrompt1.setVisibility(View.INVISIBLE);
                issuePrompt2.setVisibility(View.INVISIBLE);

                //passes date parameter so activity is added to current date
                AddActivity addActivity = new AddActivity(date);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_tracker, addActivity);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        addBtn.setOnClickListener(addListener);
        //edit activities
        editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //same code as in onCreateView()
                String prompt = "Please select an activity to edit/delete";
                issuePrompt2.setText(prompt);

                if (!activitySelected(activities, issuePrompt1, issuePrompt2)) return;
                int id = activities.getCheckedRadioButtonId();

                startEdit(id, userId);  //this retrieves the selected activity for editing and
                //boots up AddActivity fragment in edit mode via
                //"loadFragment(new AddActivity(date, activityToEdit));".
                //Note that upon clicking "save" btn in addactivity, the activity (as stored in firebase)
                //will be updated using updateChildren() method of firebase rtdb
            }
        };
        editBtn.setOnClickListener(editListener);
        //delete activities
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Please select an activity to edit/delete";
                issuePrompt2.setText(prompt);
                if (!activitySelected(activities, issuePrompt1, issuePrompt2)) return;
                int id = activities.getCheckedRadioButtonId();

                //code below removes associated activity in firebase rtdb
                //get the date of the activity we want to remove
                DatabaseReference dateRef = db.getReference(USER_DATA)
                        .child(userId)
                        .child("calendar")
                        .child(date);
                //get the activities of the date
                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if the array exists
                        if (dataSnapshot.exists()) {
                            // Convert the snapshot into a List
                            acts = (List<List<String>>) dataSnapshot.getValue();
                            delFromFirebase(dateRef, acts, id, noActivities);  //delete the activity
                            //next line polls firebase for update and updates ui via call to updateDisplay
                            //in "listener"
                            Toast.makeText(getContext(), "Activity Deleted", Toast.LENGTH_SHORT).show();
                            fetchSnapshot();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });

        return view;
    }


    /**
     * Deletes an activity from the firebase rtdb.
     * @param dateRef the date in which the activity is logged
     * @param acts the activities of the aforementioned date
     * @param id the id of the activity we want to delete
     */
    public void delFromFirebase(DatabaseReference dateRef, List<List<String>> acts, int id,
                                TextView noActivities) {
        acts.remove(id);  //removes the activity at position [id]
        if (acts.isEmpty())   //if no activities left on this date, remove the date from firebase
            dateRef.removeValue();
        else dateRef.setValue(acts);  //otherwise, set the activity list of date to be updated list
    }


    /**
     * Used to perform a one-time ping to the firebase to retrieve most up-to-date data
     */
    public static void fetchSnapshot() {
        calendarRef.addListenerForSingleValueEvent(activitiesListener);
        habitsRef.addListenerForSingleValueEvent(habitsListener);
    }

    /**
     * Initializes eco tracker UI
     * @param listener ValueEventListener to listen for firebase updates
     * @param d TextView for displaying current date in form "11 Nov" or "21 Sep"
     * @param y TextView for displaying current year in standard form
     * @param activities RadioGroup in which the radiobutton activities are displayed
     * @param noActivities  TextView with message "no activities logged yet for today"
     * @param dailyTotal TextView displaying the day's total emissions
     */
    public void initUI(ValueEventListener listener, TextView d, TextView y,
                       RadioGroup activities, TextView noActivities, TextView dailyTotal) {
        if (presetCalendar == 0) {
            //displays current date
            Calendar today = Calendar.getInstance();
            String t = today.get(Calendar.DAY_OF_MONTH) + " " +
                    months[today.get(Calendar.MONTH)];
            d.setText(t);
            y.setText(String.valueOf(today.get(Calendar.YEAR)));
            date = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-"
                    + today.get(Calendar.DAY_OF_MONTH);
        } else {  //occurs if start EcoTracker from AddActivity instead of through bottom bar/nav
            String[] t = date.split("-");
            String day = t[2]; String month = months[Integer.parseInt(t[1]) - 1]; String year = t[0];
            String x = day + " " + month;
            d.setText(x);
            y.setText(year);
        }
        //pings Firebase to show activities of current date
        fetchSnapshot();
        updateDisplay();
    }

    public void updateDisplay() {
        if (habitsToggled) {
            switchToHabits();
        } else {
            switchToActivities();
        }
    }

    public void switchToHabits() {
        RadioGroup rg = globalView.findViewById(R.id.activitiesGroup);
        Button logBtn = globalView.findViewById(R.id.addBtn);
        Button delBtn = globalView.findViewById(R.id.delBtn);
        Button editBtn = globalView.findViewById(R.id.editBtn);
        String t = "Log";
        logBtn.setText(t);  //don't need an edit button for habits
        delBtn.setVisibility(View.INVISIBLE);  //don't need a delete button for habits
        // ^^ add and delete feature of habits taken care of in "AddHabit.java"

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = globalView.findViewById(R.id.activitiesGroup);
                TextView issuePrompt1 = globalView.findViewById(R.id.issuePrompt1);
                TextView issuePrompt2 = globalView.findViewById(R.id.issuePrompt2);
                issuePrompt1.setVisibility(View.INVISIBLE);
                issuePrompt2.setVisibility(View.INVISIBLE);
                int id = rg.getCheckedRadioButtonId();

                if (id == -1) {
                    issuePrompt1.setVisibility(View.VISIBLE);

                    String prompt = "You must select a habit to log";
                    issuePrompt2.setText(prompt);
                    issuePrompt2.setVisibility(View.VISIBLE);
                    return;
                }

                Toast.makeText(getContext(), "New Activity Logged", Toast.LENGTH_SHORT).show();
                List<String> habitToLog = currentHabits.get(id);
                AddActivity.writeToFirebase(date, habitToLog, userId);

                fetchSnapshot();  //Update ecoTracker display
                updateDisplay();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHabit addHabit = new AddHabit(false);  //"false" prevents immediate return to ecotracker
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_tracker, addHabit);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //DialogFragment addHabit = new AddHabit();
                //addHabit.show(getParentFragmentManager(), "AddHabit");
            }
        });

        displayHabits();
    }

    public void displayHabits() {
        RadioGroup rg = globalView.findViewById(R.id.activitiesGroup);
        TextView emptyMsg = globalView.findViewById(R.id.emptyMsg);

        rg.clearCheck(); rg.removeAllViews();
        emptyMsg.setVisibility(View.INVISIBLE);
        emptyMsg.setText("No habits adopted yet");

        if (currentHabits.isEmpty()) {
            //set default ("no habits adopted yet")
            emptyMsg.setVisibility(View.VISIBLE);
            return;
        }

        RadioButton button;
        for (int i = 0; i < currentHabits.size(); i++) {
            button = new RadioButton(getContext());
            button.setId(i);
            String t = Double.parseDouble(currentHabits.get(i).get(2)) + "kg CO2: " +
                    currentHabits.get(i).get(1);
            button.setText(t);
            rg.addView(button);
        }
    }

    public void switchToActivities() {
        RadioGroup rg = globalView.findViewById(R.id.activitiesGroup);
        Button addBtn = globalView.findViewById(R.id.addBtn);
        Button delBtn = globalView.findViewById(R.id.delBtn);
        Button editBtn = globalView.findViewById(R.id.editBtn);
        delBtn.setVisibility(View.VISIBLE);

        String t = "Add";
        addBtn.setText(t);
        addBtn.setOnClickListener(addListener);
        editBtn.setOnClickListener(editListener);

        displayActivities();
    }

    public void displayActivities() {
        RadioGroup activities = globalView.findViewById(R.id.activitiesGroup);
        TextView emptyMsg = globalView.findViewById(R.id.emptyMsg);
        TextView dailyTotal = globalView.findViewById(R.id.dailyTotal);

        activities.clearCheck(); activities.removeAllViews();
        emptyMsg.setVisibility(View.INVISIBLE);
        emptyMsg.setText("No activities today yet");

        double emissions = 0.0;
        RadioButton btn;
        if (days.containsKey(date)) {  //if true, will display logged activities for selected day
            //stores a list containing lists representing the logged activities of the day
            List<List<Object>> day = (List<List<Object>>) days.get(date);
            for (int i = 0; i < day.size(); i++) {
                btn = new RadioButton(requireContext());
                btn.setId(i);  //this is used for delete and edit activity features
                String t = (Math.round(Double.parseDouble((String) day.get(i).get(2)) * 10) / 10.0)
                        + "kg CO2: " + day.get(i).get(1);  //the rounding prevents weird floating point stuff
                btn.setText(t);
                activities.addView(btn);
                emissions += Double.parseDouble(String.valueOf(day.get(i).get(2)));
            }
            emissions = Math.round(emissions * 10) / 10.0;  //round to 1 decimal
            String s = emissions + "kg of CO2 emitted";
            dailyTotal.setText(s);
        } else {
            //set default ("no activities today")
            emptyMsg.setVisibility(View.VISIBLE);
            dailyTotal.setText("0.0 kg of CO2 Emitted");
        }
    }


    private boolean activitySelected(RadioGroup activities, TextView issuePrompt1,
                                     TextView issuePrompt2) {
        int id = activities.getCheckedRadioButtonId();
        if (activities.getChildCount() == 0 || id == -1) {
            issuePrompt1.setVisibility(View.VISIBLE);
            issuePrompt2.setVisibility(View.VISIBLE);
            return false;
        }
        issuePrompt1.setVisibility(View.INVISIBLE);
        issuePrompt2.setVisibility(View.INVISIBLE);
        return true;
    }

    /**
     * From the firebase rtdb, retrieves the activity we want to edit
     * @param id index of the activity as in the firebase
     * @param userId id of the user who's using the app currently (as in the firebase)
     * @return
     */
    public void startEdit(int id, String userId) {
        DatabaseReference dateRef = db.getReference(USER_DATA)
                .child(userId)
                .child("calendar")
                .child(date);  //index of the activity in the list of activities for the date
        //get the activities of the date
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the array exists
                if (dataSnapshot.exists()) {
                    //gets date snapshot (list of activity (lists of strings))
                    //gets the list (activity) at position id (as wanted)
                    activityToEdit = ((List<List<String>>) dataSnapshot.getValue()).get(id);
                    if (isHabit(activityToEdit)) {  //case that activity is a logged habit
                        TextView issueprompt1 = globalView.findViewById(R.id.issuePrompt1);
                        TextView issueprompt2 = globalView.findViewById(R.id.issuePrompt2);
                        String prompt = "Habit logs cannot be edited";
                        issueprompt2.setText(prompt);
                        issueprompt1.setVisibility(View.VISIBLE);
                        issueprompt2.setVisibility(View.VISIBLE);
                        return;
                    }
                    //boots addActivity in edit mode, passing activityToEdit
                    AddActivity addActivity = new AddActivity(date, activityToEdit, id);
                    FragmentManager fragmentManager = getChildFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.eco_tracker, addActivity);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


    private void setAnnualEmissionsBtn(View view) {
        Button annualEmissionsBtn = view.findViewById(R.id.annualEmissionsBtn);
        annualEmissionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyResults surveyResults = new SurveyResults(true);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_tracker, surveyResults);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }



    private boolean isHabit(List<String> list) {
        return list.size() == 3;
    }


    public static void updateDate(CalendarDay day) {
        TextView dateText = globalView.findViewById(R.id.dateText);
        TextView yearText = globalView.findViewById(R.id.yearText);

        int d = day.getDay(); int m = day.getMonth(); int y = day.getYear();
        String date1 = d + " " + months[m-1];
        dateText.setText(date1);
        yearText.setText(String.valueOf(y));
        date = y + "-" + m + "-" + d;
        fetchSnapshot();  //updates displayed info to be for the selected date
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}