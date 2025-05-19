package com.example.achordpany.ui.search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.chords.SongTitleProcessing;
import com.example.achordpany.ui.chords.ChordsDisplayActivity;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class SongSearchActivity extends AppCompatActivity {
    private TextView txtStatus, txtAboveWave, txtCountdown;
    private Button btnSearch;
    private ImageButton btnBack, btnBackBottom, btnRestart, btnStop, btnRestartPleaseWait, btnStopRedirecting;
    private LinearLayout actionButtons;
    private LottieAnimationView waveAnimation;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable listeningTimeoutRunnable;
    private int step = 0;
    private int timeRemaining = 10;

    private static final String TAG = "SongSearchActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private static final int SAMPLE_RATE = 16000; // 16kHz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE_FACTOR = 2;
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private String recognizedLyrics = "";
    private TranscriptionModel transcriptionModel;
    private List<Short> audioBuffer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        try {
            transcriptionModel = new TranscriptionModel(this);
            Log.d(TAG, "Transcription model initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize transcription model: " + e.getMessage());
            Toast.makeText(this, "Failed to initialize transcription service", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initializeViews();
        setupClickListeners();
        initializeAudioRecorder();
    }

    private void initializeViews() {
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

        String jsonFileName = getThemeJsonFilename();
        waveAnimation.setAnimation(getRawResourceIdByName(jsonFileName));
    }

    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> nextStep());
        btnBack.setOnClickListener(v -> cleanupAndExit());
        btnBackBottom.setOnClickListener(v -> cleanupAndExit());
        btnRestart.setOnClickListener(v -> restartListening());
        btnStop.setOnClickListener(v -> stopListening());
        btnRestartPleaseWait.setOnClickListener(v -> restartListening());
        btnStopRedirecting.setOnClickListener(v -> finish());
    }

    private void initializeAudioRecorder() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "Audio permission not granted");
            return;
        }

        int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        if (minBufferSize != AudioRecord.ERROR && minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
            int bufferSize = minBufferSize * BUFFER_SIZE_FACTOR;
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    bufferSize);
        }
    }

    private void nextStep() {
        step++;
        switch (step) {
            case 1: // Playing a Song
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                    return;
                }

                findViewById(R.id.headerLayout).setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                btnBackBottom.setVisibility(View.VISIBLE);
                txtAboveWave.setText(getString(R.string.play_a_song));
                txtAboveWave.setVisibility(View.VISIBLE);
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
                btnRestart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                startRecording();
                startCountdownTimer();
                listeningTimeoutRunnable = this::nextStep;
                handler.postDelayed(listeningTimeoutRunnable, 11000);
                break;

            case 3: // Please Wait
                isRecording = false;
                txtAboveWave.setText(getString(R.string.please_wait));
                actionButtons.setVisibility(View.GONE);
                txtCountdown.setVisibility(View.GONE);
                btnRestart.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnRestartPleaseWait.setVisibility(View.VISIBLE);
                handler.postDelayed(this::nextStep, 2000);
                break;

            case 4: // Redirecting
                txtAboveWave.setText(getString(R.string.redirecting));
                btnRestartPleaseWait.setVisibility(View.GONE);
                btnStopRedirecting.setVisibility(View.VISIBLE);
                
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    SongTitleProcessing.getInstance().set_SongLyrics(recognizedLyrics.trim());
                    Log.d("TRANSCRIPTION", "Final lyrics: " + recognizedLyrics.trim());
                    
                    Intent intent = new Intent(SongSearchActivity.this, ChordsDisplayActivity.class);
                    startActivity(intent);
                    finish();
                }, 2000);
                break;
        }
    }

    private void startRecording() {
        if (audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord not initialized");
            return;
        }

        isRecording = true;
        audioBuffer.clear();

        Thread recordingThread = new Thread(() -> {
            audioRecord.startRecording();
            // Buffer size aligned to 16-bit samples
            short[] buffer = new short[4096];

            while (isRecording) {
                int read = audioRecord.read(buffer, 0, buffer.length);
                if (read > 0) {
                    // Store raw 16-bit samples
                    for (int i = 0; i < read; i++) {
                        audioBuffer.add(buffer[i]);
                    }
                }
            }

            audioRecord.stop();

            // Process the recorded audio
            try {
                // Convert the entire buffer to shorts array
                short[] audioData = new short[audioBuffer.size()];
                for (int i = 0; i < audioBuffer.size(); i++) {
                    audioData[i] = audioBuffer.get(i);
                }
                
                recognizedLyrics = transcriptionModel.transcribeAudio(audioData);
                Log.d(TAG, "Transcription result: " + recognizedLyrics);
                
                handler.post(this::nextStep);
            } catch (Exception e) {
                Log.e(TAG, "Error during transcription: " + e.getMessage());
                handler.post(() -> {
                    Toast.makeText(this, "Transcription failed", Toast.LENGTH_SHORT).show();
                    restartListening();
                });
            }
        });

        recordingThread.start();
    }

    private void startListeningForAudio() {
        waveAnimation.playAnimation();
        handler.postDelayed(this::nextStep, 2000);
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
                    txtAboveWave.setText("Recording Done!");
                    txtCountdown.setText("");
                }
            }
        });
    }

    private void restartListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Audio permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }

        recognizedLyrics = "";
        timeRemaining = 10;
        handler.removeCallbacksAndMessages(null);
        
        initializeAudioRecorder();
        
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
        isRecording = false;
        txtAboveWave.setText("Recording Done!");
        timeRemaining = 0;
        step = 2;
        handler.postDelayed(this::nextStep, 0);
        handler.postDelayed(() -> btnStop.setEnabled(true), 1000);
    }

    private void cleanupAndExit() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (transcriptionModel != null) {
            transcriptionModel.close();
        }
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }
        if (transcriptionModel != null) {
            transcriptionModel.close();
        }
        if (handler != null && listeningTimeoutRunnable != null) {
            handler.removeCallbacks(listeningTimeoutRunnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PERMISSION", "Audio permission granted");
                initializeAudioRecorder();
                if (step == 1) {
                    nextStep(); // Continue with the flow if we were waiting for permission
                }
            } else {
                Log.d("PERMISSION", "Audio permission denied");
                Toast.makeText(this, "Permission to record audio was denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getThemeJsonFilename() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES ? "wave_animation_dark" : "wave_animation";
    }

    private int getRawResourceIdByName(String filename) {
        return getResources().getIdentifier(filename, "raw", getPackageName());
    }
}
