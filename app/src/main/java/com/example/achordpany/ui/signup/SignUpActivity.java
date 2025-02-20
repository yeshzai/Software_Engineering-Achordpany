package com.example.achordpany.ui.signup;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

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
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

    }

}
