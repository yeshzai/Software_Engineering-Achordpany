package com.example.achordpany.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.achordpany.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SignupStep4Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_signup_step4, container, false);

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        Button btnSignupEnd = view.findViewById(R.id.btnSignupEnd);
        TextView textHaveAccount = view.findViewById(R.id.textHaveAccount);

        // Handle Back Button Click - Navigate to WelcomeActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
            startActivity(intent);
        });

        // Handle Continue Button Click
        btnSignupEnd.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_signup4_to_home);
        });

        // If user clicks signup, navigate to MainActivity and open SignupFragment1
        textHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.putExtra("navigateToLogin", true); // Extra to tell MainActivity to open SignupFragment
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if BottomNavigationView exists before modifying visibility
        if (getActivity() != null) {
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.nav_view);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
            }
        }
    }


}
