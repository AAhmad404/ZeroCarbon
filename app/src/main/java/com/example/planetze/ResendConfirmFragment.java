package com.example.planetze;

//import static com.google.firebase.database.core.operation.OperationSource.Source.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ResendConfirmFragment extends Fragment {

    private TextView message;
    private Button resendConfirm, login;
    private String email;
    private FirebaseAuth auth;

    public ResendConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resend_confirm, container, false);

        message = view.findViewById(R.id.message);
        resendConfirm = view.findViewById(R.id.resendButton);
        login = view.findViewById(R.id.loginButton);
        auth = FirebaseAuth.getInstance();

        resendConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            setMessage("Verification email sent!");
                        }else{
                            setMessage("There was an error in sending verification email, please try again.");
                        }
                    }
                });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LoginView());
            }
        });

        return view;
    }

    private void setMessage(String msg) {
        message.setText(msg);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
