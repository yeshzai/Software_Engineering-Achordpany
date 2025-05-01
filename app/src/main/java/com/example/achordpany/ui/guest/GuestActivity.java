package com.example.achordpany.ui.guest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.auth.WelcomeActivity;
import com.example.achordpany.ui.search.SongSearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesthome);

        findViewById(R.id.btnBack_GUEST).setOnClickListener(v -> {

            Intent intent = new Intent(GuestActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();

        });

        FloatingActionButton fab_GUEST = findViewById(R.id.fab_GUEST);
        fab_GUEST.setOnClickListener(v -> {

            Intent intent = new Intent(GuestActivity.this, Guest_SongSearchActivity.class);
            startActivity(intent);
            finish();

        });

    }
}
