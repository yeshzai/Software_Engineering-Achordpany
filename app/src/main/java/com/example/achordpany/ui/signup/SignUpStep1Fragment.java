package com.example.achordpany.ui.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.text.method.PasswordTransformationMethod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.LoginActivity;
import com.example.achordpany.ui.auth.WelcomeActivity;

import java.util.ArrayList;

public class SignUpStep1Fragment extends Fragment {

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    public TextView usernameText;
    public TextView emailAddressText;
    public EditText passwordText;
    public EditText confirmPasswordText;

    @SuppressLint("ClickableViewAccessibility")

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_1, container, false);

        usernameText = view.findViewById(R.id.usernameText);
        emailAddressText = view.findViewById(R.id.emailAddressText);
        passwordText = view.findViewById(R.id.passwordText);
        confirmPasswordText = view.findViewById(R.id.confirmPasswordText);
        backtrackContent();

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {

            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();

        });


        // PASSWORD HIDE/VISIBLE
        passwordText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = passwordText.getWidth();
                int paddingRight = passwordText.getPaddingRight();
                float touchX = event.getX();

                if (touchX > width - paddingRight - passwordText.getCompoundDrawables()[2].getBounds().width()) {

                    if(!passwordText.isFocused()) {
                        passwordText.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        passwordText.setTransformationMethod(new PasswordTransformationMethod());
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        passwordText.setTransformationMethod(null);
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    passwordText.setSelection(passwordText.getText().length());
                    return true;
                }
            }
            return false;
        });

        // CONFIRM PASSWORD HIDE/VISIBLE
        confirmPasswordText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = confirmPasswordText.getWidth();
                int paddingRight = confirmPasswordText.getPaddingRight();
                float touchX = event.getX();

                if (touchX > width - paddingRight - confirmPasswordText.getCompoundDrawables()[2].getBounds().width()) {

                    if(!confirmPasswordText.isFocused()) {
                        confirmPasswordText.requestFocus();
                    }

                    if (isConfirmPasswordVisible) { // HIDE
                        confirmPasswordText.setTransformationMethod(new PasswordTransformationMethod());
                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        confirmPasswordText.setTransformationMethod(null);
                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isConfirmPasswordVisible = !isConfirmPasswordVisible;
                    confirmPasswordText.setSelection(confirmPasswordText.getText().length());
                    return true;
                }
            }
            return false;
        });


        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {

            SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();

            String user_usernameText = usernameText.getText().toString();
            String user_emailAddressText = emailAddressText.getText().toString();
            String user_passwordText = passwordText.getText().toString();
            String user_confirmPasswordText = confirmPasswordText.getText().toString();

            if(username_valid(user_usernameText)) { // [KEY - CHILD_NAME] Username Does Not Contain Invalid Character(s)

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

            } else {  // [KEY - CHILD_NAME] Username Contain Invalid Character(s)

                Toast.makeText(requireActivity(), "Username Invalid! Remove [., $, #, [, ], /]", Toast.LENGTH_SHORT).show();

            }

        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {

            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish();

        });

        return view;

    }

    private void backtrackContent() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        String backtrack_username = signUpCredentials.get_credential_usernameText();
        String backtrack_emailAddress = signUpCredentials.get_credential_emailAddressText();
        String backtrack_password = signUpCredentials.get_credential_passwordText();
        String backtrack_confirmPassword = signUpCredentials.get_credential_confirmPasswordText();

        usernameText.setText(backtrack_username);
        emailAddressText.setText(backtrack_emailAddress);
        passwordText.setText(backtrack_password);
        confirmPasswordText.setText(backtrack_confirmPassword);

    }

    private void resetSignUpCredentials() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        signUpCredentials.set_credential_usernameText("");
        signUpCredentials.set_credential_emailAddressText("");
        signUpCredentials.set_credential_passwordText("");
        signUpCredentials.set_credential_confirmPasswordText("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

    private boolean username_valid(String check_username) {

        String[] forbidden_keys = {".", "$", "#", "[", "]", "/"};
        boolean the_result = false;

        for(String i : forbidden_keys) {
            if(check_username.contains(i)) {
                the_result = true;
                break;
            }
        }

        return the_result;

    }

}