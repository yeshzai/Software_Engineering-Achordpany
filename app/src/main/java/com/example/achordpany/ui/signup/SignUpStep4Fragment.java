package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.WelcomeActivity;

public class SignUpStep4Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_4, container, false);

        Button btnSignUpEnd = view.findViewById(R.id.btnSignupEnd);

        btnSignUpEnd.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(3);
        });

        return view;

    }
}
