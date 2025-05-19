package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.LoginActivity;

import java.util.ArrayList;

public class SignUpStep3Fragment extends Fragment {

    private Button genreButton_Rock, genreButton_Blues, genreButton_Jazz,
            genreButton_Classical, genreButton_Pop, genreButton_Reggae;

    private ArrayList<String> genres;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_3, container, false);

        // Initialize buttons
        genreButton_Rock = view.findViewById(R.id.genreButton_Rock);
        genreButton_Blues = view.findViewById(R.id.genreButton_Blues);
        genreButton_Jazz = view.findViewById(R.id.genreButton_Jazz);
        genreButton_Classical = view.findViewById(R.id.genreButton_Classical);
        genreButton_Pop = view.findViewById(R.id.genreButton_Pop);
        genreButton_Reggae = view.findViewById(R.id.genreButton_Reggae);

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        genres = signUpCredentials.get_credential_genre();
        if (genres == null) genres = new ArrayList<>();

        // Apply any previously selected genres
        backtrackContent();

        // Setup listeners
        setupGenreButton(genreButton_Rock, "Rock");
        setupGenreButton(genreButton_Blues, "Blues");
        setupGenreButton(genreButton_Jazz, "Jazz");
        setupGenreButton(genreButton_Classical, "Classical");
        setupGenreButton(genreButton_Pop, "Pop");
        setupGenreButton(genreButton_Reggae, "Reggae");

        // Continue button
        view.findViewById(R.id.btnContinue).setOnClickListener(v -> {
            if (genres.size() == 3) {
                signUpCredentials.set_credential_genre(genres);
                Log.d("SignUpCredentials Genre", genres.toString());
                ((SignUpActivity) requireActivity()).navigateToStep(4);
            } else {
                Toast.makeText(getContext(), "Please Select 3 Genres!", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to login
        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {
            resetSignUpCredentials();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(2);
        });

        return view;
    }

    private void setupGenreButton(Button button, String genreName) {
        button.setOnClickListener(v -> {
            if (genres.size() != 3 || genres.contains(genreName)) {
                /*if (!button.isSelected()) {
                    genres.add(genreName);
                    button.setSelected(true);
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853"))); // green
                    button.setTextColor(Color.WHITE);
                } else {
                    genres.remove(genreName);
                    button.setSelected(false);
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF"))); // white
                    button.setTextColor(Color.BLACK);
                }*/

                if (!button.isSelected()) {
                    genres.add(genreName);
                    button.setSelected(true);
                    button.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), R.color.genre_selected_bg)
                    ));
                    button.setTextColor(ContextCompat.getColor(requireContext(), R.color.genre_selected_text));
                } else {
                    genres.remove(genreName);
                    button.setSelected(false);
                    button.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), R.color.genre_unselected_bg)
                    ));
                    button.setTextColor(ContextCompat.getColor(requireContext(), R.color.genre_unselected_text));
                }
            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*private void setupGenreButton(Button button, String genreName) {
        button.setOnClickListener(v -> {
            if (genres.contains(genreName)) {
                genres.remove(genreName);
                button.setSelected(false);
            } else if (genres.size() < 3) {
                genres.add(genreName);
                button.setSelected(true);
            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void backtrackContent() {
        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        ArrayList<String> backtrack_genre = signUpCredentials.get_credential_genre();
        genres = backtrack_genre == null ? new ArrayList<>() : new ArrayList<>(backtrack_genre);

        if (backtrack_genre == null) return;

        for (String genre : backtrack_genre) {
            switch (genre) {
                case "Rock":
                    genreButton_Rock.setSelected(true);
                    genreButton_Rock.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Rock.setTextColor(Color.WHITE);
                    break;
                case "Blues":
                    genreButton_Blues.setSelected(true);
                    genreButton_Blues.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Blues.setTextColor(Color.WHITE);
                    break;
                case "Jazz":
                    genreButton_Jazz.setSelected(true);
                    genreButton_Jazz.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Jazz.setTextColor(Color.WHITE);
                    break;
                case "Classical":
                    genreButton_Classical.setSelected(true);
                    genreButton_Classical.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Classical.setTextColor(Color.WHITE);
                    break;
                case "Pop":
                    genreButton_Pop.setSelected(true);
                    genreButton_Pop.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Pop.setTextColor(Color.WHITE);
                    break;
                case "Reggae":
                    genreButton_Reggae.setSelected(true);
                    genreButton_Reggae.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853")));
                    genreButton_Reggae.setTextColor(Color.WHITE);
                    break;
            }
        }
    }

    private void resetSignUpCredentials() {
        SignUpCredentials creds = SignUpCredentials.getInstance();
        creds.set_credential_usernameText("");
        creds.set_credential_emailAddressText("");
        creds.set_credential_passwordText("");
        creds.set_credential_confirmPasswordText("");
        creds.set_credential_avatarUID("");
        creds.set_credential_genre(new ArrayList<>());
    }
}
