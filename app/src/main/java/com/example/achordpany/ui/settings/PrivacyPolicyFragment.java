package com.example.achordpany.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.databinding.FragmentSearchBinding;
public class PrivacyPolicyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacypolicy, container, false);

        // Find the TextView
        TextView textView = view.findViewById(R.id.policy_text);

        // Set HTML text safely depending on Android version
        Spanned htmlText;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            htmlText = Html.fromHtml(getString(R.string.privacy_policy), Html.FROM_HTML_MODE_LEGACY);
        } else {
            htmlText = Html.fromHtml(getString(R.string.privacy_policy));
        }

        textView.setText(htmlText);

        return view;
    }
}
