package com.example.achordpany.ui.auth;
import com.example.achordpany.R;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);  // Ensure the correct layout is set

        ImageView logoImageView = findViewById(R.id.logoImageView);
        logoImageView.setImageResource(R.drawable.logo_light);
    }
}
