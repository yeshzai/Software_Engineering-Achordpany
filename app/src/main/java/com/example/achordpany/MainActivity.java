package com.example.achordpany;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.achordpany.databinding.ActivityMainBinding;

import android.app.DatePickerDialog;
import java.util.Calendar;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private View activeTabIndicator; // The moving line above the active navbar item
    private BottomNavigationView bottomNavigationView;
    private int tabWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Views
        bottomNavigationView = findViewById(R.id.nav_view);
        activeTabIndicator = findViewById(R.id.active_tab_indicator);
        setContentView(R.layout.fragment_signup_step1);

        /* Initialize fields
        private View activeTabIndicator;
        private BottomNavigationView bottomNavigationView;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        View activeTabIndicator = findViewById(R.id.active_tab_indicator);*/

        setupNavigation();
        setupTabIndicator();

        // Find FloatingActionButton and set click listener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            // Handle the search or main action
            Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
        });
    }
        // Find BottomNavigationView
        //BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

    // Set up Navigation
    private void setupNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_bookmark, R.id.navigation_searchSong, R.id.navigation_history, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // Set up Tab Indicator (Moving Line)
    private void setupTabIndicator() {
        bottomNavigationView.post(() -> {
            // Ensure the indicator starts at the first tab
            tabWidth = bottomNavigationView.getWidth() / bottomNavigationView.getMenu().size();
            activeTabIndicator.setTranslationX(0); // Start at first tab
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
                int position = -1;

                if (item.getItemId() == R.id.navigation_home) {
                    position = 0;
                } else if (item.getItemId() == R.id.navigation_bookmark) {
                    position = 1;
                } else if (item.getItemId() == R.id.navigation_searchSong) {
                    position = 2;
                } else if (item.getItemId() == R.id.navigation_history) {
                    position = 3;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    position = 4;
                }

                if (position != -1) {
                    activeTabIndicator.animate().translationX(position * tabWidth).setDuration(200).start();
                }

                return true;
        });

        // Set initial indicator position (for the first tab)
        View firstMenuItemView = bottomNavigationView.getChildAt(0); // Get the first menu item view
        if (firstMenuItemView != null) {
            firstMenuItemView.post(() -> {
                int tabWidth = firstMenuItemView.getWidth();
                int tabLeft = firstMenuItemView.getLeft();

                // Center the indicator on the first tab
                float indicatorPosition = tabLeft + ((float)(tabWidth - activeTabIndicator.getWidth())) / 2;
                activeTabIndicator.setTranslationX(indicatorPosition);
            });
        }

    }

}