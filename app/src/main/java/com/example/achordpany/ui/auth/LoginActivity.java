package com.example.achordpany.ui.auth;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.achordpany.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ensure this matches your Login layout file

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

