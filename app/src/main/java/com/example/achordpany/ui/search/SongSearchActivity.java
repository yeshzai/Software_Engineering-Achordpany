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

import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private String recognizedLyrics = "";
    
    private static final String SERVER_IP = "192.168.1.100"; // CHANGE THIS to your computer's IP address
    private static final String TRANSCRIPTION_SERVICE_URL = "http://" + SERVER_IP + ":5000/transcribe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
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
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    CHANNEL_CONFIG,
                    AUDIO_FORMAT,
                    minBufferSize);
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
                    
                    Intent intent = new Intent(SongSearchActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("openChords", true);
                    intent.putExtra("from_search", true);
                    startActivity(intent);
                    finish();
                }, 2000);
                break;
        }
    }

    private void startRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "Audio permission not granted");
            Toast.makeText(this, "Audio permission not granted", Toast.LENGTH_SHORT).show();
            cleanupAndExit();
            return;
        }

        if (audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Toast.makeText(this, "Error: Audio recorder not initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        final int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        final short[] audioBuffer = new short[bufferSize / 2];
        final ByteArrayOutputStream recordingBuffer = new ByteArrayOutputStream();

        audioRecord.startRecording();
        isRecording = true;

        new Thread(() -> {
            try {
                while (isRecording && timeRemaining > 0) {
                    int numberOfShorts = audioRecord.read(audioBuffer, 0, audioBuffer.length);
                    for (int i = 0; i < numberOfShorts; i++) {
                        recordingBuffer.write(audioBuffer[i] & 0xFF);
                        recordingBuffer.write((audioBuffer[i] >> 8) & 0xFF);
                    }
                }

                // Convert to float array and normalize
                byte[] recordedData = recordingBuffer.toByteArray();
                float[] floatData = new float[recordedData.length / 2];
                ByteBuffer.wrap(recordedData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(new short[recordedData.length / 2]);
                
                float maxAbs = 0.0f;
                for (int i = 0; i < floatData.length; i++) {
                    floatData[i] = audioBuffer[i] / 32768.0f;
                    maxAbs = Math.max(maxAbs, Math.abs(floatData[i]));
                }

                // Normalize
                if (maxAbs > 0) {
                    for (int i = 0; i < floatData.length; i++) {
                        floatData[i] /= maxAbs;
                    }
                }

                sendToTranscriptionService(floatData);

            } catch (Exception e) {
                Log.e("AUDIO", "Error recording audio: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(SongSearchActivity.this, "Error recording audio", Toast.LENGTH_SHORT).show();
                });
            } finally {
                if (audioRecord != null) {
                    audioRecord.stop();
                }
            }
        }).start();
    }

    private void sendToTranscriptionService(float[] audioData) {
        new Thread(() -> {
            try {
                // Convert audio data to bytes
                ByteBuffer byteBuffer = ByteBuffer.allocate(audioData.length * 4);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                for (float value : audioData) {
                    byteBuffer.putFloat(value);
                }
                byte[] audioBytes = byteBuffer.array();

                // Send to transcription service
                URL url = new URL(TRANSCRIPTION_SERVICE_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/octet-stream");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(audioBytes);
                }

                // Get response
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (InputStream is = conn.getInputStream();
                         ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                        byte[] responseBuffer = new byte[1024];
                        int length;
                        while ((length = is.read(responseBuffer)) != -1) {
                            result.write(responseBuffer, 0, length);
                        }
                        String jsonResponse = result.toString("UTF-8");
                        JSONObject json = new JSONObject(jsonResponse);
                        String transcription = json.getString("transcription");
                        
                        runOnUiThread(() -> {
                            recognizedLyrics = transcription;
                            nextStep();
                        });
                    }
                }
            } catch (Exception e) {
                Log.e("TRANSCRIPTION", "Error: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(SongSearchActivity.this, "Transcription error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
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
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        handler.removeCallbacksAndMessages(null);
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
