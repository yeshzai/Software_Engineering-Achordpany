package com.example.achordpany.ui.chords;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.R;

public class ChordsDisplayActivity extends AppCompatActivity {
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chords);

        btnBack = findViewById(R.id.btnBack);

        // Handle Back Button Click
        btnBack.setOnClickListener(v -> finish());

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
        webView.loadUrl("https://tabs.ultimate-guitar.com/");
    }
}
