package com.example.planetze;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EcoBalanceDestinationFragment extends Fragment {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_eco_balance_destination_, container, false);

        CardView crd1 = view.findViewById(R.id.cd1);
        crd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CleanAirFragment cleanAirFragment = new CleanAirFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_balance_destination, cleanAirFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        CardView crd2 = view.findViewById(R.id.cd2);
        crd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlantTreeFragment plantATreeFragment = new PlantTreeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_balance_destination, plantATreeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        CardView crd3 = view.findViewById(R.id.cd3);
        crd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TechnologyFragment technologyFragment = new TechnologyFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_balance_destination, technologyFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

    CardView crd4 = view.findViewById(R.id.cd4);
        crd4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           WasteFragment wasteFragment = new WasteFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.eco_balance_destination, wasteFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    });

        CardView crd5 = view.findViewById(R.id.cd5);
        crd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FarmersFragment farmersFragment = new FarmersFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_balance_destination, farmersFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        CardView crd6 = view.findViewById(R.id.cd6);
        crd6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuelsFragment fuelsFragment = new FuelsFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.eco_balance_destination, fuelsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        return view;
    }
}