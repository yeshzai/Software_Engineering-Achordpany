package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.LoginActivity;

import java.util.ArrayList;

public class SignUpStep3Fragment extends Fragment {

    private Button genreButton_Rock;
    private Button genreButton_Blues;
    private Button genreButton_Jazz;
    private Button genreButton_Classical;
    private Button genreButton_Pop;
    private Button genreButton_Reggae;

    private ArrayList<String> genres;
    private boolean genreButton_Rock_Clicked = false;
    private boolean genreButton_Blues_Clicked = false;
    private boolean genreButton_Jazz_Clicked = false;
    private boolean genreButton_Classical_Clicked = false;
    private boolean genreButton_Pop_Clicked = false;
    private boolean genreButton_Reggae_Clicked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_3, container, false);

        genreButton_Rock = view.findViewById(R.id.genreButton_Rock);
        genreButton_Blues = view.findViewById(R.id.genreButton_Blues);
        genreButton_Jazz = view.findViewById(R.id.genreButton_Jazz);
        genreButton_Classical = view.findViewById(R.id.genreButton_Classical);
        genreButton_Pop = view.findViewById(R.id.genreButton_Pop);
        genreButton_Reggae = view.findViewById(R.id.genreButton_Reggae);
        backtrackContent();

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {

            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();

        });

        genreButton_Rock.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Rock")) {

                if(!genreButton_Rock_Clicked) {
                    genres.add("Rock");
                    genreButton_Rock.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Rock");
                    genreButton_Rock.setBackgroundColor(Color.WHITE);
                }
                genreButton_Rock_Clicked = !genreButton_Rock_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        genreButton_Blues.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Blues")) {

                if(!genreButton_Blues_Clicked) {
                    genres.add("Blues");
                    genreButton_Blues.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Blues");
                    genreButton_Blues.setBackgroundColor(Color.WHITE);
                }
                genreButton_Blues_Clicked = !genreButton_Blues_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        genreButton_Jazz.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Jazz")) {

                if(!genreButton_Jazz_Clicked) {
                    genres.add("Jazz");
                    genreButton_Jazz.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Jazz");
                    genreButton_Jazz.setBackgroundColor(Color.WHITE);
                }
                genreButton_Jazz_Clicked = !genreButton_Jazz_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        genreButton_Classical.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Classical")) {

                if(!genreButton_Classical_Clicked) {
                    genres.add("Classical");
                    genreButton_Classical.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Classical");
                    genreButton_Classical.setBackgroundColor(Color.WHITE);
                }
                genreButton_Classical_Clicked = !genreButton_Classical_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        genreButton_Pop.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Pop")) {

                if(!genreButton_Pop_Clicked) {
                    genres.add("Pop");
                    genreButton_Pop.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Pop");
                    genreButton_Pop.setBackgroundColor(Color.WHITE);
                }
                genreButton_Pop_Clicked = !genreButton_Pop_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        genreButton_Reggae.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Reggae")) {

                if(!genreButton_Reggae_Clicked) {
                    genres.add("Reggae");
                    genreButton_Reggae.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Reggae");
                    genreButton_Reggae.setBackgroundColor(Color.WHITE);
                }
                genreButton_Reggae_Clicked = !genreButton_Reggae_Clicked;

            } else {
                Toast.makeText(getContext(), "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {

            if(genres.size() == 3) {
                SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
                signUpCredentials.set_credential_genre(genres);
                Log.d("SignUpCredentials Genre", signUpCredentials.get_credential_genre().toString());

                ((SignUpActivity) requireActivity()).navigateToStep(4);
            } else {
                Toast.makeText(getContext(), "Please Select 3 Genres!", Toast.LENGTH_SHORT).show();
            }

        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(2);
        });

        return view;

    }

    private void backtrackContent() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        ArrayList<String> backtrack_genre = signUpCredentials.get_credential_genre();
        genres = signUpCredentials.get_credential_genre();

        for(String i : backtrack_genre) {
            switch(i) {
                case "Rock":
                    genreButton_Rock_Clicked = true;
                    genreButton_Rock.setBackgroundColor(Color.GREEN);
                    break;
                case "Blues":
                    genreButton_Blues_Clicked = true;
                    genreButton_Blues.setBackgroundColor(Color.GREEN);
                    break;
                case "Jazz":
                    genreButton_Jazz_Clicked = true;
                    genreButton_Jazz.setBackgroundColor(Color.GREEN);
                    break;
                case "Classical":
                    genreButton_Classical_Clicked = true;
                    genreButton_Classical.setBackgroundColor(Color.GREEN);
                    break;
                case "Pop":
                    genreButton_Pop_Clicked = true;
                    genreButton_Pop.setBackgroundColor(Color.GREEN);
                    break;
                case "Reggae":
                    genreButton_Reggae_Clicked = true;
                    genreButton_Reggae.setBackgroundColor(Color.GREEN);
                    break;
            }
        }

    }

    private void resetSignUpCredentials() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        signUpCredentials.set_credential_usernameText("");
        signUpCredentials.set_credential_emailAddressText("");
        signUpCredentials.set_credential_passwordText("");
        signUpCredentials.set_credential_confirmPasswordText("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

}
