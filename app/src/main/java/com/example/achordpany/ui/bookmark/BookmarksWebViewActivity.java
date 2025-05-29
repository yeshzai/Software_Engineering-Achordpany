package com.example.achordpany.ui.bookmark;

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

public class BookmarksWebViewActivity extends AppCompatActivity {

    ChordsWebView chordsWebView;
    private ImageButton btnBack_Bookmarks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_webview);

        chordsWebView = ChordsWebView.getInstance();
        TextView txtTitle_Bookmarks = findViewById(R.id.txtTitle_Bookmarks);
        TextView txtLink_Bookmarks = findViewById(R.id.txtLink_Bookmarks);
        btnBack_Bookmarks = findViewById(R.id.btnBack_Bookmarks);

        String song_Title = chordsWebView.get_Title();
        String song_URL = chordsWebView.get_Url();

        // Handle Back Button Click
        btnBack_Bookmarks.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(BookmarksWebViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        WebView webView_Bookmarks = findViewById(R.id.webView_Bookmarks);
        WebSettings webSettings = webView_Bookmarks.getSettings();

        // Securely Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);  // Prevents file-based XSS attacks
        webSettings.setAllowContentAccess(false); // Blocks unsafe content access
        webSettings.setDomStorageEnabled(true);  // Enables local storage for modern sites
        webSettings.setBlockNetworkLoads(false); // Allows network requests, but only to trusted URLs
        webSettings.setBlockNetworkImage(false); // Allows image loading

        webView_Bookmarks.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if(url.startsWith("https://"))
                    view.loadUrl(url);
                return true;
            }
        });

        webView_Bookmarks.setWebChromeClient(new WebChromeClient());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        txtTitle_Bookmarks.setText(song_Title);
        txtLink_Bookmarks.setText(song_URL);
        webView_Bookmarks.loadUrl(song_URL);

    }
}
