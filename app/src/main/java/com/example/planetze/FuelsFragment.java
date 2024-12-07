package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FuelsFragment extends Fragment {

    TextView textView1;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fuels_fragment, container, false);

        textView1 = view.findViewById(R.id.bt6);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());


        return view;
    }
}