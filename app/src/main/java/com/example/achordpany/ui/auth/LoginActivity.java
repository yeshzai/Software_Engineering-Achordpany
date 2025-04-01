package com.example.achordpany.ui.auth;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.achordpany.ChordsRecommendations;
import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

interface UsernameCallBack {
    void onUsernameReceived(String username);
}

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private boolean isPasswordVisible = false;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textForgotPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ensure this matches your Login layout file

        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textForgotPassword = findViewById(R.id.textForgotPassword);

        // If user clicks forgot password, navigate to RecoverAccountActivity
        textForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoverAccountActivity.class);
            startActivity(intent);
            finish();
        });

        // PASSWORD HIDE/VISIBLE
        editTextPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = editTextPassword.getWidth();
                int paddingRight = editTextPassword.getPaddingRight();
                float touchX = event.getX();

                if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[2].getBounds().width())) {

                    if(!editTextPassword.isFocused()) {
                        editTextPassword.requestFocus();
                    }

                    if (isPasswordVisible) {    // HIDE
                        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eyehide_black), // drawableEnd
                                null  // drawableBottom
                        );
                    } else {    // SHOW
                        editTextPassword.setTransformationMethod(null);
                        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(
                                ContextCompat.getDrawable(this, R.drawable.ic_lock_black), // drawableStart
                                null, // drawableTop
                                ContextCompat.getDrawable(this, R.drawable.ic_eye_black), // drawableEnd
                                null  // drawableBottom
                        );
                    }

                    isPasswordVisible = !isPasswordVisible;
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    return true;
                }
            }
            return false;
        });

        // Handle Back Button Click - Navigate to WelcomeActivity
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
            startActivity(intent);
        });

        // If login is successful, navigate to MainActivity (which hosts HomeFragment)
        findViewById(R.id.buttonLogin).setOnClickListener(v -> {

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if(!email.isEmpty() && !password.isEmpty()) {

                // Add restrictions for email (gmail.com, bicol-u.edu.ph, yahoo.com, etc).

                // LogIn using Firebase Database
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Log.d("Login", "[SUCCESS] Login Successful!");

                        // Load Firebase to Local Database
                        findUsernameByEmail(email, new UsernameCallBack() {
                            @Override
                            public void onUsernameReceived(String username) {

                                if(username != null) {

                                    Log.d("USERNAME SEARCH", "[SUCCESS] Username: " + username);

                                    Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance(LoginActivity.this);
                                    main_EverythingLocalDatabase.mainPage_RetrieveFirebase(username);

                                } else {

                                    Log.d("USERNAME SEARCH", "[FAILED] Username Not Found!");

                                }

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                        Log.d("Login", "[FAILED] Login Failed!");

                    }
                });

            } else {

                Toast.makeText(LoginActivity.this, "Please Fill In All Fields!", Toast.LENGTH_SHORT).show();

            }

        });

        // If user clicks signup, navigate to MainActivity and open SignupFragment1
        findViewById(R.id.textNoAccount).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        // Find the TextView
        TextView textNoAccount = findViewById(R.id.textNoAccount);

        // Create a SpannableString to change color for "Sign Up"
        SpannableString spannable = new SpannableString("Don't have an account? Sign Up");

        // Find "Sign Up" position
        int start = spannable.toString().indexOf("Sign Up");
        int end = start + "Sign Up".length();

        // Apply the color change (e.g., Blue) to "Sign Up"
        spannable.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the styled text to the TextView
        textNoAccount.setText(spannable);
    }

    // Getting Username by Email Logged In
    private void findUsernameByEmail(String the_email, UsernameCallBack callBack) {

        DatabaseReference userCredentials = FirebaseDatabase.getInstance().getReference("Users_Credentials");

        userCredentials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snap_shot : snapshot.getChildren()) {

                    String emailFromDB = snap_shot.child("email").getValue(String.class);
                    if(emailFromDB != null && emailFromDB.equals(the_email)) {

                        String return_username = snap_shot.child("username").getValue(String.class);
                        callBack.onUsernameReceived(return_username);
                        return; // Stop searching once found.

                    }

                }
                callBack.onUsernameReceived(null);  // No email is found.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                callBack.onUsernameReceived(null);

            }
        });

    }

}

