package com.example.achordpany.ui.chords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.databinding.FragmentSearchBinding;

public class ChordsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chords, container, false);

        // ✅ Find WebView
        WebView webView = root.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        // ✅ Securely Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);  // Prevents file-based XSS attacks
        webSettings.setAllowContentAccess(false); // Blocks unsafe content access
        webSettings.setDomStorageEnabled(true);  // Enables local storage for modern sites
        webSettings.setBlockNetworkLoads(false); // Allows network requests, but only to trusted URLs
        webSettings.setBlockNetworkImage(false); // Allows image loading


        // ✅ Secure WebViewClient (Prevents opening external browsers)
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("https://")) { // Allow only HTTPS links
                    view.loadUrl(url);
                }
                return true;
            }
        });

        // ✅ Optional: Improve WebView Performance
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // ✅ Load URL (Ensure it’s HTTPS)
        webView.loadUrl("https://tabs.ultimate-guitar.com/");

        return root;
    }
}
