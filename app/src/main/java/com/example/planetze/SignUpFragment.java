package com.example.planetze;


import static android.app.Activity.RESULT_OK;
import static java.lang.Character.isLetter;
import static utilities.Constants.AUTH;
import static utilities.Constants.EMAIL;
import static utilities.Constants.FIREBASE_LINK;
import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.HIDE_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;
import static utilities.Constants.UNVERIFIED_USERS_REFERENCE;
import static utilities.Constants.USER_DATA;
import static utilities.Constants.USER_REFERENCE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import utilities.UserData;


public class SignUpFragment extends Fragment {

    private EditText signupEmail, signupPassword,signupConfirmPassword, fullName;
    private Button signupButton;

    private TextView inputError, signinLink;
    ActivityResultLauncher<Intent> launcher;
    private Button googleSignUp;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        setup(view);

        signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new LoginView());
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient client = GoogleSignIn.getClient(getActivity(), options);

                Intent intent = client.getSignInIntent();
                launcher.launch(intent);
            }
        });

        return view;

    }

    private void setup(View view) {
        signupEmail = view.findViewById(R.id.emailInput);
        signupPassword = view.findViewById(R.id.passwordInput);
        signupConfirmPassword = view.findViewById(R.id.confirmPasswordInput);
        signupButton = view.findViewById(R.id.registerButton);
        fullName = view.findViewById(R.id.nameInput);

        inputError = view.findViewById(R.id.error);

        signinLink = view.findViewById(R.id.signInLink);
        googleSignUp = view.findViewById(R.id.signUpWithGoogleButton);

        UserData.addUserstoDatabase();

        setSignUpLauncher();
    }

    private void setMessage(String msg) {
        inputError.setText(msg);
        if (!msg.trim().isEmpty()) {
            inputError.setTextSize(18);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(50,20,20,20);
            inputError.setLayoutParams(params);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean validPassword(String password) {
        boolean cond1 = password.length() >= 7;
        boolean cond2 = hasNumber(password);
        boolean cond3 = hasSpace(password);

        if (!cond1) {
            setMessage("Password has to be at least 7 character long");
            return false;
        }
        else if (!cond2) {
            setMessage("Password has to contains numbers");
            return false;
        }
        else if (cond3) {
            setMessage("Password cannot contain a space");
            return false;
        }

        return true;
    }

    private boolean hasNumber(String string) {
        ArrayList<Character> digits = new ArrayList<Character>();
        digits.add('0');digits.add('1');digits.add('2');digits.add('3');digits.add('4');
        digits.add('5');digits.add('6');digits.add('7');digits.add('8');digits.add('9');
        for (int i = 0; i < string.length(); i++) {
            if (digits.contains(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSpace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                return true;
            }
        }
        return false;
    }


    private boolean validInput(String email, String name, String password, String confirmPassword) {
        boolean cond1 = name == null || name.trim().isEmpty();
        boolean cond2 = email == null ||email.trim().isEmpty();
        boolean cond3 = validPassword(password);
        boolean cond4 = confirmPassword.equals(password);
        if (cond1) {
            setMessage("Name cannot be empty");
            return false;
        }
        else if (cond2) {
            setMessage("Email cannot be empty");
            return false;
        }
        if (!cond3) {
            return false;
        }
        else if (!cond4) {
            setMessage("Confirm Password has to match with password");
            return false;
        }
        return true;
    }


    private void signup() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String confirmPassword = signupConfirmPassword.getText().toString().trim();
        String name = fullName.getText().toString().trim();

        boolean validInput = validInput(email, name, password, confirmPassword);

        if (validInput) {
            createAccountOnFirebase(email, password, name);
        }

    }

    private void checkExistingUnverifiedAccount(String name, String email, String password) {

        UNVERIFIED_USERS_REFERENCE.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                boolean equalsEmail = false;
                String oldPassword = "";

                DataSnapshot users = task.getResult();
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";

                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }

                    if (currentemail.equals(email)) {
                        equalsEmail = true;
                        oldPassword = user.child("password").getValue(String.class).toString().trim();
                        break;
                    }

                }
                if (equalsEmail) {
                    recreateAccount(name, email, password, oldPassword);
                }
                else {
                    setMessage("Email already accociated with an account");
                }
            }
        });
    }

    private void recreateAccount(String name, String email, String password, String oldPassword) {

        AUTH.signInWithEmailAndPassword(email, oldPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = AUTH.getCurrentUser();
                            String userID = user.getUid().toString().trim();
                            UNVERIFIED_USERS_REFERENCE.child(userID).removeValue();
                            initialize(userID, email, password, name);
                            user.updatePassword(password);
                            sendVerification();
                        }
                    }
                });

    }

    private void createAccountOnFirebase(String email, String password, String name) {
        AUTH.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String id = AUTH.getCurrentUser().getUid();
                            initialize(id, email, password, name);
                            sendVerification();
                        } else {
                            checkExistingUnverifiedAccount(name, email, password);
                        }
                    }
                });
    }

    private void sendVerification() {
        Activity activity = getActivity();
        AUTH.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(activity, "there was an error in sending verification email", Toast.LENGTH_LONG).show();
                }
                loadFragment(new ResendConfirmFragment());
            }
        });
    }

    private void initialize(String userID, String email, String password, String name) {

        UNVERIFIED_USERS_REFERENCE.child(userID+"/email").setValue(email);
        UNVERIFIED_USERS_REFERENCE.child(userID+"/password").setValue(password);
        UNVERIFIED_USERS_REFERENCE.child(userID+"/name").setValue(name);

    }

    public void setSignUpLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        onGoogleActivityResult(result);
                    }
                });
    }

    public void onGoogleActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                AUTH.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            googleSignin();
                        }
                        else {

                        }
                    }
                });

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    private void googleSignin() {
        USER_REFERENCE.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserData.login(requireContext(),AUTH.getCurrentUser().getUid());
                UserData.initialize(getContext());
                DataSnapshot users = task.getResult();
                boolean equalsEmail = false;
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";
                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }
                    if (currentemail.equals(UserData.getData(getContext(),EMAIL))) {
                        equalsEmail = true;
                    }

                }

                if (!equalsEmail) {
                    String id = UserData.getUserID(getContext());
                    String name = AUTH.getCurrentUser().getDisplayName();
                    String email = UserData.getData(getContext(),EMAIL);

                    UserData.setDefaultSettings(id,email, name);
                    loadFragment(new SurveyFragment());
                }
                else {
                    isNewUser();
                }

            }
        });
    }

    private void isNewUser() {
        //for google sign in only
        USER_REFERENCE.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = AUTH.getCurrentUser().getUid();
            for(DataSnapshot user:users.getChildren()) {
                Object inu = user.child("is_new_user").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = inu != null && inu.toString().equals("true");

                if (cond1 && cond2) {
                    loadFragment(new SurveyFragment());
                    break;
                }
                else if (cond1) {
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    // Prevent the user from being able to navigate back the login page using the return action.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                }

            }
        });
    }



}