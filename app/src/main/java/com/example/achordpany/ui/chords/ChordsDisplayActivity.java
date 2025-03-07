package com.example.achordpany.ui.chords;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;

import java.util.ArrayList;
import java.util.List;

public class ChordsDisplayActivity extends AppCompatActivity {

    private String song_TITLE;
    private String song_ARTIST;
    private String song_URL;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        // Process Recorded Lyrics
        SongTitleProcessing songTitleProcessing = SongTitleProcessing.getInstance();
        get_SongTitleArtist(songTitleProcessing.get_SongLyrics());
        get_ChordsURL(song_TITLE, song_ARTIST); // We have now the URL inside song_URL.

        btnBack = findViewById(R.id.btnBack);

        // Handle Back Button Click
        btnBack.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(ChordsDisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Find WebView
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        // Securely Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);  // Prevents file-based XSS attacks
        webSettings.setAllowContentAccess(false); // Blocks unsafe content access
        webSettings.setDomStorageEnabled(true);  // Enables local storage for modern sites
        webSettings.setBlockNetworkLoads(false); // Allows network requests, but only to trusted URLs
        webSettings.setBlockNetworkImage(false); // Allows image loading
        //webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        // Secure WebViewClient (Prevents opening external browsers)
        // Restrict URL Loading
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                /*if (url.startsWith("https://tabs.ultimate-guitar.com/")) { // Allow only trusted domain
                    view.loadUrl(url);
                }*/
                if (url.startsWith("https://")) { // Allow only HTTPS links
                    view.loadUrl(url);
                }
                return true; // Blocks other links
            }
        });

        // Optional: Improve WebView Performance
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Load URL (Ensure it’s HTTPS)
        webView.loadUrl(song_URL);

    }

    private void get_SongTitleArtist(String songLyrics) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_songtitleauthor");
        if(pyModule == null) {
            Log.d("SONG TITLE ARTIST", "Python Module Not Found");
            return;
        }

        PyObject pyObjectResult = pyModule.callAttr("get_song_by_lyrics", songLyrics);
        if(pyObjectResult == null || pyObjectResult.toString().equals("None")) {
            Log.d("SONG TITLE ARTIST", "Python Module Not Found");
            return;
        }

        List<PyObject> pyList = pyObjectResult.asList();
        List<String> songTitleArtistList = new ArrayList<>();

        for (PyObject obj : pyList) {

            List<PyObject> tuple = obj.asList();  // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            songTitleArtistList.add(songTitle + "|||||" + artist);  // ||||| is the separator to be used later

        }

        for(String i : songTitleArtistList) {
            Log.d("SONG TITLE ARTIST", i);
        }

        Log.d("[TITLE/ARTIST CHOSEN]", songTitleArtistList.get(0));

        String song_TITLEARTIST = songTitleArtistList.get(0);
        String[] parts = song_TITLEARTIST.split("\\|\\|\\|\\|\\|");

        song_TITLE = parts[0];
        song_ARTIST = parts.length > 1 ? parts[1] : ""; // Avoid index errors

    }

    private void get_ChordsURL(String search_SongTitle, String search_SongArtist) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_chordswebsite");
        if(pyModule == null) {
            Log.d("CHORDS WEBSITE", "Python Module Not Found");
            return;
        }

        PyObject pyObjectResult = pyModule.callAttr("find_chords", search_SongTitle.trim(), search_SongArtist.trim());
        if(pyObjectResult == null || pyObjectResult.toString().equals("None")) {
            Log.d("CHORDS WEBSITE", "Python Module Not Found");
            return;
        }

        song_URL = pyObjectResult.toString();

        // Add to ChordsSearchedHistory (Local database)
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        ChordsSearchedHistory chordsSearchedHistory = ChordsSearchedHistory.getInstance();

        if(chordsSearchedHistory.get_Title().size() >= 5) {

            chordsSearchedHistory.get_Title().remove(0);
            chordsSearchedHistory.get_Artist().remove(0);
            chordsSearchedHistory.get_Genre().remove(0);
            chordsSearchedHistory.get_Site().remove(0);
            chordsSearchedHistory.get_URL().remove(0);
            chordsSearchedHistory.get_isBookmarked().remove(0);
            chordsSearchedHistory.get_UID().remove(0);

        }

        chordsSearchedHistory.set_Title(search_SongTitle);
        chordsSearchedHistory.set_Artist(search_SongArtist);
        chordsSearchedHistory.set_Genre("No Genre");
        chordsSearchedHistory.set_Site("Ultimate Guitar");
        chordsSearchedHistory.set_URL(song_URL);
        chordsSearchedHistory.set_isBookmarked("false");

        while(true) {

            int create_UID = (int)(Math.random() * 5) + 1;
            if(!chordsSearchedHistory.get_UID().contains(Integer.toString(create_UID))) {
                chordsSearchedHistory.set_UID(Integer.toString(create_UID));
                break;
            }

        }

        // Update Firebase Database for History
        chordsSearchedHistory.updateHistory_FirebaseDatabase(main_EverythingLocalDatabase.get_Username());

    }

}