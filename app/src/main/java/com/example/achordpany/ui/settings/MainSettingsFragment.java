package com.example.achordpany.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.achordpany.R;

public class MainSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainsettings, container, false);

        // Theme Toggle Logic
        SwitchCompat themeSwitch = view.findViewById(R.id.theme_switch);
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int currentMode = AppCompatDelegate.getDefaultNightMode();

        // Set the switch to match current theme
        themeSwitch.setChecked(currentMode == AppCompatDelegate.MODE_NIGHT_YES);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int newMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(newMode);

            // Save preference
            prefs.edit().putInt("night_mode", newMode).apply();

            // Optionally recreate activity to apply theme immediately
            requireActivity().recreate();
        });

        // Button to Change Password
        View changePasswordBtn = view.findViewById(R.id.change_password_option);
        changePasswordBtn.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.changePassFragment);
        });

        // Button to See Terms & Conditions
        View termsConditionsOption = view.findViewById(R.id.terms_conditions_option);
        termsConditionsOption.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.termsConditionsFragment); // make sure this ID exists in your nav_graph.xml
        });

        View privacyPolicyOption = view.findViewById(R.id.privacy_policy_option);
        privacyPolicyOption.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.privacyPolicyFragment); // Make sure this ID exists in nav_graph
        });

        View aboutAppOption = view.findViewById(R.id.about_app_option);
        aboutAppOption.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.aboutAppFragment); // Make sure this ID exists in nav_graph
        });

        return view;
    }
}
