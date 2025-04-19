package com.example.achordpany.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
