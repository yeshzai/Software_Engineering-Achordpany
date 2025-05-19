package com.example.achordpany.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private TextInputLayout changeUsername_NewUsernameLayout;
    private TextInputLayout changeUsername_PasswordLayout;
    private boolean isPasswordVisible = false;
    private TextInputEditText changeUsername_NewUsername;
    private TextInputEditText changeUsername_Password;
    private Drawable defaultBackground;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeusername);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        changeUsername_NewUsernameLayout = findViewById(R.id.changeUsername_NewUsernameLayout);
        changeUsername_PasswordLayout = findViewById(R.id.changeUsername_PasswordLayout);

        changeUsername_NewUsername = findViewById(R.id.changeUsername_NewUsername);
        changeUsername_Password = findViewById(R.id.changeUsername_Password);
        Button changeUsername_ChangeButton = findViewById(R.id.changeUsername_ChangeButton);
        ImageButton changeUsername_btnBack = findViewById(R.id.changeUsername_btnBack);
        defaultBackground = changeUsername_NewUsername.getBackground();

        changeUsername_NewUsername.setOnTouchListener((v, event) -> {
            return_DefaultBackground();
            return false;
        });

        // PASSWORD HIDE/VISIBLE
        changeUsername_Password.setOnTouchListener((v, event) -> {

            return_DefaultBackground();

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
                                ContextCompat.getDrawable(this, R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        changeUsername_Password.setTransformationMethod(null);
                        changeUsername_Password.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye), // drawableEnd
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

            if(changeUsername_NewUsername.getText().toString().isEmpty() || changeUsername_Password.getText().toString().isEmpty()) {

                if(changeUsername_NewUsername.getText().toString().isEmpty()) {
                    changeUsername_NewUsernameLayout.setError("This field cannot be empty.");
                    //changeUsername_NewUsername.setBackgroundResource(R.drawable.edittext_error);
                }
                if(changeUsername_Password.getText().toString().isEmpty()) {
                    changeUsername_PasswordLayout.setError("This field cannot be empty.");
                    //changeUsername_Password.setBackgroundResource(R.drawable.edittext_error);
                }

                changeUsername_NewUsernameLayout.setError("This field cannot be empty.");
                changeUsername_PasswordLayout.setError("This field cannot be empty.");
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
                            changeUsername_NewUsernameLayout.setError("Username already taken.");
                            //changeUsername_NewUsername.setBackgroundResource(R.drawable.edittext_error);
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
            Log.d("[EDIT USERNAME]", "User Not Logged In");
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
                // Users_Credentials
                database.child("Users_Credentials").child(oldUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            /*  [Users_Credentials]
                                0. Get original oldUsername entry value
                                1. Create new Users_Credentials entry (newUsername)
                                2. Copy oldUsername value as newUsername value
                                3. Delete oldUsername entry
                            */
                            Object userData = snapshot.getValue();  // [Users_Credentials] 0. Get
                            DatabaseReference newUserRef = database.child("Users_Credentials").child(newUsername); // [Users_Credentials] 1. Create
                            newUserRef.setValue(userData).addOnCompleteListener(task -> {   // [Users_Credentials] 2. Copy

                                if (task.isSuccessful()) {

                                    newUserRef.child("username").setValue(newUsername); // [Users_Credentials] Change original username to new username

                                    // Users_RecommendationData & Users_UsernameList
                                    database.child("Users_RecommendationData").child(oldUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot recSnapshot) {
                                            if (recSnapshot.exists()) {

                                                /*  [Users_RecommendationData]
                                                    0. Get original oldUsername entry value
                                                    1. Create new Users_RecommendationData entry (newUsername)
                                                    2. Copy oldUsername value as newUsername value
                                                    3. Delete oldUsername entry
                                                */
                                                Object recData = recSnapshot.getValue(); // [Users_RecommendationData] 0. Get
                                                database.child("Users_RecommendationData").child(newUsername).setValue(recData).addOnCompleteListener(recTask -> {  // [Users_RecommendationData] 1 & 2. Create & Copy
                                                    if (recTask.isSuccessful()) {

                                                        database.child("Users_Credentials").child(oldUsername).removeValue();           // [Users_Credentials]          3. Delete
                                                        database.child("Users_RecommendationData").child(oldUsername).removeValue();    // [Users_RecommendationData]   3. Delete
                                                        database.child("Users_UsernameList").child(oldUsername).removeValue();          // [Users_UsernameList]         3. Delete
                                                        database.child("Users_UsernameList").child(newUsername).setValue("");           // [Users_RecommendationData]   Rename

                                                        Toast.makeText(getApplicationContext(), "Username Changed Successfully!", Toast.LENGTH_SHORT).show();
                                                        Log.d("[EDIT USERNAME]", "[SUCCESS] Username Changed Successfully!");
                                                        main_EverythingLocalDatabase.set_Username(newUsername);

                                                        // We have to go back to MainActivity to refresh everything
                                                        Intent intent = new Intent(EditUsernameActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "There seems to be an error. Please try again later.", Toast.LENGTH_SHORT).show();
                                                        Log.d("[EDIT USERNAME]", "[FAILED] Users_RecommendationData Username Failed!");
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Toast.makeText(getApplicationContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d("[Users_RecommendationData]", "[DATABASE ERROR]" + error.getMessage());
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to Update Username!", Toast.LENGTH_SHORT).show();
                                    Log.d("[EDIT USERNAME]", "[FAILED] Username Change Failed!");
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "There seems to be an error. Please try again later.", Toast.LENGTH_SHORT).show();
                            Log.d("[EDIT USERNAME]", "[WARNING] Cannot Find Current Username in Firebase!");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("[Users_Credentials]", "[DATABASE ERROR]" + error.getMessage());
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

        //changeUsername_NewUsername.setBackground(defaultBackground);
        //changeUsername_Password.setBackground(defaultBackground);

        changeUsername_NewUsernameLayout.setError(null);
        changeUsername_NewUsernameLayout.setErrorEnabled(false);

        changeUsername_PasswordLayout.setError(null);
        changeUsername_PasswordLayout.setErrorEnabled(false);
    }

    private void all_ErrorBackground() {
        changeUsername_PasswordLayout.setError("Re-authentication failed.");
        //changeUsername_Password.setBackgroundResource(R.drawable.edittext_error);

    }

}
