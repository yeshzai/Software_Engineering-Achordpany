package com.example.achordpany.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    FirebaseUser user;
    private Drawable defaultBackground;
    private boolean isPasswordVisible_Current = false;
    private boolean isPasswordVisible_New = false;
    private boolean isPasswordVisible_Confirm = false;
    private TextInputLayout changePassword_CurrentPasswordLayout;
    private TextInputLayout changePassword_NewPasswordLayout;
    private TextInputLayout changePassword_ConfirmPasswordLayout;
    private TextInputEditText changePassword_CurrentPassword;
    private TextInputEditText changePassword_NewPassword;
    private TextInputEditText changePassword_ConfirmPassword;
    Button changePassword_Button;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_changepass, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        changePassword_CurrentPasswordLayout = view.findViewById(R.id.changePassword_CurrentPasswordLayout);
        changePassword_NewPasswordLayout = view.findViewById(R.id.changePassword_NewPasswordLayout);
        changePassword_ConfirmPasswordLayout = view.findViewById(R.id.changePassword_ConfirmPasswordLayout);

        changePassword_CurrentPassword = view.findViewById(R.id.changePassword_CurrentPassword);
        changePassword_NewPassword = view.findViewById(R.id.changePassword_NewPassword);
        changePassword_ConfirmPassword = view.findViewById(R.id.changePassword_ConfirmPassword);
        changePassword_Button = view.findViewById(R.id.changePassword_Button);
        defaultBackground = changePassword_CurrentPassword.getBackground();

        // PASSWORD HIDE/VISIBLE
        changePassword_CurrentPassword.setOnTouchListener((v, event) -> {

            changePassword_CurrentPasswordLayout.setError(null);
            changePassword_CurrentPasswordLayout.setErrorEnabled(false);
            //changePassword_CurrentPassword.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changePassword_CurrentPassword.getWidth();
                int paddingRight = changePassword_CurrentPassword.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changePassword_CurrentPassword.getRight() - changePassword_CurrentPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changePassword_CurrentPassword.isFocused()) {
                        changePassword_CurrentPassword.requestFocus();
                    }

                    if (isPasswordVisible_Current) {    // HIDE
                        changePassword_CurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
                        changePassword_CurrentPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changePassword_CurrentPassword.setTransformationMethod(null);
                        changePassword_CurrentPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible_Current = !isPasswordVisible_Current;
                    changePassword_CurrentPassword.setSelection(changePassword_CurrentPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changePassword_NewPassword.setOnTouchListener((v, event) -> {

            changePassword_NewPasswordLayout.setError(null);
            changePassword_NewPasswordLayout.setErrorEnabled(false);
            //changePassword_NewPassword.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changePassword_NewPassword.getWidth();
                int paddingRight = changePassword_NewPassword.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changePassword_NewPassword.getRight() - changePassword_NewPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changePassword_NewPassword.isFocused()) {
                        changePassword_NewPassword.requestFocus();
                    }

                    if (isPasswordVisible_New) {    // HIDE
                        changePassword_NewPassword.setTransformationMethod(new PasswordTransformationMethod());
                        changePassword_NewPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changePassword_NewPassword.setTransformationMethod(null);
                        changePassword_NewPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible_New = !isPasswordVisible_New;
                    changePassword_NewPassword.setSelection(changePassword_NewPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changePassword_ConfirmPassword.setOnTouchListener((v, event) -> {

            changePassword_ConfirmPasswordLayout.setError(null);
            changePassword_ConfirmPasswordLayout.setErrorEnabled(false);
            //changePassword_ConfirmPassword.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changePassword_ConfirmPassword.getWidth();
                int paddingRight = changePassword_ConfirmPassword.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changePassword_ConfirmPassword.getRight() - changePassword_ConfirmPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changePassword_ConfirmPassword.isFocused()) {
                        changePassword_ConfirmPassword.requestFocus();
                    }

                    if (isPasswordVisible_Confirm) {    // HIDE
                        changePassword_ConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                        changePassword_ConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changePassword_ConfirmPassword.setTransformationMethod(null);
                        changePassword_ConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_eye), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible_Confirm = !isPasswordVisible_Confirm;
                    changePassword_ConfirmPassword.setSelection(changePassword_ConfirmPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        changePassword_Button.setOnClickListener(v -> {

            if(     changePassword_CurrentPassword.getText().toString().isEmpty()   ||
                    changePassword_NewPassword.getText().toString().isEmpty()       ||
                    changePassword_ConfirmPassword.getText().toString().isEmpty()
            ) {

                Toast.makeText(requireActivity(), "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();

                if(changePassword_CurrentPassword.getText().toString().isEmpty())
                    changePassword_CurrentPasswordLayout.setError("This field cannot be empty.");
                    //changePassword_CurrentPassword.setBackgroundResource(R.drawable.edittext_error);

                if(changePassword_NewPassword.getText().toString().isEmpty())
                    changePassword_NewPasswordLayout.setError("This field cannot be empty.");
                    //changePassword_NewPassword.setBackgroundResource(R.drawable.edittext_error);

                if(changePassword_ConfirmPassword.getText().toString().isEmpty())
                    changePassword_ConfirmPasswordLayout.setError("This field cannot be empty.");
                    //changePassword_ConfirmPassword.setBackgroundResource(R.drawable.edittext_error);

            } else {

                restoreAll_defaultBackground();
                String check_password = changePassword_CurrentPassword.getText().toString();

                if(changePassword_NewPassword.getText().toString().equals(changePassword_ConfirmPassword.getText().toString())) {

                    // Check first if current password is correct through User Authentication.
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), check_password);
                    user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {

                        if (reauthTask.isSuccessful()) {

                            // User Authentication successful, continue changing password.
                            changeUserPassword(changePassword_NewPassword.getText().toString());

                        } else {

                        Toast.makeText(requireActivity(), "Re-authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                        Log.d("[RE-AUTHENTICATION]", "[FAILED]" + reauthTask.getException().getMessage());
                        changePassword_CurrentPasswordLayout.setError("Re-authentication failed.");
                        //changePassword_CurrentPassword.setBackgroundResource(R.drawable.edittext_error);

                        }
                    });

                } else {

                    changePassword_NewPasswordLayout.setError("Passwords do not match.");
                    changePassword_ConfirmPasswordLayout.setError("Passwords do not match.");
                    Toast.makeText(requireActivity(), "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();
                    //changePassword_NewPassword.setBackgroundResource(R.drawable.edittext_error);
                    //changePassword_ConfirmPassword.setBackgroundResource(R.drawable.edittext_error);

                }
            }

        });

        return view;

    }

    private void changeUserPassword(String new_password) {

        if (user != null) {
            user.updatePassword(new_password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                            Log.d("[CHANGE PASSWORD]", "[SUCCESS] Changed Successfully!");

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getActivity(), "Password Failed to Change!", Toast.LENGTH_SHORT).show();
                            Log.d("[CHANGE PASSWORD]", "[FAILED] Change Failed!");
                        }

                    });
        } else {
            Log.d("[CHANGE PASSWORD]", "[NaN] No Account Found!");
        }

    }

    private void restoreAll_defaultBackground() {

        //changePassword_CurrentPassword.setBackground(defaultBackground);
        //changePassword_NewPassword.setBackground(defaultBackground);
        //changePassword_ConfirmPassword.setBackground(defaultBackground);

        changePassword_CurrentPasswordLayout.setError(null);
        changePassword_CurrentPasswordLayout.setErrorEnabled(false);

        changePassword_NewPasswordLayout.setError(null);
        changePassword_NewPasswordLayout.setErrorEnabled(false);

        changePassword_ConfirmPasswordLayout.setError(null);
        changePassword_ConfirmPasswordLayout.setErrorEnabled(false);
    }

}
