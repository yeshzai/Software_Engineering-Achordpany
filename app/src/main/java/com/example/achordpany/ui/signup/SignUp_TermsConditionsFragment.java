package com.example.achordpany.ui.signup;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;

public class SignUp_TermsConditionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_termsconditions, container, false);

        // Button Functions
        view.findViewById(R.id.btnBack_SIGNUP_TERMSNCONDITION).setOnClickListener(v -> {

            ((SignUpActivity) requireActivity()).navigateToStep(4);

        });

        // Find the TextView
        TextView textView = view.findViewById(R.id.terms_text_SIGNUP_TERMSNCONDITION);

        // Set HTML text safely depending on Android version
        Spanned htmlText;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            htmlText = Html.fromHtml(getString(R.string.terms_and_conditions), Html.FROM_HTML_MODE_LEGACY);
        } else {
            htmlText = Html.fromHtml(getString(R.string.terms_and_conditions));
        }

        textView.setText(htmlText);

        return view;
    }
}
