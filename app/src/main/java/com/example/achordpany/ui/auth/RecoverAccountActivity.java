package com.example.achordpany.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecoverAccountActivity extends AppCompatActivity {

    private Drawable defaultBackground;
    private TextInputLayout recoverAccount_EmailAddressLayout;
    private TextInputEditText recoverAccount_EmailAddress;
    TextView recoverAccount_Subtitle;

    FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoveraccount);

        recoverAccount_EmailAddressLayout = findViewById(R.id.recoverAccount_EmailAddressLayout);

        auth = FirebaseAuth.getInstance();
        recoverAccount_Subtitle = findViewById(R.id.recoverAccount_Subtitle);
        recoverAccount_EmailAddress = findViewById(R.id.recoverAccount_EmailAddress);
        Button recoverAccount_Button = findViewById(R.id.recoverAccount_Button);
        ImageView recoverAccount_Back = findViewById(R.id.recoverAccount_Back);
        defaultBackground = recoverAccount_EmailAddress.getBackground();

        recoverAccount_EmailAddress.setOnTouchListener((v, event) -> {
            //recoverAccount_EmailAddress.setBackground(defaultBackground);
            recoverAccount_EmailAddressLayout.setError(null);
            recoverAccount_EmailAddressLayout.setErrorEnabled(false);
            return false;
        });

        recoverAccount_Button.setOnClickListener(v -> {

            if(recoverAccount_EmailAddress.getText().toString().isEmpty()) {
                recoverAccount_EmailAddressLayout.setError("This field cannot be empty.");
                Toast.makeText(getApplicationContext(), "Please enter your email address.",
                        Toast.LENGTH_SHORT).show();
                //recoverAccount_EmailAddress.setBackgroundResource(R.drawable.edittext_error);
                return;
            } else {
                //recoverAccount_EmailAddress.setBackground(defaultBackground); hi
                recoverAccount_EmailAddressLayout.setError(null);
                recoverAccount_EmailAddressLayout.setErrorEnabled(false);
                recoverAccount_Function(recoverAccount_EmailAddress.getText().toString());
            }

        });

        recoverAccount_Back.setOnClickListener(v -> {
            Intent intent = new Intent(RecoverAccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void recoverAccount_Function(String recover_email) {

        auth.sendPasswordResetEmail(recover_email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("[RECOVER ACCOUNT]", "[SUCCESS] Recovery Link Sent!");
                        String recovery_Text = "The recovery link has been sent to your email.";
                        recoverAccount_Subtitle.setText(recovery_Text);
                        Toast.makeText(getApplicationContext(), "The recovery link has been sent to your email.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("[RECOVER ACCOUNT]", "[FAILED] Error Sending Recovery Email.");
                        Toast.makeText(getApplicationContext(), "Error sending recovery email. Try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

}