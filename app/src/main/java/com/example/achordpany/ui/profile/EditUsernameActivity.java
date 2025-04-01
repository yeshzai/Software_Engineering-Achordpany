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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUsernameActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    private boolean isPasswordVisible = false;
    EditText changeUsername_CurrentUsername;
    EditText changeUsername_NewUsername;
    EditText changeUsername_Password;
    private Drawable defaultBackground;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeusername);

        changeUsername_CurrentUsername = findViewById(R.id.changeUsername_CurrentUsername);
        changeUsername_NewUsername = findViewById(R.id.changeUsername_NewUsername);
        changeUsername_Password = findViewById(R.id.changeUsername_Password);
        Button changeUsername_ChangeButton = findViewById(R.id.changeUsername_ChangeButton);
        ImageButton changeUsername_btnBack = findViewById(R.id.changeUsername_btnBack);
        defaultBackground = changeUsername_CurrentUsername.getBackground();

        changeUsername_CurrentUsername.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });
        changeUsername_NewUsername.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });


        // PASSWORD HIDE/VISIBLE
        changeUsername_Password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = changeUsername_Password.getWidth();
                int paddingRight = changeUsername_Password.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (changeUsername_Password.getRight() - changeUsername_Password.getCompoundDrawables()[2].getBounds().width())) {

                    if(!changeUsername_Password.isFocused()) {
                        changeUsername_Password.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        changeUsername_Password.setTransformationMethod(new PasswordTransformationMethod());
                        changeUsername_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changeUsername_Password.setTransformationMethod(null);
                        changeUsername_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    changeUsername_Password.setSelection(changeUsername_Password.getText().length());
                    return true;
                }
            }
            return false;
        });

        changeUsername_ChangeButton.setOnClickListener(v -> {

            if(changeUsername_CurrentUsername.getText().toString().isEmpty()
                    || changeUsername_NewUsername.getText().toString().isEmpty()
                    || changeUsername_Password.getText().toString().isEmpty()) {

                if(changeUsername_CurrentUsername.getText().toString().isEmpty()) {
                    changeUsername_CurrentUsername.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeUsername_CurrentUsername.getText().toString().isEmpty()) {
                    changeUsername_CurrentUsername.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeUsername_Password.getText().toString().isEmpty()) {
                    changeUsername_Password.setBackgroundResource(R.drawable.edittext_error);
                }

                Toast.makeText(EditUsernameActivity.this, "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show();

            } else {

                // Check first if username is already present on the database (used by other user)
                DatabaseReference users_usernamelist = FirebaseDatabase.getInstance().getReference("Users_UsernameList");
                String newUsername = changeUsername_NewUsername.getText().toString();
                String password = changeUsername_Password.getText().toString();

                users_usernamelist.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {    // Username already exists.
                            Toast.makeText(getApplicationContext(), "Username already taken! Choose another.", Toast.LENGTH_LONG).show();
                            changeUsername_NewUsername.setBackgroundResource(R.drawable.edittext_error);
                        } else {                    // Username available, proceed to account authentication.
                            updateUsername(newUsername, password);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.d("[DATABASE ERROR]", error.getMessage());

                    }
                });

            }

        });

        changeUsername_btnBack.setOnClickListener(v -> {

            // Just go back to the Profile Page (No Changes)
            finish();

        });

    }

    private void updateUsername(String newUsername, String password) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        String oldUsername = main_EverythingLocalDatabase.get_Username();

        if (user == null) {
            Log.d("[EDIT EMAIL]", "User not Logged In");
            return;
        }

        // Re-authenticate the user
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
            if (reauthTask.isSuccessful()) {

                // Proceed with username update
                Log.d("[RE-AUTHENTICATION]", "[SUCCESS] Continuing to change username.");
                main_EverythingLocalDatabase.set_Username(newUsername);

                // We can now change/update new username
                database.child("Users_Credentials").child(oldUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            // 1. Get user's data from original user (oldUsername).
                            // 2. Create new user (newUsername).
                            // 2. Pass all original data to new user (Users_Credentials) then change "username" value to newUsername.
                            Object userData = snapshot.getValue();
                            DatabaseReference newUserRef = database.child("Users_Credentials").child(newUsername);
                            newUserRef.setValue(userData).addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {

                                    // Users_Credentials
                                    newUserRef.child("username").setValue(newUsername);                     // Set same value to new
                                    database.child("Users_Credentials").child(oldUsername).removeValue();   // Remove old

                                    // Users_UsernameList
                                    database.child("Users_UsernameList").child(oldUsername).removeValue();  // Remove old
                                    database.child("Users_UsernameList").child(newUsername).setValue("");   // Add new

                                    Toast.makeText(getApplicationContext(), "Username changed successfully!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to update username!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            Toast.makeText(getApplicationContext(), "Old username not found!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.d("[DATABASE ERROR]", error.getMessage());
                    }
                });

            } else {

                Toast.makeText(EditUsernameActivity.this, "Re-authentication failed. Please try again.", Toast.LENGTH_LONG).show();
                Log.d("[RE-AUTHENTICATION]", "[FAILED]" + reauthTask.getException().getMessage());
                all_ErrorBackground();

            }
        });
    }

    private void return_DefaultBackground() {

        changeUsername_CurrentUsername.setBackground(defaultBackground);
        changeUsername_NewUsername.setBackground(defaultBackground);
        changeUsername_Password.setBackground(defaultBackground);

    }

    private void all_ErrorBackground() {

        changeUsername_CurrentUsername.setBackgroundResource(R.drawable.edittext_error);
        changeUsername_Password.setBackgroundResource(R.drawable.edittext_error);

    }

}
