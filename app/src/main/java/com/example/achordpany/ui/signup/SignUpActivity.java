package com.example.achordpany.ui.signup;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;

public class SignUpActivity extends AppCompatActivity {

    private Button btnContinue1;
    private Button btnContinue2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnContinue1 = findViewById(R.id.btnContinue1);
        btnContinue1.setOnClickListener(v -> {
            setContentView(R.layout.activity_signup2);
            btnContinue2 = findViewById(R.id.btnContinue2);
        });

    }

}
