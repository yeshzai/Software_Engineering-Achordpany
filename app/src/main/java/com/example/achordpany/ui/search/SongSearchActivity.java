package com.example.achordpany.ui.search;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.achordpany.R;
import com.example.achordpany.MainActivity;

public class SongSearchActivity extends AppCompatActivity {
    private TextView txtStatus, txtAboveWave, txtCountdown;
    //private Button btnAction;
    private Button btnSearch;
    private ImageButton btnBack, btnBackBottom, btnRestart, btnStop, btnRestartPleaseWait, btnStopRedirecting;
    private LinearLayout actionButtons;
    private LottieAnimationView waveAnimation;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable listeningTimeoutRunnable;
    private int step = 0;
    private int timeRemaining = 15; // Countdown from 15 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        txtStatus = findViewById(R.id.txtStatus);
        txtAboveWave = findViewById(R.id.txtAboveWave);
        txtCountdown = findViewById(R.id.txtCountdown);
        waveAnimation = findViewById(R.id.waveAnimation);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        btnBackBottom = findViewById(R.id.btnBackBottom);
        actionButtons = findViewById(R.id.actionButtons);
        btnRestart = findViewById(R.id.btnRestart);
        btnStop = findViewById(R.id.btnStop);
        btnRestartPleaseWait = findViewById(R.id.btnRestartPleaseWait);
        btnStopRedirecting = findViewById(R.id.btnStopRedirecting);

        // Handle Back Button Click
        btnBack.setOnClickListener(v -> finish());
        btnBackBottom.setOnClickListener(v -> finish());

        // Button Click: Start animation and change text dynamically
        btnSearch.setOnClickListener(v -> nextStep());

        btnRestart.setOnClickListener(v -> restartListening());
        btnStop.setOnClickListener(v -> stopListening());
        btnRestartPleaseWait.setOnClickListener(v -> restartListening());
        btnStopRedirecting.setOnClickListener(v -> finish());

        btnRestart.setOnClickListener(v -> {
            if (!btnRestart.isEnabled()) return; // Ignore clicks if disabled
            restartListening();
        });

        btnStop.setOnClickListener(v -> {
            if (!btnStop.isEnabled()) return; // Ignore clicks if disabled
            stopListening();
        });

    }

    private void nextStep() {
        step++;
        switch (step) {
            case 1: // Playing a Song
                findViewById(R.id.headerLayout).setVisibility(View.GONE); // Hide the Toolbar
                btnSearch.setVisibility(View.GONE);
                btnBackBottom.setVisibility(View.VISIBLE); // Show the Bottom Return Button

                // Show "Playing a Song..." Above Animation
                txtAboveWave.setText(getString(R.string.play_a_song));
                txtAboveWave.setVisibility(View.VISIBLE);

                // Hide action buttons initially
                actionButtons.setVisibility(View.GONE);
                btnRestart.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnRestartPleaseWait.setVisibility(View.GONE);
                btnStopRedirecting.setVisibility(View.GONE);

                startListeningForAudio();
                break;
            case 2: // Listening Mode
                txtAboveWave.setText(getString(R.string.listening));
                actionButtons.setVisibility(View.VISIBLE);
                btnBackBottom.setVisibility(View.GONE);

                txtCountdown.setVisibility(View.VISIBLE);
                startCountdownTimer();

                btnRestart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);

                // Start Listening Timer (1 Minute)
                listeningTimeoutRunnable = () -> nextStep(); // Move to "Please Wait"
                handler.postDelayed(listeningTimeoutRunnable, 15000);

                break;
            case 3: // Please Wait
                txtAboveWave.setText(getString(R.string.please_wait));
                actionButtons.setVisibility(View.GONE);
                txtCountdown.setVisibility(View.GONE);

                btnRestart.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnRestartPleaseWait.setVisibility(View.VISIBLE);

                // Auto Proceed to Redirecting in 2 Seconds
                handler.postDelayed(() -> nextStep(), 2000);
                break;
            case 4: // Redirecting
                txtAboveWave.setText(getString(R.string.redirecting));

                btnRestartPleaseWait.setVisibility(View.GONE);
                btnStopRedirecting.setVisibility(View.VISIBLE);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent = new Intent(SongSearchActivity.this, MainActivity.class);
                    intent.putExtra("openChords", true); // Pass data to indicate navigation
                    startActivity(intent);
                    finish();
                }, 2000);
                break;
        }
    }

    private void restartListening() {
        btnRestart.setEnabled(false);
        handler.removeCallbacks(listeningTimeoutRunnable);
        timeRemaining = 15;
        step = 1;
        nextStep(); // Restart Listening

        // Re-enable button after 1 second
        handler.postDelayed(() -> btnRestart.setEnabled(true), 1000);
    }

    private void stopListening() {
        btnStop.setEnabled(false);
        handler.removeCallbacks(listeningTimeoutRunnable);

        step = 2;
        handler.postDelayed(() -> nextStep(), 500);

        // Re-enable button after 1 second
        handler.postDelayed(() -> btnStop.setEnabled(true), 1000);
    }

    private void startCountdownTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    txtCountdown.setText("Time Remaining: " + timeRemaining + "s");
                    timeRemaining--;
                    handler.postDelayed(this, 1000);
                } else {
                    txtCountdown.setText("Time Remaining: 0s");
                }
            }
        });
    }

    private void startListeningForAudio() {
        waveAnimation.playAnimation(); // Start animation

        // Simulate 5 seconds of listening (Replace with real audio processing)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            nextStep(); // Move to Step 3 when done
        }, 5000);
    }
}