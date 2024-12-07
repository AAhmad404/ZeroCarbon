package com.example.planetze.Login;

import static utilities.Constants.FIREBASE_LINK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planetze.ForgotPasswordFragment;
import com.example.planetze.HomeActivity;
import com.example.planetze.R;
import com.example.planetze.SignUpFragment;
import com.example.planetze.SurveyFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import utilities.UserData;

public class LoginView extends Fragment  {

    private EditText loginEmail, loginPass;

    private Button login;

    private TextView inputError, forgotpass;
    private Button googleSignUp;

    private LoginPresenter presenter;

    private TextView signUpLink;

    ActivityResultLauncher<Intent> launcher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initialize(view);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmail.getText().toString().trim();
                String pass = loginPass.getText().toString().trim();

                presenter.loginUser(email, pass);

            }
        });

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startGoogleSignin();
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ForgotPasswordFragment());
            }
        });
        return view;
    }

    public void setMessage(String msg) {

        inputError.setText(msg);
        if (!msg.trim().isEmpty()) {
            inputError.setTextSize(18);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,20,20,20);
            inputError.setLayoutParams(params);
        }

    }

    public Context getViewContext() {
        return getContext();
    }

    public void takeToSurvey() {
        loadFragment(new SurveyFragment());
    }

    public void takeToHub() {
        Intent intent = new Intent(getContext(), HomeActivity.class);

        // Prevent the user from being able to navigate back the login page using the return action.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private Activity getViewActivity() {
        return getActivity();
    }

    public void setSignUpLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        presenter.onSignInResult(result);
                    }
                });
    }

    //take to home fragment and connect so bottem bar can show

    public void startGoogleSignin() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(getViewActivity(), options);

        Intent intent = client.getSignInIntent();
        launcher.launch(intent);
    }

    private void initialize(View view) {
        signUpLink = view.findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(v -> loadFragment(new SignUpFragment()));

        loginEmail = view.findViewById(R.id.emailInput);
        loginPass = view.findViewById(R.id.passwordInput);
        login = view.findViewById(R.id.logInButton);
        inputError = view.findViewById(R.id.error);
        forgotpass = view.findViewById(R.id.forgotPasswordLink);

        googleSignUp = view.findViewById(R.id.signInWithGoogleButton);

        presenter = new LoginPresenter(new LoginModel(), this);

        presenter.setMessage(" ");
        presenter.setSignUpLauncher();
        UserData.logout(getViewContext());
    }


}
