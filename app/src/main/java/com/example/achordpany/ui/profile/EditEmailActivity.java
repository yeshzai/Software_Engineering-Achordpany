package com.example.achordpany.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EditEmailActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextInputLayout changeEmail_NewEmailLayout;
    private TextInputLayout changeEmail_PasswordLayout;
    private boolean isPasswordVisible = false;
    private TextInputEditText changeEmail_NewEmail;
    private TextInputEditText changeEmail_Password;
    private Drawable defaultBackground;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeemail);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        changeEmail_PasswordLayout = findViewById(R.id.changeEmail_PasswordLayout);
        changeEmail_NewEmailLayout = findViewById(R.id.changeEmail_NewEmailLayout);

        changeEmail_NewEmail = findViewById(R.id.changeEmail_NewEmail);
        changeEmail_Password = findViewById(R.id.changeEmail_Password);
        Button changeEmail_ChangeButton = findViewById(R.id.changeEmail_ChangeButton);
        ImageButton changeEmail_btnBack = findViewById(R.id.changeEmail_btnBack);
        defaultBackground = changeEmail_NewEmail.getBackground();

        changeEmail_NewEmail.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changeEmail_Password.setOnTouchListener((v, event) -> {

            return_DefaultBackground();

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
                                ContextCompat.getDrawable(this, R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changeEmail_Password.setTransformationMethod(null);
                        changeEmail_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye), // drawableEnd
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

            if(changeEmail_NewEmail.getText().toString().isEmpty()
                    || changeEmail_Password.getText().toString().isEmpty()) {

                if(changeEmail_NewEmail.getText().toString().isEmpty()) {
                    changeEmail_NewEmailLayout.setError("This field cannot be empty.");
                    //changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeEmail_Password.getText().toString().isEmpty()) {
                    changeEmail_PasswordLayout.setError("This field cannot be empty.");
                    //changeEmail_Password.setBackgroundResource(R.drawable.edittext_error);
                }

                changeEmail_NewEmailLayout.setError("This field cannot be empty.");
                changeEmail_PasswordLayout.setError("This field cannot be empty.");
                Toast.makeText(EditEmailActivity.this, "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();

            } else {

                String newEmail = changeEmail_NewEmail.getText().toString();
                String password = changeEmail_Password.getText().toString();

                checkIfEmailExists(newEmail.toLowerCase(), password);

            }

        });

        changeEmail_btnBack.setOnClickListener(v -> {

            // Just go back to the Profile Page (No Changes)
            finish();

        });

    }

    private void checkIfEmailExists(String newEmail, String password) {

        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        List<String> signInMethods = task.getResult().getSignInMethods();

                        if (signInMethods != null && !signInMethods.isEmpty()) {
                            // Email already exists
                            Toast.makeText(EditEmailActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                            changeEmail_NewEmailLayout.setError("Email already exists.");
                            //changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                        } else {
                            // Email is available
                            updateEmail(newEmail, password);
                        }

                    } else {

                        Exception e = task.getException();
                        Log.e("EMAIL_CHECK", "[FAILED] Cannot find email! ERROR: " + e.getMessage());
                        changeEmail_NewEmailLayout.setError("Invalid email.");
                        //changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                        Toast.makeText(EditEmailActivity.this, "Please enter valid/correct email.", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void updateEmail(String newEmail, String password) {

        if (user == null) {
            Log.d("[EDIT EMAIL]", "User Not Logged In");
            return;
        }

        // Re-authenticate the user
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {

            if (reauthTask.isSuccessful()) {

                Log.d("[RE-AUTHENTICATION]", "[SUCCESS] Correct email and password. Continue change email.");

                // Verify first if user really wants to change email.
                LayoutInflater inflater = getLayoutInflater();
                View popupView = inflater.inflate(R.layout.verifypopup_editemail, null);
                TextView verifypopup_editemail_NewEmailText = popupView.findViewById(R.id.verifypopup_editemail_NewEmailText);
                verifypopup_editemail_NewEmailText.setText(newEmail);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditEmailActivity.this);
                builder.setView(popupView);
                AlertDialog dialog = builder.create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button editEmail_acceptButton = popupView.findViewById(R.id.editemail_popup_AcceptButton);
                editEmail_acceptButton.setOnClickListener(v -> {
                    Log.d("[EDIT EMAIL VERIFICATION]", "[ACCEPT]");
                    dialog.dismiss();

                    user.updateEmail(newEmail).addOnCompleteListener(updateTask -> {

                        if (updateTask.isSuccessful()) {

                            Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
                            main_EverythingLocalDatabase.set_Email(newEmail);

                            Log.d("[EDIT EMAIL - AUTHENTICATION]", "[SUCCESS] Successfully Changed Email!");
                            updateEmailInDatabase(main_EverythingLocalDatabase.get_Username(), newEmail);

                        } else {

                            // Handle errors
                            String errorMessage = updateTask.getException().getMessage();

                            Toast.makeText(EditEmailActivity.this, "Failed to update email: " + errorMessage, Toast.LENGTH_LONG).show();
                            changeEmail_NewEmailLayout.setError("Update failed.");
                            //changeEmail_NewEmail.setBackgroundResource(R.drawable.edittext_error);
                            Log.d("[EDIT EMAIL]", "[FAILED] Email Change Failed!");

                        }
                    });

                });

                Button editEmail_goBackButton = popupView.findViewById(R.id.editemail_popup_RejectButton);
                editEmail_goBackButton.setOnClickListener(v -> {

                    Log.d("[EDIT EMAIL VERIFICATION]", "[REJECT]");
                    dialog.dismiss();
                    finish();

                }); // Exit back to profile page.

            } else {

                Toast.makeText(EditEmailActivity.this, "Re-authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                Log.d("[RE-AUTHENTICATION]", "[FAILED]" + reauthTask.getException().getMessage());
                all_ErrorBackground();

            }
        });

    }

    private void updateEmailInDatabase(String usernameKey, String newEmail) {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference emailRef = databaseRef.child("Users_Credentials").child(usernameKey).child("email");

        emailRef.setValue(newEmail)
                .addOnSuccessListener(aVoid -> {

                    Log.d("[EMAIL User_Credentials UPDATE]", "[SUCCESS] Email on User_Credentials Successfully Changed!");
                    Toast.makeText(EditEmailActivity.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditEmailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                })
                .addOnFailureListener(e -> {
                    Log.d("[EMAIL User_Credentials UPDATE]", "[FAILED] Email on User_Credentials Change Failed!");
                });
    }

    private void return_DefaultBackground() {

        //changeEmail_NewEmail.setBackground(defaultBackground);
        //changeEmail_Password.setBackground(defaultBackground);

        changeEmail_NewEmailLayout.setError(null);
        changeEmail_NewEmailLayout.setErrorEnabled(false);

        changeEmail_PasswordLayout.setError(null);
        changeEmail_PasswordLayout.setErrorEnabled(false);

    }

    private void all_ErrorBackground() {

        changeEmail_PasswordLayout.setError("Re-authentication failed.");
        //changeEmail_Password.setBackgroundResource(R.drawable.edittext_error);

    }

}
