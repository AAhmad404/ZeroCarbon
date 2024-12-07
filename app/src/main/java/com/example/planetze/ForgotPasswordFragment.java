package com.example.planetze;

import static utilities.Constants.USER_DATA;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planetze.Login.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ArrayList<String> emailArray;
    private FirebaseDatabase db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText emailInput;
    private String email;

    private TextView message;

    private Button login, sendlink;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        auth = FirebaseAuth.getInstance();

        login = view.findViewById(R.id.returnButton);
        sendlink = view.findViewById(R.id.resetPasswordButton);
        emailInput = view.findViewById(R.id.emailInput);
        message = view.findViewById(R.id.msg);


        emailArray = new ArrayList<String>();

        db = FirebaseDatabase.getInstance("https://planetze-c3c95-default-rtdb.firebaseio.com/");
        userRef = db.getReference(USER_DATA);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LoginView());
            }
        });

        sendlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString().trim();
                sendPassReset(email);

            }
        });


        return view;


    }

    private void sendPassResetEmail(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    setMessage("Password reset link sent! Please check your email");
                }else{
                    setMessage("There was an error in sending verification email, please try again");
                }
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setMessage(String msg) {

        message.setText(msg);
        if (!msg.trim().isEmpty()) {
            message.setTextSize(18);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,20,20,20);
            message.setLayoutParams(params);
        }

    }


    private void sendPassReset(String email) {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                //emailArray = new ArrayList<String>();
                boolean equalsEmail = false;
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";
                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }
                    if (currentemail.equals(email)) {
                        equalsEmail = true;
                    }

                }
                if (equalsEmail) {
                    sendPassResetEmail(email);
                }
                else if (email.trim().isEmpty()) {
                    setMessage("Email cannot be empty");
                }
                else {
                    setMessage("There isn't an account accociated with that email");
                }


            }
        });
    }

}
