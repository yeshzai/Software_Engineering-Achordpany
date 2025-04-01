package com.example.achordpany.ui.profile;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditEmailActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private boolean isPasswordVisible = false;
    EditText changeEmail_CurrentEmail;
    EditText changeEmail_NewEmail;
    EditText changeEmail_Password;
    private Drawable defaultBackground;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeemail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        changeEmail_CurrentEmail = findViewById(R.id.changeEmail_CurrentEmail);
        changeEmail_NewEmail = findViewById(R.id.changeEmail_NewEmail);
        changeEmail_Password = findViewById(R.id.changeEmail_Password);
        Button changeEmail_ChangeButton = findViewById(R.id.changeEmail_ChangeButton);
        ImageButton changeEmail_btnBack = findViewById(R.id.changeEmail_btnBack);
        defaultBackground = changeEmail_CurrentEmail.getBackground();

        changeEmail_CurrentEmail.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });
        changeEmail_NewEmail.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changeEmail_Password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changeEmail_Password.getWidth();
                int paddingRight = changeEmail_Password.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changeEmail_Password.getRight() - changeEmail_Password.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changeEmail_Password.isFocused()) {
                        changeEmail_Password.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        changeEmail_Password.setTransformationMethod(new PasswordTransformationMethod());
                        changeEmail_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changeEmail_Password.setTransformationMethod(null);
                        changeEmail_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    changeEmail_Password.setSelection(changeEmail_Password.getText().length());
                    return true;
                }
            }
            return false;
        });

        changeEmail_ChangeButton.setOnClickListener(v -> {

            if(changeEmail_CurrentEmail.getText().toString().isEmpty()
                    || changeEmail_NewEmail.getText().toString().isEmpty()
                    || changeEmail_Password.getText().toString().isEmpty()) {

                if(changeEmail_CurrentEmail.getText().toString().isEmpty()) {
                    changeEmail_CurrentEmail.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeEmail_NewEmail.getText().toString().isEmpty()) {
                    changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeEmail_Password.getText().toString().isEmpty()) {
                    changeEmail_Password.setBackgroundResource(R.drawable.edittext_error);
                }

                Toast.makeText(EditEmailActivity.this, "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();

            } else {

                Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
                String currentEmail = main_EverythingLocalDatabase.get_Email();
                String newEmail = changeEmail_NewEmail.getText().toString();
                String password = changeEmail_Password.getText().toString();

                if(changeEmail_CurrentEmail.getText().toString().equals(currentEmail)) {

                    // Proceed to change.
                    updateEmail(newEmail, password);

                } else {

                    // Incorrect Current Email.
                    Toast.makeText(EditEmailActivity.this, "Incorrect Current Email!", Toast.LENGTH_SHORT).show();
                    changeEmail_CurrentEmail.setBackgroundResource(R.drawable.edittext_error);

                }

            }

        });

        changeEmail_btnBack.setOnClickListener(v -> {

            // Just go back to the Profile Page (No Changes)
            finish();

        });

    }

    private void updateEmail(String newEmail, String password) {

        if (user == null) {
            Log.d("[EDIT EMAIL]", "User not Logged In");
            return;
        }

        // Re-authenticate the user
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
            if (reauthTask.isSuccessful()) {

                // Proceed with email update
                Log.d("[RE-AUTHENTICATION]", "[SUCCESS] Continuing to change email.");
                user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {

                        user.sendEmailVerification();   // Optional, send verification email to user.
                        Toast.makeText(EditEmailActivity.this, "Email updated successfully! Please verify your new email.", Toast.LENGTH_SHORT).show();
                        Log.d("[EDIT EMAIL]", "[SUCCESS] Successfully Changed Email!");

                        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
                        main_EverythingLocalDatabase.set_Email(newEmail);

                    } else {

                        // Handle errors
                        String errorMessage = updateTask.getException().getMessage();
                        if (errorMessage != null && errorMessage.contains("already in use")) {
                            Toast.makeText(EditEmailActivity.this, "This email is already in use. Please try another.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditEmailActivity.this, "Failed to update email: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                        changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                        Log.d("[EDIT EMAIL]", "[FAILED] Changed Email Failed!");

                    }
                });
            } else {

                Toast.makeText(EditEmailActivity.this, "Re-authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                Log.d("[RE-AUTHENTICATION]", "[FAILED]" + reauthTask.getException().getMessage());
                all_ErrorBackground();

            }
        });

    }

    private void return_DefaultBackground() {

        changeEmail_CurrentEmail.setBackground(defaultBackground);
        changeEmail_NewEmail.setBackground(defaultBackground);
        changeEmail_Password.setBackground(defaultBackground);

    }

    private void all_ErrorBackground() {

        changeEmail_CurrentEmail.setBackgroundResource(R.drawable.edittext_error);
        changeEmail_Password.setBackgroundResource(R.drawable.edittext_error);

    }

}
