package com.example.achordpany;

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
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private View activeTabIndicator;
    private BottomNavigationView bottomNavigationView;
    private int tabWidth;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Views
        bottomNavigationView = findViewById(R.id.nav_view);
        activeTabIndicator = findViewById(R.id.active_tab_indicator);
        ImageView profileImage = findViewById(R.id.profile_image);
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
        Glide.with(this)
                .load("file:///android_asset/profile_images/horse.png")
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(profileImage);

        // Set up navigation & tab indicator
        setupNavigation();
        setupTabIndicator();

        // Ensure Home is the default selected tab
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        headerTitle.setText("Dashboard");
        subtextView.setText("Navigation section");

        // FloatingActionButton click listener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SongSearchActivity.class);
            startActivity(intent);
        });

        // Dropdown button click listener
        dropdownButton.setOnClickListener(this::showProfileMenu);

        // Handle Bottom Navigation Item Clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
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

            // ✅ Add listener to hide/show UI elements
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                boolean isSignupFlow = destination.getId() == R.id.signupFragment1 ||
                        destination.getId() == R.id.signupFragment2 ||
                        destination.getId() == R.id.signupFragment3 ||
                        destination.getId() == R.id.signupFragment4;

                findViewById(R.id.header_layout).setVisibility(isSignupFlow ? View.GONE : View.VISIBLE);
                findViewById(R.id.fab).setVisibility(isSignupFlow ? View.GONE : View.VISIBLE);
                findViewById(R.id.fab_background).setVisibility(isSignupFlow ? View.GONE : View.VISIBLE);
                findViewById(R.id.active_tab_indicator).setVisibility(isSignupFlow ? View.GONE : View.VISIBLE);
            });

            // ✅ Navigate to Signup Step 1 if needed
            if (getIntent().getBooleanExtra("navigateToSignup", false)) {
                navController.navigate(R.id.signupFragment1);
            }

            // ✅ Navigate to Chords if needed
            //if (getIntent().getBooleanExtra("openChords", false)) {
            //    navController.navigate(R.id.navigation_chords);
            //}

        }, 500); // ✅ Small delay to ensure UI is fully loaded before navigation


        /*new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // ✅ Get NavController inside the delay to avoid null errors
            //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavHostFragment navHostFragment = (NavHostFragment)
                    getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();

                navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                    if (destination.getId() == R.id.signupFragment1 ||
                            destination.getId() == R.id.signupFragment2 ||
                            destination.getId() == R.id.signupFragment3 ||
                            destination.getId() == R.id.signupFragment4) {

                        // ✅ Hide header & FAB
                        findViewById(R.id.header_layout).setVisibility(View.GONE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                        findViewById(R.id.fab_background).setVisibility(View.GONE);
                        findViewById(R.id.active_tab_indicator).setVisibility(View.GONE);
                    } else {
                        // ✅ Show header & FAB for other fragments
                        findViewById(R.id.header_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.VISIBLE);
                        findViewById(R.id.fab_background).setVisibility(View.VISIBLE);
                        findViewById(R.id.active_tab_indicator).setVisibility(View.VISIBLE);
                    }
                });

                // ✅ Navigate to Signup Step 1 if needed
                if (getIntent().getBooleanExtra("navigateToSignup", false)) {
                    navController.navigate(R.id.signupFragment1);
                }

                // ✅ Navigate to Chords if needed
                if (getIntent().getBooleanExtra("openChords", false)) {
                    navController.navigate(R.id.navigation_chords);
                }

            } else {
                Log.e("MainActivity", "NavHostFragment is NULL! Cannot add destination listener.");
            }
        }, 500); // ✅ Small delay to ensure UI is fully loaded before navigation*/



        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.signupFragment1 ||
                    destination.getId() == R.id.signupFragment2 ||
                    destination.getId() == R.id.signupFragment3 ||
                    destination.getId() == R.id.signupFragment4) {

                // Hide header and FAB
                findViewById(R.id.header_layout).setVisibility(View.GONE);
                findViewById(R.id.fab).setVisibility(View.GONE);
            } else {
                // Show them on other fragments
                findViewById(R.id.header_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });


        // Check if LoginActivity sent "openSignup"
        if (getIntent().getBooleanExtra("navigateToSignup", false)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                navController.navigate(R.id.signupFragment1);
            }, 500); // Small delay to ensure UI is fully loaded
        }

        if (getIntent().getBooleanExtra("openChords", false)) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                NavHostFragment navHostFragment =
                        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

                if (navHostFragment != null) {
                    navController.navigate(R.id.navigation_chords);
                } else {
                    Log.e("MainActivity", "NavHostFragment is NULL! Cannot navigate.");
                }
            }, 500); // Small delay to ensure UI is fully loaded
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        NavController navController = getNavController();
        if (navController == null) return;

        // Navigate to Chords fragment if needed
        if (getIntent().getBooleanExtra("openChords", false)) {
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() != R.id.navigation_chords) {

                navController.navigate(R.id.navigation_chords);
            }

            getIntent().removeExtra("openChords"); // Prevents multiple navigations
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
        if (item.getItemId() == R.id.menu_general_settings) {
            Toast.makeText(this, "General Settings Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.menu_profile_settings) {
            Toast.makeText(this, "Profile Settings Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            Toast.makeText(this, "Log Out Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    // Set up Navigation
    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else {
            Log.e("MainActivity", "NavHostFragment is NULL! Check activity_main.xml");
            Toast.makeText(this, "Navigation setup failed", Toast.LENGTH_SHORT).show();
        }

        /*if (navHostFragment == null) {
            Toast.makeText(this, "NavHostFragment not found", Toast.LENGTH_SHORT).show();
            return;
        }

        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_bookmark, R.id.navigation_searchSong, R.id.navigation_history, R.id.navigation_profile)
                .build();

        NavigationUI.setupWithNavController(binding.navView, navController);*/
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
