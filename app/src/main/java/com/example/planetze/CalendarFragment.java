package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CalendarFragment extends DialogFragment {
    static View globalView;
    static FirebaseDatabase db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");;
    String userId = EcoTrackerFragment.userId;  //this is legal because CalendarFragment is only ever called through ecotracker

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        globalView = view;
        setCalendarDecorators(userId);

        MaterialCalendarView calendarView = view.findViewById(R.id.cal);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int d = date.getDay(), m = date.getMonth(), y = date.getYear();
                EcoTrackerFragment.updateDate(date);
            }
        });

        return view;
    }


    /**
     * Sets little color decorators on each day that has activities logged so user can easily
     * know which days to navigate to to see logged activities
     * @param userId
     */
    public static void setCalendarDecorators(String userId) {
        MaterialCalendarView calendarView = globalView.findViewById(R.id.cal);
        DatabaseReference calendarRef = db.getReference(USER_DATA)
                .child(userId).child("calendar");
        calendarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> calendar = (HashMap<String, Object>) snapshot.getValue();
                Set<String> dates = (Set<String>) calendar.keySet();
                dates.remove("0000-00-00");  //gets rid of default date

                List<CalendarDay> eventDates = new ArrayList<>();  //populates set of days
                for (String date : dates) {
                    String[] ymd = date.split("-");
                    int y = Integer.parseInt(ymd[0]);
                    int m = Integer.parseInt(ymd[1]);
                    int d = Integer.parseInt(ymd[2]);
                    eventDates.add(CalendarDay.from(y, m, d));
                }

                calendarView.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        return eventDates.contains(day);
                    }
                    @Override
                    public void decorate(DayViewFacade view) {
                        view.addSpan(new DotSpan(10, Color.MAGENTA));
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}