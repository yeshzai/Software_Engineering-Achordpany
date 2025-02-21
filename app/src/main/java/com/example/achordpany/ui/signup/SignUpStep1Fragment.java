package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.WelcomeActivity;

public class SignUpStep1Fragment extends Fragment {

    public TextView usernameText;
    public TextView emailAddressText;
    public TextView passwordText;
    public TextView confirmPasswordText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_1, container, false);

        usernameText = view.findViewById(R.id.usernameText);
        emailAddressText = view.findViewById(R.id.emailAddressText);
        passwordText = view.findViewById(R.id.passwordText);
        confirmPasswordText = view.findViewById(R.id.confirmPasswordText);

        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            // CODE HERE - Store credentials in SignUpCredentials class.
            SignUpCredentials signUpCredentials = new SignUpCredentials();

            String user_usernameText = usernameText.getText().toString();
            String user_emailAddressText = emailAddressText.getText().toString();
            String user_passwordText = passwordText.getText().toString();
            String user_confirmPasswordText = confirmPasswordText.getText().toString();

            if(!user_usernameText.isEmpty() && !user_emailAddressText.isEmpty()
                    && !user_passwordText.isEmpty() && !user_confirmPasswordText.isEmpty())
            {
                if (user_passwordText.equals(user_confirmPasswordText)) {

                    signUpCredentials.set_credential_usernameText(user_usernameText);
                    signUpCredentials.set_credential_emailAddressText(user_emailAddressText);
                    signUpCredentials.set_credential_passwordText(user_passwordText);
                    signUpCredentials.set_credential_confirmPasswordText(user_confirmPasswordText);

                    // Testing purposes - Logcat
                    Log.d("SignUpCredentials", "Username: " + signUpCredentials.get_credential_usernameText());
                    Log.d("SignUpCredentials", "Email Address: " + signUpCredentials.get_credential_emailAddressText());
                    Log.d("SignUpCredentials", "Password: " + signUpCredentials.get_credential_passwordText());
                    Log.d("SignUpCredentials", "Confirm Password: " + signUpCredentials.get_credential_confirmPasswordText());

                    ((SignUpActivity) requireActivity()).navigateToStep(2);

                } else {

                    Toast.makeText(requireActivity(), "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();

                }
            } else {

                Toast.makeText(requireActivity(), "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();

            }

        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;

    }
}
