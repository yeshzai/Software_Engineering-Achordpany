package com.example.achordpany.ui.auth;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ensure this matches your Login layout file

        // Handle Back Button Click - Navigate to WelcomeActivity
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
            startActivity(intent);
        });

        // If login is successful, navigate to MainActivity (which hosts HomeFragment)
        findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close LoginActivity so it doesn't stay in the back stack
        });

        // If user clicks signup, navigate to MainActivity and open SignupFragment1
        findViewById(R.id.textNoAccount).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("navigateToSignup", true); // Extra to tell MainActivity to open SignupFragment
            startActivity(intent);
            finish();
            //NavController navController = Navigation.findNavController(LoginActivity.this, R.id.nav_host_fragment);
            //navController.navigate(R.id.signupFragment1);
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

