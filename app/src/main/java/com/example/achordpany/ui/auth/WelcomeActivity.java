package com.example.achordpany.ui.auth;
import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);  // Ensure the correct layout is set

        // Set up theme toggle icon
        ImageButton themeToggle = findViewById(R.id.themeToggle);
        // Toggle listener
        themeToggle.setOnClickListener(v -> {
            boolean dark = sharedPreferences.getBoolean("darkMode", false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("darkMode", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("darkMode", true);
            }
            editor.apply();
        });


        // Go to Login Activity
        findViewById(R.id.loginBtn).setOnClickListener(v -> {

            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        });

        // Go to Signup (MainActivity hosting Signup Fragments)
        findViewById(R.id.signUpBtn).setOnClickListener(v -> {

            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();

        });

        ImageView logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setImageResource(R.drawable.logo_light);
    }
}
