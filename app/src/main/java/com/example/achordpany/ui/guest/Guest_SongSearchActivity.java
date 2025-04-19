package com.example.achordpany.ui.guest;

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
import com.example.achordpany.ui.chords.ChordsDisplayActivity;
import com.example.achordpany.ui.chords.SongTitleProcessing;

import java.util.ArrayList;

public class Guest_SongSearchActivity extends AppCompatActivity {
    private TextView txtStatus, txtAboveWave, txtCountdown;
    //private Button btnAction;
    private Button btnSearch;
    private ImageButton btnBack, btnBackBottom, btnRestart, btnStop, btnRestartPleaseWait, btnStopRedirecting;
    private LinearLayout actionButtons;
    private LottieAnimationView waveAnimation;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable listeningTimeoutRunnable;
    private int step = 0;
    private int timeRemaining = 10; // Countdown from 15 seconds

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

        /*
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

                if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT ||
                        error == SpeechRecognizer.ERROR_NO_MATCH ||
                        error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                    continue_Listening();
                }

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> recognized_speech = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(recognized_speech != null && !recognized_speech.isEmpty()) {

                    recognizedLyrics = recognizedLyrics + " " + recognized_speech.get(0);
                    Log.d("SPEECH RECOGNIZER [CONTINUED]", recognized_speech.get(0));
                    continue_Listening();

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}

        });
        */

        recognition_Function();
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
        btnBack.setOnClickListener(v -> {
            cleanupAndExit();
        });

        btnBackBottom.setOnClickListener(v -> {
            cleanupAndExit();
        });

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

    private void cleanupAndExit() {

        Log.d("[SONG SEARCH ACTIVITY]", "EXIT CLEANUP");

        // Destroy the SpeechRecognizer properly
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

        // Remove any pending handlers or callbacks to prevent memory leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        step = 0; // Reset step to avoid executing pending `nextStep()`
        recognizedLyrics = "";

        // Navigate back to MainActivity while clearing any remaining instances of SongSearchActivity
        Intent intent = new Intent(Guest_SongSearchActivity.this, GuestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void recognition_Function() {

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

                if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT ||
                        error == SpeechRecognizer.ERROR_NO_MATCH ||
                        error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) {
                    continue_Listening();
                }

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> recognized_speech = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(recognized_speech != null && !recognized_speech.isEmpty()) {

                    recognizedLyrics = recognizedLyrics + " " + recognized_speech.get(0);
                    Log.d("SPEECH RECOGNIZER [CONTINUED]", recognized_speech.get(0));
                    continue_Listening();

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}

        });
        // [BANDAID] - SPEECH RECOGNIZER

    }

    private void continue_Listening() {

        if(timeRemaining > 1) {
            speechRecognizer.stopListening();
            speechRecognizer.startListening(speechRecognizerIntent);
        }

    }

    private void nextStep() {
        step++;
        switch (step) {
            case 1: // Playing a Song
                findViewById(R.id.headerLayout).setVisibility(View.GONE); // Hide the Toolbar
                btnSearch.setVisibility(View.GONE);
                btnBackBottom.setVisibility(View.VISIBLE); // Show the Bottom Return Button

                // Show "Preparing to Record..." Above Animation
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
                if (speechRecognizer != null) {
                    Log.d("SPEECH RECOGNIZER", "Started Listening.");
                    speechRecognizer.startListening(speechRecognizerIntent);
                    startCountdownTimer();
                } else {
                    Log.e("SPEECH RECOGNIZER", "Speech Recognizer is NULL.");
                    return; // Prevent further execution
                }
                // [BANDAID] - SPEECH RECOGNIZER (Start Listening) | END |

                btnRestart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);

                // Start Listening Timer (1 Minute)
                listeningTimeoutRunnable = () -> nextStep(); // Move to "Please Wait"
                handler.postDelayed(listeningTimeoutRunnable, 11000);

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

                    SongTitleProcessing songTitleProcessing = SongTitleProcessing.getInstance();
                    recognizedLyrics = recognizedLyrics.trim();
                    songTitleProcessing.set_SongLyrics(recognizedLyrics);
                    Log.d("SPEECH RECOGNIZER [OUTPUT]", recognizedLyrics);

                    //Intent intent = new Intent(Guest_SongSearchActivity.this, MainActivity.class);
                    //intent.putExtra("openChords", true); // Pass data to indicate navigation
                    //startActivity(intent);
                    //finish();

                    Intent intent = new Intent(Guest_SongSearchActivity.this, ChordsDisplayActivity.class);
                    startActivity(intent);
                    finish();

                }, 2000);
                break;
        }
    }

    private void restartListening() {

        Log.d("SPEECH RECOGNIZER", "Restarting Listening ...");

        // Destroy old SpeechRecognizer instance to avoid conflicts
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

        // Reinitialize SpeechRecognizer
        recognition_Function();

        // Reset variables
        recognizedLyrics = "";
        timeRemaining = 10;

        // Ensure previous countdown is stopped
        handler.removeCallbacksAndMessages(null);

        handler.postDelayed(() -> {
            btnRestart.setEnabled(false);
            step = 1;
            nextStep();
            btnRestart.setEnabled(true);
        }, 1000);
    }

    private void stopListening() {
        btnStop.setEnabled(false);
        handler.removeCallbacks(listeningTimeoutRunnable);

        txtAboveWave.setText("Recording Done!");
        timeRemaining = 0;
        step = 2;
        handler.postDelayed(() -> nextStep(), 0);

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
                    //txtCountdown.setText("Time Remaining: 0s");
                    txtAboveWave.setText("Recording Done!");
                    txtCountdown.setText("");
                }
            }
        });

    }

    private void startListeningForAudio() {
        waveAnimation.playAnimation(); // Start animation

        // Simulate 5 seconds of listening (Replace with real audio processing)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            nextStep(); // Move to Step 2
        }, 2000);   // Changed from 5000 (5 seconds) to 500 (2 second) - KaytoKidd
    }

    // [BANDAID] - SPEECH RECOGNIZER
    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            if (speechRecognizer != null) {
                speechRecognizer.destroy();
                speechRecognizer = null;
            }

            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }

            Log.d("SongSearchActivity", "onDestroy() cleanup complete.");
        } catch (Exception e) {
            Log.e("SongSearchActivity", "Error in onDestroy: " + e.getMessage());
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