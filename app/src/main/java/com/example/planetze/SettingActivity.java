package com.example.planetze;

import static utilities.Constants.EMAIL;
import static utilities.Constants.FIREBASE_LINK;
import static utilities.Constants.HIDE_GRID_LINES;
import static utilities.Constants.INTERPOLATE_EMISSIONS_DATA;
import static utilities.Constants.HIDE_TREND_LINE_POINTS;
import static utilities.Constants.STAY_LOGGED_ON;
import static utilities.Constants.USER_DATA;
import static utilities.Constants.USERNAME;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.planetze.Login.LoginView;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import utilities.UserData;

public class SettingActivity extends AppCompatActivity {

    private SwitchMaterial stayLoggedOnSwitch;
    private SwitchMaterial interpolateEmissionsDataSwitch;
    private SwitchMaterial hideGridLinesSwitch;
    private SwitchMaterial hideTrendLinePointsSwitch;

    private Button returnButton;
    private Button logoutButton;

    private Button deleteAccountButton;
    private TextView changeName;

    private TextView name;
    private TextView email;

    private DatabaseReference userRef;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialize();
        initializeSwitch();

        String userID = UserData.getUserID(getApplicationContext());

        returnButton.setOnClickListener(view -> {
            UserData.initialize(getApplicationContext());
            Intent j = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(j);
        });

        changeName.setOnClickListener(view -> {
            setChangeNamePopup();
        });

        logoutButton.setOnClickListener(view -> {
            UserData.logout(getApplicationContext());
            loadFragment(new LoginView());
        });

        deleteAccountButton.setOnClickListener(view -> {
            setDeleteAccountPopup();
        });

        stayLoggedOnSwitch.setOnClickListener(view -> {
            switchFunction(userID, STAY_LOGGED_ON, stayLoggedOnSwitch);
        });


        interpolateEmissionsDataSwitch.setOnClickListener(view -> {
            switchFunction(userID, INTERPOLATE_EMISSIONS_DATA, interpolateEmissionsDataSwitch);

        });

        hideGridLinesSwitch.setOnClickListener(view -> {
            switchFunction(userID, HIDE_GRID_LINES, hideGridLinesSwitch);
        });

        hideTrendLinePointsSwitch.setOnClickListener(view -> {
            switchFunction(userID, HIDE_TREND_LINE_POINTS, hideTrendLinePointsSwitch);
        });
    }

    private void showMessage(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(msg+"\n\n\n");
        alert.show();
    }

    private void setChangeNamePopup() {
        String userID = UserData.getUserID(getApplicationContext());
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter new name");

        EditText input = new EditText(getApplicationContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String nameText = input.getText().toString().trim();
                if (nameText.isEmpty()) {
                    showMessage("New name cannot be empty");
                }
                else {
                    userRef.child(userID+"/name").setValue(nameText);
                    showMessage("Name changed successfully");
                    UserData.initialize(getApplicationContext());
                    name.setText(nameText);
                }
                dialog.cancel();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    private void setDeleteAccountPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);

        builder.setTitle("Are you sure you want to delete this account?");
        builder.setMessage("This action cannot be undone");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserData.deleteAccount(getApplicationContext());
                loadFragment(new LoginView());
                showMessage("Account deleted successfully");
                dialog.cancel();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initializeSwitch() {
        boolean stayLoggedOn = UserData.getSetting(getApplicationContext(),STAY_LOGGED_ON);
        boolean interpolateEmissionsData = UserData.getSetting(getApplicationContext(),INTERPOLATE_EMISSIONS_DATA);
        boolean hideGridLines = UserData.getSetting(getApplicationContext(),HIDE_GRID_LINES);
        boolean hideTrendLinePoints = UserData.getSetting(getApplicationContext(), HIDE_TREND_LINE_POINTS);

        stayLoggedOnSwitch.setChecked(stayLoggedOn);
        interpolateEmissionsDataSwitch.setChecked(interpolateEmissionsData);
        hideGridLinesSwitch.setChecked(hideGridLines);
        hideTrendLinePointsSwitch.setChecked(hideTrendLinePoints);
    }

    private void initialize() {
        returnButton = findViewById(R.id.returnButton);
        logoutButton = findViewById(R.id.logoutButton);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        changeName = findViewById(R.id.change_name);

        stayLoggedOnSwitch = findViewById(R.id.stay_logged_in_switch);
        interpolateEmissionsDataSwitch = findViewById(R.id.ied_switch);
        hideTrendLinePointsSwitch = findViewById(R.id.hide_trend_line_points_switch);
        hideGridLinesSwitch = findViewById(R.id.hide_grid_lines_switch);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        name.setText(UserData.getData(getApplicationContext(), USERNAME));
        email.setText(UserData.getData(getApplicationContext(), EMAIL));

        db = FirebaseDatabase.getInstance(FIREBASE_LINK);
        userRef = db.getReference(USER_DATA);
    }

    private void switchFunction(String userID, String settingName, SwitchMaterial settingSwitch) {
        if (settingSwitch.isChecked()) {
            userRef.child(userID+"/settings/"+settingName).setValue(true);
        }
        else {
            userRef.child(userID+"/settings/"+settingName).setValue(false);
        }
        UserData.initialize(getApplicationContext());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}