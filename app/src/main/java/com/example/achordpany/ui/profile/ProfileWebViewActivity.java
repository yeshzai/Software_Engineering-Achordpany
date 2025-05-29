package com.example.achordpany.ui.profile;

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

public class ProfileWebViewActivity extends AppCompatActivity {

    ChordsWebView chordsWebView;
    private ImageButton btnBack_Profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_webview);

        chordsWebView = ChordsWebView.getInstance();
        TextView txtTitle_Profile = findViewById(R.id.txtTitle_Profile);
        TextView txtLink_Profile = findViewById(R.id.txtLink_Profile);
        btnBack_Profile = findViewById(R.id.btnBack_Profile);

        String song_Title = chordsWebView.get_Title();
        String song_URL = chordsWebView.get_Url();

        // Handle Back Button Click
        btnBack_Profile.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(ProfileWebViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        WebView webView_Profile = findViewById(R.id.webView_Profile);
        WebSettings webSettings = webView_Profile.getSettings();

        // Securely Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);  // Prevents file-based XSS attacks
        webSettings.setAllowContentAccess(false); // Blocks unsafe content access
        webSettings.setDomStorageEnabled(true);  // Enables local storage for modern sites
        webSettings.setBlockNetworkLoads(false); // Allows network requests, but only to trusted URLs
        webSettings.setBlockNetworkImage(false); // Allows image loading

        webView_Profile.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if(url.startsWith("https://"))
                    view.loadUrl(url);
                return true;
            }
        });

        webView_Profile.setWebChromeClient(new WebChromeClient());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        txtTitle_Profile.setText(song_Title);
        txtLink_Profile.setText(song_URL);
        webView_Profile.loadUrl(song_URL);

    }
}
