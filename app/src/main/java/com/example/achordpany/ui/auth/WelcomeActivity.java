package com.example.achordpany.ui.auth;
import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);  // Ensure the correct layout is set

        // Go to Login Activity
        findViewById(R.id.loginBtn).setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Go to Signup (MainActivity hosting Signup Fragments)
        findViewById(R.id.signUpBtn).setOnClickListener(v -> {

            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        ImageView logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setImageResource(R.drawable.logo_light);
    }
}
