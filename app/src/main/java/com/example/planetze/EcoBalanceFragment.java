package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EcoBalanceFragment extends Fragment {

    public EcoBalanceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_eco_balance_fragment, container, false);
        Button btn1 = view.findViewById(R.id.button4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EcoBalanceDestinationFragment ecoBalanceDestinationFragment = new EcoBalanceDestinationFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.balance_entry, ecoBalanceDestinationFragment);
                transaction.addToBackStack("nati");
                transaction.commit();
            }
        });


        return view;
    }
}