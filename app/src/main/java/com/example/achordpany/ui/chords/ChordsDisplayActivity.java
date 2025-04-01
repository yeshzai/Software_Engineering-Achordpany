package com.example.achordpany.ui.chords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.ChordsBookmarks;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChordsDisplayActivity extends AppCompatActivity {

    private String song_TITLE;
    private String song_URL;
    private ImageButton btnBack;
    private Boolean success_OPEN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        // Process Recorded Lyrics
        SongTitleProcessing songTitleProcessing = SongTitleProcessing.getInstance();
        get_SongTitleArtist(songTitleProcessing.get_SongLyrics());

        btnBack = findViewById(R.id.btnBack);

        // Handle Back Button Click
        btnBack.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(ChordsDisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        if(success_OPEN) {

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

        } else {

            TextView txtTitle = findViewById(R.id.txtTitle);
            TextView txtLink = findViewById(R.id.txtLink);
            txtTitle.setText("No Song Found");
            txtLink.setText("");

            // Add visibility of prompt here.

            Toast.makeText(this, "Song cannot be found. Please try again.", Toast.LENGTH_SHORT).show();

        }

    }

    private void get_SongTitleArtist(String songLyrics) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_songtitleauthor");
        if(pyModule == null) {
            Log.d("SONG TITLE", "[ERROR] - PYMODULE");
            return;
        }

        PyObject pyObjectResult = pyModule.callAttr("get_song_by_lyrics", songLyrics);
        if(pyObjectResult == null || pyObjectResult.toString().equals("None")) {
            Log.d("SONG TITLE", "[ERROR] - PYOBJECTRESULT");
            return;
        }

        List<PyObject> pyList = pyObjectResult.asList();
        List<String> songTitleList = new ArrayList<>();

        for (PyObject obj : pyList) {

            String songTitle = obj.toString();
            songTitleList.add(songTitle);

        }

        for(String i : songTitleList) {
            Log.d("SONG TITLE", i);
        }

        Log.d("[SONG TITLE CHOSEN]", songTitleList.get(0));
        song_TITLE = songTitleList.get(0);
        get_ChordsURL(song_TITLE);

    }

    private void get_ChordsURL(String search_SongTitle) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_chordswebsite");
        if(pyModule == null) {
            Log.d("CHORDS WEBSITE", "[ERROR] - PYMODULE");
            return;
        }

        PyObject pyObjectResult = pyModule.callAttr("find_chords", search_SongTitle.trim());
        if(pyObjectResult == null || pyObjectResult.toString().equals("None")) {
            Log.d("CHORDS WEBSITE", "[ERROR] - PYOBJECTRESULT");
            return;
        }

        song_URL = pyObjectResult.toString();
        String song_ARTIST = "";

        // Regular expression to match the artist name part of the URL
        Pattern pattern = Pattern.compile("https://tabs\\.ultimate-guitar\\.com/tab/([a-zA-Z0-9-]+)");
        Matcher matcher = pattern.matcher(song_URL);

        if (matcher.find()) {

            song_ARTIST = matcher.group(1);  // Extracted artist name
            song_ARTIST = song_ARTIST.replaceAll("-", " ");

            // Split artist and capitalize each starting letter.
            String[] words = song_ARTIST.split(" ");
            StringBuilder result = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    // Capitalize the first letter and add the rest of the word in lowercase
                    result.append(word.substring(0, 1).toUpperCase())               // First letter to uppercase
                            .append(word.substring(1).toLowerCase())     // Rest of the word to lowercase
                            .append(" "); // Add a space between words
                }
            }

            // Remove the last space added after the final word
            song_ARTIST = result.toString().trim();
            Log.d("[SONG ARTIST CHOSEN]", song_ARTIST);

        } else {
            System.out.println("No match found!");
        }

        // Add to ChordsSearchedHistory (Local database)
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        ChordsSearchedHistory chordsSearchedHistory = ChordsSearchedHistory.getInstance();
        ChordsBookmarks chordsBookmarks = ChordsBookmarks.getInstance();

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
        chordsSearchedHistory.set_Artist(song_ARTIST);
        chordsSearchedHistory.set_Genre("No Genre");
        chordsSearchedHistory.set_Site("Ultimate Guitar");
        chordsSearchedHistory.set_URL(song_URL);
        chordsSearchedHistory.set_isBookmarked("false");

        Random random = new Random();
        char[] letters = {'a', 'b', 'c', 'd', 'e'};

        while(true) {

            String newUID = "" +
                    letters[random.nextInt(5)] +
                    letters[random.nextInt(5)] +
                    letters[random.nextInt(5)];

            if(!chordsSearchedHistory.get_UID().contains(newUID) && !chordsBookmarks.get_UID().contains(newUID)) {
                chordsSearchedHistory.set_UID(newUID);
                break;
            }

        }

        // Update Firebase Database for History
        chordsSearchedHistory.updateHistory_FirebaseDatabase(main_EverythingLocalDatabase.get_Username());
        success_OPEN = true;

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtLink = findViewById(R.id.txtLink);
        txtTitle.setText(search_SongTitle);
        txtLink.setText(song_ARTIST);

    }

}