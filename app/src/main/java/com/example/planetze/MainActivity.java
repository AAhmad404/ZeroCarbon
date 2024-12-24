package com.example.planetze;

import android.content.Intent;

import static com.example.planetze.utils.Constants.FIREBASE_URL;
import static com.example.planetze.utils.Constants.STAY_LOGGED_ON;
import static com.example.planetze.utils.Constants.USER_DATA;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planetze.login.LoginView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.planetze.utils.UserData;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This block only for when user selects "Retake Survey" from survey results page
        Intent intent = getIntent();
        if (intent.hasExtra("retakeSurvey") && intent.getBooleanExtra("retakeSurvey", false)) {
            loadFragment(new SurveyFragment());
            return;
        }

        db = FirebaseDatabase.getInstance(FIREBASE_URL);
        userRef = db.getReference(USER_DATA);
        auth = FirebaseAuth.getInstance();

        initializeData();

        if (savedInstanceState == null) {
            onOpenApp();
        }

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void takeToHomePage() {
        userRef.get().addOnCompleteListener(task -> {
            DataSnapshot users = task.getResult();
            String userID = UserData.getUserID(getApplicationContext());
            for (DataSnapshot user : users.getChildren()) {
                Object inu = user.child("is_new_user").getValue();
                boolean cond1 = user.getKey().toString().trim().equals(userID);
                boolean cond2 = inu != null && inu.toString().equals("true");

                if (cond1 && cond2) {
                    loadFragment(new SurveyFragment());
                    break;
                } else if (cond1) {
                    navigateToHomeActivity();
                    break;
                }

            }
        });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void onOpenApp() {
        if (!UserData.isLoggedIn(getApplicationContext())) {
            loadFragment(new LoginView());
        } else {
            takeToHomePage();
        }
    }

    private void initializeData() {
        boolean isLoggedIn = UserData.isLoggedIn(getApplicationContext());
        boolean stayLoggedOn = UserData.getSetting(getApplicationContext(), STAY_LOGGED_ON);
        if (isLoggedIn && !stayLoggedOn) {
            UserData.logout(getApplicationContext());
        }
        if (isLoggedIn) {
            UserData.initialize(getApplicationContext());
        }
    }
}