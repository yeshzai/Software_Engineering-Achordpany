package com.example.achordpany.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;

public class LoginSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);

        // You can show a Lottie animation, ProgressBar, or any graphic here

        // Delay then go to MainActivity (or Home)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // close this loading screen
        }, 2000); // 2 seconds delay (adjust as needed)
    }

}
