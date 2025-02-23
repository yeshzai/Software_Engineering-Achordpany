package com.example.achordpany.ui.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.achordpany.R;
import com.example.achordpany.MainActivity;
import com.example.achordpany.ui.chords.ChordsDisplayActivity;
import com.example.achordpany.ui.chords.SongTitleProcessing;

import java.util.ArrayList;

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

    // [BANDAID] - SPEECH RECOGNIZER
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    String recognizedLyrics = "";
    // [BANDAID] - SPEECH RECOGNIZER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        // [BANDAID] - SPEECH RECOGNIZER
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                Log.d("SPEECH RECOGNIZER", "Error: " + error);
            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> recognized_speech = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(recognized_speech != null && !recognized_speech.isEmpty()) {

                    SongTitleProcessing songTitleProcessing = SongTitleProcessing.getInstance();
                    recognizedLyrics = recognized_speech.get(0);
                    songTitleProcessing.set_SongLyrics(recognizedLyrics);
                    Log.d("SPEECH RECOGNIZER [OUTPUT]", recognizedLyrics);

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}

        });
        // [BANDAID] - SPEECH RECOGNIZER

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

                // [BANDAID] - SPEECH RECOGNIZER (Start Listening) | START |
                Log.d("SPEECH RECOGNIZER", "Started Listening.");

                speechRecognizer.startListening(speechRecognizerIntent);
                startCountdownTimer();
                // [BANDAID] - SPEECH RECOGNIZER (Start Listening) | END |

                btnRestart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);

                // Start Listening Timer (1 Minute)
                listeningTimeoutRunnable = () -> nextStep(); // Move to "Please Wait"
                handler.postDelayed(listeningTimeoutRunnable, 15000);

                break;
            case 3: // Please Wait

                // [BANDAID] - SPEECH RECOGNIZER (Stop Listening) | START |
                Log.d("SPEECH RECOGNIZER", "Stopped Listening.");

                speechRecognizer.stopListening();
                // [BANDAID] - SPEECH RECOGNIZER (Stop Listening) | END |

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
            nextStep(); // Move to Step 2
        }, 1000);   // Changed from 5000 (5 seconds) to 1000 (1 second) - KaytoKidd
    }

    // [BANDAID] - SPEECH RECOGNIZER
    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(speechRecognizer != null) {
            speechRecognizer.destroy();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("REQUEST AUDIO RESULT", "Permission Granted");
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("REQUEST AUDIO RESULT", "Permission Denied");
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            }
        }

    }
    // [BANDAID] - SPEECH RECOGNIZER

}