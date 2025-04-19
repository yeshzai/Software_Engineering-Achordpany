package com.example.achordpany.ui.guest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.search.SongSearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GuestActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guesthome);

        bottomNavigationView = findViewById(R.id.nav_view_GUEST);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            Toast.makeText(this, "Create an account to use this feature.", Toast.LENGTH_SHORT).show();
            return true;

        });

        FloatingActionButton fab_GUEST = findViewById(R.id.fab_GUEST);
        fab_GUEST.setOnClickListener(v -> {

            //Intent intent = new Intent(GuestActivity.this, SongSearchActivity.class);
            //startActivity(intent);
            //finish();

        });

    }
}
