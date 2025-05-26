package com.example.achordpany.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

interface UsernameCallBack {
    void onUsernameReceived(String username);
}

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private boolean isPasswordVisible = false;
    private TextInputEditText editTextEmail, editTextPassword;
    private TextInputLayout emailLayout, passwordLayout;
    private TextView textForgotPassword;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textForgotPassword = findViewById(R.id.textForgotPassword);

        textForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RecoverAccountActivity.class));
            finish();
        });

        editTextEmail.setOnTouchListener((v, event) -> {

            return_allDefaultBackground();
            //editTextEmail.setBackground(defaultBackground);
            return false;
        });

        editTextPassword.setOnTouchListener((v, event) -> {

            return_allDefaultBackground();
            //editTextPassword.setBackground(defaultBackground);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[2].getBounds().width())) {
                    if (!editTextPassword.isFocused()) editTextPassword.requestFocus();
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        findViewById(R.id.buttonLogin).setOnClickListener(v -> {

            //editTextEmail.setBackground(defaultBackground);
            //editTextPassword.setBackground(defaultBackground);
            return_allDefaultBackground();

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> findUsernameByEmail(email, username -> {
                            if (username != null) {
                                Log.d("USERNAME SEARCH", "[SUCCESS] Username: " + username);
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.activity_loginsuccess);
                                Main_EverythingLocalDatabase.getInstance(LoginActivity.this).mainPage_RetrieveFirebase(username);
                            } else {
                                Log.d("USERNAME SEARCH", "[FAILED] Username Not Found!");
                            }
                        }))
                        .addOnFailureListener(e -> {
                            emailLayout.setError("Invalid email address");
                            passwordLayout.setError("Invalid password");
                            updatePasswordIcons();
                            Toast.makeText(LoginActivity.this, "Invalid Email Address or Password!", Toast.LENGTH_SHORT).show();
                            Log.d("Login", "[FAILED] Login Failed!");
                        });
            } else {

                if(editTextEmail.getText().toString().isEmpty())
                    emailLayout.setError("This field cannot be empty.");

                if(editTextPassword.getText().toString().isEmpty())
                    passwordLayout.setError("This field cannot be empty.");

                updatePasswordIcons(editTextPassword, isPasswordVisible);

                Toast.makeText(LoginActivity.this, "Please Fill In All Fields!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.textNoAccount).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        // Color the "Sign Up" part
        TextView textNoAccount = findViewById(R.id.textNoAccount);
        SpannableString spannable = new SpannableString("Don't have an account? Sign Up");
        int start = spannable.toString().indexOf("Sign Up");
        int end = start + "Sign Up".length();
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.textColor)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textNoAccount.setText(spannable);
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        // Toggle password visibility
        editTextPassword.setTransformationMethod(isPasswordVisible ? null : new PasswordTransformationMethod());
        // Move cursor to the end after toggle
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private void updatePasswordIcons() {
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout); // replace with your layout ID

        // Set start icon to default lock icon (material default)
        passwordLayout.setStartIconDrawable(null); // remove custom lock icon

        // Enable built-in password toggle icon
        passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
    }

    private void findUsernameByEmail(String the_email, UsernameCallBack callBack) {
        DatabaseReference userCredentials = FirebaseDatabase.getInstance().getReference("Users_Credentials");
        userCredentials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String emailFromDB = snap.child("email").getValue(String.class);
                    if (emailFromDB != null && emailFromDB.equalsIgnoreCase(the_email)) {
                        callBack.onUsernameReceived(snap.child("username").getValue(String.class));
                        return;
                    }
                }
                callBack.onUsernameReceived(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onUsernameReceived(null);
            }
        });
    }

    private void updatePasswordIcons(EditText editText, boolean isVisible) {
        Drawable lockIcon = ContextCompat.getDrawable(this, R.drawable.ic_lock);
        Drawable eyeIcon = ContextCompat.getDrawable(this,
                isVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);
        editText.setCompoundDrawablesWithIntrinsicBounds(lockIcon, null, eyeIcon, null);

        TextInputLayout layout = (TextInputLayout) editText.getParent().getParent();

        // Set the start icon (lock icon)
        layout.setStartIconDrawable(R.drawable.ic_lock);

        // Tell TextInputLayout to use a custom end icon
        layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);

        // Set the end icon (eye icon)
        layout.setEndIconDrawable(isVisible ? R.drawable.ic_eye : R.drawable.ic_eyehide);
    }

    private void return_allDefaultBackground() {

        emailLayout.setError(null);
        emailLayout.setErrorEnabled(false);

        passwordLayout.setError(null);
        passwordLayout.setErrorEnabled(false);

    }


}
