package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.LoginActivity;
import com.example.achordpany.ui.auth.WelcomeActivity;

import java.util.ArrayList;

public class SignUpStep4Fragment extends Fragment {

    private ImageView profileImage;
    private TextView profileName;
    private TextView passwordValue;
    private TextView genresValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_4, container, false);
        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {
            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        passwordValue = view.findViewById(R.id.passwordValue);
        genresValue = view.findViewById(R.id.genresValue);
        CheckBox checkBox_Terms = view.findViewById(R.id.checkBox_Terms);

        Log.d("SignUpCredentials Username", signUpCredentials.get_credential_usernameText());
        Log.d("SignUpCredentials Password", signUpCredentials.get_credential_passwordText());
        Log.d("SignUpCredentials Genre", signUpCredentials.get_credential_genre().toString());

        profileName.setText(signUpCredentials.get_credential_usernameText());
        passwordValue.setText(signUpCredentials.get_credential_passwordText());
        genresValue.setText(signUpCredentials.get_credential_genre().toString());

        Button btnSignUpEnd = view.findViewById(R.id.btnSignupEnd);
        btnSignUpEnd.setOnClickListener(v -> {

            if(checkBox_Terms.isChecked()) {

                saveToFirebaseDatabase();   // New User saved to Firebase (Authentication) Database
                resetSignUpCredentials();   // Ready for next new Sign-Up

                Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
                startActivity(intent);
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), "Please accept the Terms and Conditions!", Toast.LENGTH_SHORT).show();
            }

        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(3);
        });

        return view;

    }

    private void saveToFirebaseDatabase() {



    }

    private void resetSignUpCredentials() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        signUpCredentials.set_credential_usernameText("");
        signUpCredentials.set_credential_emailAddressText("");
        signUpCredentials.set_credential_passwordText("");
        signUpCredentials.set_credential_confirmPasswordText("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

}
