package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;


public class MarketTrendsFragment extends Fragment {

    TextView textview1;
    TextView textview2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_trends, container, false);


        textview1 = view.findViewById(R.id.hyperlink1);
        textview1.setMovementMethod(LinkMovementMethod.getInstance());

        textview2 = view.findViewById(R.id.hyperlink2);
        textview2.setMovementMethod(LinkMovementMethod.getInstance());

        // Inflate the layout for this fragment
        return view;
    }
}