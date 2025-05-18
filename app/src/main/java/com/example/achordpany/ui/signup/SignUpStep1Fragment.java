package com.example.achordpany.ui.signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpStep1Fragment extends Fragment {

    private Drawable defaultBackground;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    public TextInputEditText usernameText;
    public TextInputEditText emailAddressText;
    public TextInputEditText passwordText;
    public TextInputEditText confirmPasswordText;
    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmpasswordLayout;

    @SuppressLint("ClickableViewAccessibility")

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_1, container, false);

        usernameText = view.findViewById(R.id.usernameText);
        emailAddressText = view.findViewById(R.id.emailAddressText);
        passwordText = view.findViewById(R.id.passwordText);
        confirmPasswordText = view.findViewById(R.id.confirmPasswordText);

        usernameLayout = view.findViewById(R.id.usernameLayout);
        emailLayout = view.findViewById(R.id.emailLayout);
        passwordLayout = view.findViewById(R.id.passwordLayout);
        confirmpasswordLayout = view.findViewById(R.id.confirmpasswordLayout);

        defaultBackground = usernameText.getBackground();
        backtrackContent();

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {

            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();

        });

        passwordText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updatePasswordIcons(passwordText, isPasswordVisible);
            }
        });
        confirmPasswordText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                updatePasswordIcons(confirmPasswordText, isConfirmPasswordVisible);
            }
        });


        // PASSWORD HIDE/VISIBLE
        passwordText.setOnTouchListener((v, event) -> {

            return_AllDefaultBackground();

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = passwordText.getWidth();
                int paddingRight = passwordText.getPaddingRight();
                float touchX = event.getX();

                //if (touchX > width - paddingRight - passwordText.getCompoundDrawables()[2].getBounds().width()) {
                Drawable endDrawable = passwordText.getCompoundDrawables()[2];
                if (endDrawable != null && touchX > width - paddingRight - endDrawable.getBounds().width()) {

                    if(!passwordText.isFocused()) {
                        passwordText.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        passwordText.setTransformationMethod(new PasswordTransformationMethod());
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        passwordText.setTransformationMethod(null);
                        passwordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye), // drawableEnd
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

            return_AllDefaultBackground();

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = confirmPasswordText.getWidth();
                int paddingRight = confirmPasswordText.getPaddingRight();
                float touchX = event.getX();

                //if (touchX > width - paddingRight - confirmPasswordText.getCompoundDrawables()[2].getBounds().width()) {
                Drawable endDrawable = confirmPasswordText.getCompoundDrawables()[2];
                if (endDrawable != null && touchX > width - paddingRight - endDrawable.getBounds().width()) {

                    if(!confirmPasswordText.isFocused()) {
                        confirmPasswordText.requestFocus();
                    }

                    if (isConfirmPasswordVisible) { // HIDE
                        confirmPasswordText.setTransformationMethod(new PasswordTransformationMethod());
                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        confirmPasswordText.setTransformationMethod(null);
                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye), // drawableEnd
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

        usernameText.setOnTouchListener((v, event) -> {
            return_AllDefaultBackground();
            return false;
        });

        emailAddressText.setOnTouchListener((v, event) -> {
            return_AllDefaultBackground();
            return false;
        });

        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {

            return_AllDefaultBackground();
            SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();

            String user_usernameText = usernameText.getText().toString();
            String user_emailAddressText = emailAddressText.getText().toString();
            String user_passwordText = passwordText.getText().toString();
            String user_confirmPasswordText = confirmPasswordText.getText().toString();

            if(user_usernameText.isEmpty()) {

                Toast.makeText(requireActivity(), "Please enter a username.", Toast.LENGTH_SHORT).show();
                //usernameText.setBackgroundResource(R.drawable.edittext_error);
                usernameLayout.setError("This field cannot be empty.");

            } else {

                if(username_valid(user_usernameText)) {

                    // Check if username already exists.
                    DatabaseReference users_usernamelist = FirebaseDatabase.getInstance().getReference("Users_UsernameList");
                    users_usernamelist.child(user_usernameText).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {    // Username already exists.

                                Toast.makeText(requireActivity(), "Username already taken! Please choose another.", Toast.LENGTH_LONG).show();
                                //usernameText.setBackgroundResource(R.drawable.edittext_error);
                                usernameLayout.setError("Username already taken");

                            } else {                    // Username available, proceed.

                                if(user_emailAddressText.isEmpty()) {

                                    Toast.makeText(requireActivity(), "Please enter an email address.", Toast.LENGTH_SHORT).show();
                                    //emailAddressText.setBackgroundResource(R.drawable.edittext_error);
                                    emailLayout.setError("This field cannot be empty.");

                                } else {

                                    // Check if email already exists.
                                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(user_emailAddressText)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {

                                                    List<String> signInMethods = task.getResult().getSignInMethods();

                                                    if (signInMethods != null && !signInMethods.isEmpty()) {

                                                        // Email already exists
                                                        Toast.makeText(requireActivity(), "Email already exists. Please choose another.", Toast.LENGTH_SHORT).show();
                                                        //emailAddressText.setBackgroundResource(R.drawable.edittext_error);
                                                        emailLayout.setError("Email already exists.");

                                                    } else {

                                                        // Email is available
                                                        if(user_passwordText.isEmpty() || user_confirmPasswordText.isEmpty()) {

                                                            if(user_passwordText.isEmpty()) {
                                                                //passwordText.setBackgroundResource(R.drawable.edittext_error);
                                                                passwordLayout.setError("This field cannot be empty.");

                                                                // Re-apply password icon
                                                                /*passwordText.post(() -> {
                                                                    Drawable lockIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock);
                                                                    Drawable eyeIcon = ContextCompat.getDrawable(requireContext(),
                                                                            isPasswordVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);

                                                                    if (lockIcon != null && eyeIcon != null) {
                                                                        passwordText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, eyeIcon, null);
                                                                    } else {
                                                                        Log.e("DrawableError", "Missing drawable resource: lockIcon or eyeIcon is null");
                                                                    }
                                                                });*/
                                                                updatePasswordIcons(passwordText, isPasswordVisible);
                                                            }

                                                            if(user_confirmPasswordText.isEmpty()) {
                                                                //confirmPasswordText.setBackgroundResource(R.drawable.edittext_error);
                                                                confirmpasswordLayout.setError("This field cannot be empty.");

                                                                // Re-apply confirm password icon
                                                                /*confirmPasswordText.post(() -> {
                                                                    Drawable confirmLockIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock);
                                                                    Drawable confirmEyeIcon = ContextCompat.getDrawable(requireContext(),
                                                                            isConfirmPasswordVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);

                                                                    if (confirmLockIcon != null && confirmEyeIcon != null) {
                                                                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(confirmLockIcon, null, confirmEyeIcon, null);
                                                                    } else {
                                                                        Log.e("DrawableError", "Missing drawable resource: confirmLockIcon or confirmEyeIcon is null");
                                                                    }
                                                                });*/
                                                                updatePasswordIcons(confirmPasswordText, isConfirmPasswordVisible);
                                                            }

                                                            Toast.makeText(requireActivity(), "Please fill in all the blanks.", Toast.LENGTH_SHORT).show();

                                                        } else {

                                                            if (user_passwordText.equals(user_confirmPasswordText)) {

                                                                passwordText.setBackground(defaultBackground);
                                                                confirmPasswordText.setBackground(defaultBackground);

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
                                                                //passwordText.setBackgroundResource(R.drawable.edittext_error);
                                                                //confirmPasswordText.setBackgroundResource(R.drawable.edittext_error);
                                                                passwordLayout.setError("Password do not match.");

                                                                // Re-apply password icon
                                                                /*passwordText.post(() -> {
                                                                    Drawable lockIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock);
                                                                    Drawable eyeIcon = ContextCompat.getDrawable(requireContext(),
                                                                            isPasswordVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);

                                                                    if (lockIcon != null && eyeIcon != null) {
                                                                        passwordText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, eyeIcon, null);
                                                                    } else {
                                                                        Log.e("DrawableError", "Missing drawable resource: lockIcon or eyeIcon is null");
                                                                    }
                                                                });*/
                                                                updatePasswordIcons(passwordText, isPasswordVisible);


                                                                confirmpasswordLayout.setError("Password do not match.");

                                                                // Re-apply confirm password icon
                                                                /*confirmPasswordText.post(() -> {
                                                                    Drawable confirmLockIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock);
                                                                    Drawable confirmEyeIcon = ContextCompat.getDrawable(requireContext(),
                                                                            isConfirmPasswordVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);

                                                                    if (confirmLockIcon != null && confirmEyeIcon != null) {
                                                                        confirmPasswordText.setCompoundDrawablesWithIntrinsicBounds(confirmLockIcon, null, confirmEyeIcon, null);
                                                                    } else {
                                                                        Log.e("DrawableError", "Missing drawable resource: confirmLockIcon or confirmEyeIcon is null");
                                                                    }
                                                                });*/
                                                                updatePasswordIcons(confirmPasswordText, isConfirmPasswordVisible);

                                                                Toast.makeText(requireActivity(), "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }

                                                    }

                                                } else {

                                                    Exception e = task.getException();
                                                    Log.e("EMAIL_CHECK", "[FAILED] Cannot find email! ERROR: " + e.getMessage());
                                                    //emailAddressText.setBackgroundResource(R.drawable.edittext_error);
                                                    Toast.makeText(requireActivity(), "Please enter valid/correct email.", Toast.LENGTH_SHORT).show();
                                                    emailLayout.setError("Invalid email address.");
                                                }
                                            });

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Log.d("[DATABASE ERROR]", error.getMessage());

                        }
                    });

                } else {

                    Toast.makeText(requireActivity(), "Username invalid! Contains special characters.", Toast.LENGTH_SHORT).show();
                    //usernameText.setBackgroundResource(R.drawable.edittext_error);
                    usernameLayout.setError("Invalid username.");
                }

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

    private void updatePasswordIcons(EditText editText, boolean isVisible) {
        Drawable lockIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock);
        Drawable eyeIcon = ContextCompat.getDrawable(requireContext(),
                isVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);
        editText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, eyeIcon, null);

        TextInputLayout layout = (TextInputLayout) editText.getParent().getParent();

        // Set the start icon (lock icon)
        layout.setStartIconDrawable(R.drawable.ic_lock);
        //layout.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));

        // Tell TextInputLayout to use a custom end icon
        layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);

        // Set the end icon (eye icon)
        layout.setEndIconDrawable(isVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);

        // Set the end icon (eye icon)
        /*if (isVisible) {
            layout.setEndIconDrawable(R.drawable.ic_eye);
        } else {
            layout.setEndIconDrawable(R.drawable.ic_eyehide);
        }*/

        //layout.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));
    }


    private void return_AllDefaultBackground() {

        usernameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmpasswordLayout.setError(null);

        //usernameText.setBackground(defaultBackground);
        //emailAddressText.setBackground(defaultBackground);
        //passwordText.setBackground(defaultBackground);
        //confirmPasswordText.setBackground(defaultBackground);

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
        signUpCredentials.set_credential_avatarUID("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

    private boolean username_valid(String check_username) {

        String[] forbidden_keys = {".", "$", "#", "[", "]", "/"};

        for(String i : forbidden_keys) {
            if(check_username.contains(i)) {
                return false;
            }
        }

        return true;

    }

}