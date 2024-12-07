package com.example.planetze;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class EcoHubEntryFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eco_hub_entry_, container, false);
        CardView crd1 = view.findViewById(R.id.learning_resourc);
        crd1.isClickable();
        CardView crd2 = view.findViewById(R.id.market_trends);
        crd2.isClickable();

        crd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LearningResourcesFragment learningResourcesFragment = new LearningResourcesFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nati, learningResourcesFragment, "tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        crd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarketTrendsFragment marketTrendsFragment = new MarketTrendsFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nati, marketTrendsFragment, "tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}