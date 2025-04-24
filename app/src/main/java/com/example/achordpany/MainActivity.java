package com.example.achordpany;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;

import com.example.achordpany.ui.auth.WelcomeActivity;
import com.example.achordpany.ui.chords.ChordsDisplayActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.ui.search.SongSearchActivity;
import com.example.achordpany.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private View activeTabIndicator;
    private BottomNavigationView bottomNavigationView;
    private int tabWidth;
    private SharedViewModel sharedViewModel;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int savedMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);*/
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);


        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Views
        bottomNavigationView = findViewById(R.id.nav_view);
        activeTabIndicator = findViewById(R.id.active_tab_indicator);
        //ImageView profileImage = findViewById(R.id.profile_image);
        CircleImageView profileImageView = findViewById(R.id.profile_image);
        ImageButton dropdownButton = findViewById(R.id.profile_dropdown);
        TextView headerTitle = findViewById(R.id.header_title);
        TextView subtextView = findViewById(R.id.subtext);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Observe the title LiveData and update the UI whenever it changes
        sharedViewModel.getTitle().observe(this, newTitle -> {
            if (headerTitle != null) {
                headerTitle.setText(newTitle);
            }
        });

        // Observe the subtext LiveData and update the subtext
        sharedViewModel.getSubtext().observe(this, newSubtext -> {
            Log.d("MainActivity", "Subtext updated: " + newSubtext);
            if (subtextView != null) {
                subtextView.setText(newSubtext);
            }
        });

        // Load Profile Image using Glide
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        Uri avatarUID_Path = Uri.parse(main_EverythingLocalDatabase.get_AvatarUID());

        Glide.with(this)
                .load(avatarUID_Path)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(profileImageView);

        // Set up navigation & tab indicator
        setupNavigation();
        setupTabIndicator();

        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Back button click logic here
        /*ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (navController != null) {
                navController.popBackStack();
            }
        });*/

        ImageView backButton = findViewById(R.id.back_button);

        // Set click listener only once
        backButton.setOnClickListener(v -> {
            if (!navController.popBackStack()) {
                navController.navigate(R.id.navigation_home); // fallback
            }
        });

        // Destination listener for UI visibility
        if (navController != null) {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                BottomNavigationView bottomNav = findViewById(R.id.nav_view);
                FloatingActionButton fab = findViewById(R.id.fab);
                View fabBackground = findViewById(R.id.fab_background);
                View activeTabIndicator = findViewById(R.id.active_tab_indicator);
                //ImageView backButton = findViewById(R.id.back_button);

                int destId = destination.getId();

                if (destId == R.id.mainSettingsFragment ||
                        destId == R.id.changePassFragment ||
                        destId == R.id.termsConditionsFragment ||
                        destId == R.id.privacyPolicyFragment ||
                        destId == R.id.aboutAppFragment) {
                    bottomNav.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                    fabBackground.setVisibility(View.GONE);
                    activeTabIndicator.setVisibility(View.GONE);
                    backButton.setVisibility(View.VISIBLE);
                } else {
                    bottomNav.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    fabBackground.setVisibility(View.VISIBLE);
                    activeTabIndicator.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.GONE);
                }
            });
        }

        // Ensure Home is the default selected tab
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        headerTitle.setText("Dashboard");
        subtextView.setText("Navigation section");

        // Open Profile - When profile image is clicked.
        profileImageView.setOnClickListener(v -> {
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
            }
        });

        // FloatingActionButton click listener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SongSearchActivity.class);
            startActivity(intent);
            finish();
        });

        // Dropdown button click listener
        dropdownButton.setOnClickListener(this::showProfileMenu);

        // Handle Bottom Navigation Item Clicks
        /*bottomNavigationView.setOnItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (handled) {
                updateTabIndicator(item.getItemId());
                updateHeaderTitle(item.getItemId());
                updateSubText(item.getItemId());
            }

            return handled;
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            NavController navController = getNavController();
            if (navController == null) return; // Exit if null to prevent crashes

        }, 500); // Small delay to ensure UI is fully loaded before navigation
        */

        bottomNavigationView.setOnItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (handled) {
                updateTabIndicator(item.getItemId());
                updateHeaderTitle(item.getItemId());
                updateSubText(item.getItemId());
            }

            return handled;
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (navController == null) return; // Exit if null to prevent crashes

        }, 500); // Small delay to ensure UI is fully loaded before navigation


        // Load saved profile image from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String imageUriString = prefs.getString("profile_image_uri", null);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            //profileImageView.setImageURI(imageUri);
            Glide.with(this).load(imageUri).into(profileImageView);
        }

        // Hide the header layout of activity_main when chord display is navigated
        /*NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.navigation_chords) {
                    findViewById(R.id.header_layout).setVisibility(View.GONE);

                    //if (activeTabIndicator != null) {
                    //    activeTabIndicator.setVisibility(View.INVISIBLE);
                    //}
                } else {
                    findViewById(R.id.header_layout).setVisibility(View.VISIBLE);

                    //if (activeTabIndicator != null) {
                    //    activeTabIndicator.setVisibility(View.VISIBLE);
                    //}
                }
            });
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*NavController navController = getNavController();
        if (navController == null) return;

        // Navigate to Chords fragment if needed
        if (getIntent().getBooleanExtra("openChords", false)) {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.navigation_chords) {

                navController.navigate(R.id.navigation_chords);
            }

            getIntent().removeExtra("openChords"); // Prevents multiple navigations
        }*/
        if (getIntent().getBooleanExtra("openChords", false)) {
            Intent intent = new Intent(this, ChordsDisplayActivity.class);
            startActivity(intent);

            getIntent().removeExtra("openChords"); // Prevents multiple openings
        }
    }


    private NavController getNavController() {
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            return navHostFragment.getNavController();
        } else {
            Log.e("MainActivity", "NavHostFragment is NULL! Cannot get NavController.");
            return null;
        }
    }


    // Show Profile Dropdown Menu
    private void showProfileMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.show();
    }

    // Handle Menu Item Clicks
    private boolean onMenuItemClick(MenuItem item) {
        NavController navController = getNavController();
        if (navController == null) {
            Log.e("MainActivity", "NavController is null, navigation failed.");
            return false;
        }

        if (item.getItemId() == R.id.menu_general_settings) {
            //Toast.makeText(this, "General Settings Clicked", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.mainSettingsFragment); // Navigate to MainSettingsFragment
            sharedViewModel.setTitle("SETTINGS");
            sharedViewModel.setSubtext("Manage your preferences");
            return true;
        } else if (item.getItemId() == R.id.menu_profile_settings) {
            //Toast.makeText(this, "Profile Settings Clicked", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.navigation_profile); // Navigate to MainSettingsFragment
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(this, "Logout Successful!", Toast.LENGTH_SHORT).show();

            // Logout Firebase
            FirebaseAuth current_user = FirebaseAuth.getInstance();
            current_user.signOut();

            if(current_user.getCurrentUser() == null)
                Log.d("[FIREBASE LOGOUT]", "SUCCESS");
            else
                Log.d("[FIREBASE LOGOUT]", "FAILED");

            // Restart application
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Runtime.getRuntime().exit(0); // Kill & restart app
            }, 300); // Delay prevents abrupt black screen

            return true;
        }
        return false;
    }

    // Set up Navigation
    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            //NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            Log.e("MainActivity", "NavHostFragment is NULL! Check activity_main.xml");
            Toast.makeText(this, "Navigation setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Set up Tab Indicator (Moving Line)
    private void setupTabIndicator() {
        bottomNavigationView.post(() -> {
            // Calculate tab width
            tabWidth = bottomNavigationView.getWidth() / bottomNavigationView.getMenu().size();
            updateTabIndicator(R.id.navigation_home);
        });
    }

    // Update Tab Indicator Position
    private void updateTabIndicator(int itemId) {
        View selectedView = bottomNavigationView.findViewById(itemId);
        if (selectedView == null) return; // Prevent crashes

        selectedView.post(() -> {
            int tabLeft = selectedView.getLeft();
            int tabWidth = selectedView.getWidth();
            float indicatorPosition = tabLeft + ((float) (tabWidth - activeTabIndicator.getWidth())) / 2;

            activeTabIndicator.animate().translationX(indicatorPosition).setDuration(200).start();
        });
    }

    // Update Header Title Based on Selected Tab
    private void updateHeaderTitle(int itemId) {
        TextView headerTitle = findViewById(R.id.header_title);
        if (itemId == R.id.navigation_home) {
            headerTitle.setText("Home");
        } else if (itemId == R.id.navigation_bookmark) {
            headerTitle.setText("Bookmarks");
        } else if (itemId == R.id.navigation_searchSong) {
            headerTitle.setText("Search");
        } else if (itemId == R.id.navigation_history) {
            headerTitle.setText("History");
        } else if (itemId == R.id.navigation_profile) {
            headerTitle.setText("Profile");
        }
    }

    private void updateSubText(int itemId) {
        TextView subtextView = findViewById(R.id.subtext);
        if (itemId == R.id.navigation_home) {
            subtextView.setText("Navigation section");
        } else if (itemId == R.id.navigation_bookmark) {
            subtextView.setText("Bookmarked songs");
        } else if (itemId == R.id.navigation_searchSong) {
            subtextView.setText("Search a song");
        } else if (itemId == R.id.navigation_history) {
            subtextView.setText("Recent history");
        } else if (itemId == R.id.navigation_profile) {
            subtextView.setText("Account section");
        }
    }
}
