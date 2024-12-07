package com.example.planetze.Login;

import static android.app.Activity.RESULT_OK;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.example.planetze.ResendConfirmFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

import utilities.UserData;

public class LoginModel {

    public LoginModel() {
        UserData.addUserstoDatabase();
    }

    public void loginUser(String email, String password, LoginPresenter presenter) {

        AUTH.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = AUTH.getCurrentUser();
                    String userID = user.getUid().toString().trim();
                    if (user.isEmailVerified()) {
                        UserData.login(presenter.getViewContext(), userID);
                        takeToHomePage(presenter);
                    } else {
                        presenter.setMessage("Account needs to be verified");
                        AUTH.signOut();
                    }

                } else {
                    presenter.setMessage("Invalid email or password");
                }
            }
        });
    }

    private void addUsertoDatabase(LoginPresenter presenter) {
        UNVERIFIED_USERS_REFERENCE.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot users = task.getResult();
                for(DataSnapshot user:users.getChildren()) {
                    if (user.hasChild("email")) {
                        String email = user.child("email").getValue().toString().trim();
                        String name = user.child("name").getValue().toString().trim();
                      
                        String userID = UserData.getUserID(presenter.getViewContext());
                        if (email.equals(AUTH.getCurrentUser().getEmail().trim())) {
                            UNVERIFIED_USERS_REFERENCE.child(userID).removeValue();
                            UserData.setDefaultSettings(userID, email, name);
                            break;
                        }

                    }

                }

            }
        });
    }


    private void takeToHomePage(LoginPresenter presenter) {
        UserData.initialize(presenter.getViewContext());
        USER_REFERENCE.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(presenter.getViewContext());

            if (users.hasChild(userID)) {
                for(DataSnapshot user:users.getChildren()) {
                    Object inu = user.child("is_new_user").getValue();
                    boolean cond1 = user.getKey().toString().trim().equals(userID);
                    boolean cond2 = inu != null && inu.toString().equals("true");

                    if (cond1 && cond2) {
                        presenter.takeToSurvey();
                        break;
                    }
                    else if (cond1) {
                        presenter.takeToHub();
                        break;
                    }

                }
            }
            else {
                addUsertoDatabase(presenter);
                presenter.takeToSurvey();
            }

        });


    }
    public void onSignInResult(ActivityResult result, LoginPresenter presenter) {
        if (result.getResultCode() == RESULT_OK) {
            presenter.setMessage("Launched Google sign up");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                AUTH.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            googleSignin(presenter);
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

    private void setDefaultSettings(String userID, String email, String name) {
        USER_REFERENCE.child(userID+"/email").setValue(email);
        USER_REFERENCE.child(userID+"/name").setValue(name);
        USER_REFERENCE.child(userID+"/is_new_user").setValue(true);
        USER_REFERENCE.child(userID+"/settings/"+STAY_LOGGED_ON).setValue(false);
        USER_REFERENCE.child(userID+"/settings/"+INTERPOLATE_EMISSIONS_DATA).setValue(false);
        USER_REFERENCE.child(userID+"/settings/"+ HIDE_TREND_LINE_POINTS).setValue(false);
        USER_REFERENCE.child(userID+"/settings/"+HIDE_GRID_LINES).setValue(false);
        USER_REFERENCE.child(userID+"/calendar/0000-00-00/0").setValue(0);

    }

    private void googleSignin(LoginPresenter presenter) {
        USER_REFERENCE.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                UserData.login(presenter.getViewContext(), AUTH.getCurrentUser().getUid());
                UserData.initialize(presenter.getViewContext());
                DataSnapshot users = task.getResult();
                boolean equalsEmail = false;
                for(DataSnapshot user:users.getChildren()) {
                    String currentemail = " ";
                    if (user.hasChild("email")) {
                        currentemail = user.child("email").getValue(String.class).toString().trim();
                    }
                    if (currentemail.equals(UserData.getData(presenter.getViewContext(), EMAIL))) {
                        equalsEmail = true;
                    }

                }

                if (!equalsEmail) {

                    String id = UserData.getUserID(presenter.getViewContext());
                    String name = AUTH.getCurrentUser().getDisplayName();
                    String email = UserData.getData(presenter.getViewContext(), EMAIL);

                    setDefaultSettings(id,email, name);
                    presenter.takeToSurvey();
                }
                else {
                    takeToHomePage(presenter);
                }

            }
        });
    }

    public boolean isLoggedIn() {
        return AUTH.getCurrentUser()!=null;
    }



}



