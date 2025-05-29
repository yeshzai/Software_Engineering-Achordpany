package com.example.achordpany.ui.signup;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;

public class SignUpActivity extends AppCompatActivity {
    private Uri selectedProfileImageUri;  // Store the selected image

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(savedInstanceState == null) {
            navigateToStep(1); // Start on Step 1
        }
    }

    public void navigateToStep(int step) {
        Fragment fragment = null;
        switch (step) {
            case 1:
                fragment = new SignUpStep1Fragment();
                break;
            case 2:
                fragment = new SignUpStep2Fragment();
                break;
            case 3:
                fragment = new SignUpStep3Fragment();
                break;
            case 4:
                fragment = new SignUpStep4Fragment();
                break;
            case 5:
                fragment = new SignUp_TermsConditionsFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public void setSelectedProfileImageUri(Uri uri) {
        this.selectedProfileImageUri = uri;
        Log.d("SignUpActivity", "Stored Image URI: " + uri);
    }

    public Uri getSelectedProfileImageUri() {
        Log.d("SignUpActivity", "Retrieving Image URI: " + selectedProfileImageUri);
        return selectedProfileImageUri;
    }
}
