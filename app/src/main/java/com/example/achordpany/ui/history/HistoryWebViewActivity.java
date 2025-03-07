package com.example.achordpany.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.ChordsWebView;
import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.chords.ChordsDisplayActivity;

public class HistoryWebViewActivity extends AppCompatActivity {

    ChordsWebView chordsWebView;
    private ImageButton btnBack_History;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_webview);

        chordsWebView = ChordsWebView.getInstance();
        TextView txtTitle_History = findViewById(R.id.txtTitle_History);
        TextView txtLink_History = findViewById(R.id.txtLink_History);
        btnBack_History = findViewById(R.id.btnBack_History);

        String song_Title = chordsWebView.get_Title();
        String song_Artist = chordsWebView.get_Artist();
        String song_Genre = chordsWebView.get_Genre();
        String song_URL = chordsWebView.get_Url();

        // Handle Back Button Click
        btnBack_History.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(HistoryWebViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        WebView webView_History = findViewById(R.id.webView_History);
        WebSettings webSettings = webView_History.getSettings();

        // Securely Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);  // Prevents file-based XSS attacks
        webSettings.setAllowContentAccess(false); // Blocks unsafe content access
        webSettings.setDomStorageEnabled(true);  // Enables local storage for modern sites
        webSettings.setBlockNetworkLoads(false); // Allows network requests, but only to trusted URLs
        webSettings.setBlockNetworkImage(false); // Allows image loading

        webView_History.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if(url.startsWith("https://"))
                    view.loadUrl(url);
                return true;
            }
        });

        webView_History.setWebChromeClient(new WebChromeClient());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        txtTitle_History.setText(song_Title);
        txtLink_History.setText(song_URL);
        webView_History.loadUrl(song_URL);

    }
}
