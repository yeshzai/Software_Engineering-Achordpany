package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.auth.WelcomeActivity;

public class SignUpActivity extends AppCompatActivity  {

    // SignUpActivity #1 - Declaration
    private ImageButton btnBack_new1;
    private Button btnContinue_new1;
    private TextView textHaveAccount_new1;

    // SignUpActivity #2 - Declaration
    private ImageButton btnBack_new2;
    private Button btnContinue_new2;
    private TextView textHaveAccount_new2;

    // SignUpActivity #3 - Declaration
    private ImageButton btnBack_new3;
    private Button btnContinue_new3;
    private TextView textHaveAccount_new3;

    // SignUpActivity #4 - Declaration
    private ImageButton btnBack_new4;
    private Button btnSignupEnd_new4;
    private TextView textHaveAccount_new4;

    private int currentStep = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_1);

        // SignUpActivity #1
        if(currentStep == 1) {
            btnBack_new1 = findViewById(R.id.btnBack_new1);
            btnContinue_new1 = findViewById(R.id.btnContinue_new1);
            textHaveAccount_new1 = findViewById(R.id.textHaveAccount_new1);
            btnContinue_new1.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_2);
                btnBack_new2 = findViewById(R.id.btnBack_new2);
                btnContinue_new2 = findViewById(R.id.btnContinue_new2);
                textHaveAccount_new2 = findViewById(R.id.textHaveAccount_new2);
                currentStep = 2;
            });

            btnBack_new1.setOnClickListener(v -> {
                Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                startActivity(intent);
                currentStep = 1;
                finish();
            });
        }

        // SignUpActivity #2
        if(currentStep == 2) {
            btnContinue_new2.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_3);
                btnBack_new3 = findViewById(R.id.btnBack_new3);
                btnContinue_new3 = findViewById(R.id.btnContinue_new3);
                textHaveAccount_new3 = findViewById(R.id.textHaveAccount_new3);
                currentStep = 3;
            });

            btnBack_new2.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_1);
                btnBack_new1 = findViewById(R.id.btnBack_new1);
                btnContinue_new1 = findViewById(R.id.btnContinue_new1);
                textHaveAccount_new1 = findViewById(R.id.textHaveAccount_new1);
                currentStep = 1;
            });
        }

        // SignUpActivity #3
        if(currentStep == 3) {
            btnContinue_new3.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_4);
                btnBack_new4 = findViewById(R.id.btnBack_new4);
                btnSignupEnd_new4 = findViewById(R.id.btnSignupEnd_new4);
                textHaveAccount_new4 = findViewById(R.id.textHaveAccount_new4);
                currentStep = 4;
            });

            btnBack_new3.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_2);
                btnBack_new2 = findViewById(R.id.btnBack_new2);
                btnContinue_new2 = findViewById(R.id.btnContinue_new2);
                textHaveAccount_new2 = findViewById(R.id.textHaveAccount_new2);
                currentStep = 2;
            });
        }

        // SignUpActivity #4
        if(currentStep == 4) {
            btnSignupEnd_new4.setOnClickListener(v -> {
                // Create Account.
                Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                startActivity(intent);
                currentStep = 1;
                finish();
            });

            btnBack_new4.setOnClickListener(v -> {
                setContentView(R.layout.activity_signup_3);
                btnBack_new3 = findViewById(R.id.btnBack_new3);
                btnContinue_new3 = findViewById(R.id.btnContinue_new3);
                textHaveAccount_new3 = findViewById(R.id.textHaveAccount_new3);
                currentStep = 3;
            });
        }

    }

}
