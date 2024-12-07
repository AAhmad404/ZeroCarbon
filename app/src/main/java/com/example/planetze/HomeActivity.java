package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.Login.LoginView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import utilities.UserData;

public class HomeActivity extends AppCompatActivity {

    private Button userSettings;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        pageTitle = findViewById(R.id.page_title);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Retrieve NavController from the NavHostFragment
        NavController navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.fragment))).getNavController();;

        // Setup the NavigationUI using the bottom navigation view and the navigation controller
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                pageTitle.setText(navDestination.getLabel());

            }
        });


        userSettings = findViewById(R.id.usersetting_button);
        userSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.initialize(getApplicationContext());
                Intent j = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(j);

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
