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

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private boolean isPasswordVisible = false;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ensure this matches your Login layout file

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // PASSWORD HIDE/VISIBLE
        editTextPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int width = editTextPassword.getWidth();
                int paddingRight = editTextPassword.getPaddingRight();
                float touchX = event.getX();

                if (touchX > width - paddingRight - editTextPassword.getCompoundDrawables()[2].getBounds().width()) {

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

            // LogIn using Firebase Database
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "[SUCCESS] Login Successful!");

                    // Go to Main Page
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(LoginActivity.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                    Log.d("Login", "[FAILED] Login Failed!");

                }
            });

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

}

