package com.example.achordpany.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.TooltipCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.achordpany.ChordsWebView;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentProfileBinding;
import com.example.achordpany.ChordsRecommendations;
import com.example.achordpany.ui.home.HomeWebViewActivity;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    private ImageView profile_image;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "ProfileFragment is created");

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Data Insertion
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();

        String username = main_EverythingLocalDatabase.get_Username();
        String email = main_EverythingLocalDatabase.get_Email();
        String avatar_UID = main_EverythingLocalDatabase.get_AvatarUID();
        ArrayList<String> genre = main_EverythingLocalDatabase.get_Genre();
        get_SongRecommendations();

        binding.username.setText(username);
        binding.emailPlaceholder.setText(email);
        binding.genrePlaceholder1.setText(genre.get(0));
        binding.genrePlaceholder2.setText(genre.get(1));
        binding.genrePlaceholder3.setText(genre.get(2));
        Glide.with(this).load(avatar_UID).into(binding.profileImage);

        // Get the SharedViewModel instance
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Update the header title via SharedViewModel
        sharedViewModel.setTitle("Profile");
        sharedViewModel.setSubtext("Account section");

        TextView edit_username = binding.editUsername;
        edit_username.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditUsernameActivity.class);
            startActivity(intent);
        });

        TextView edit_email = binding.editEmail;
        edit_email.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditEmailActivity.class);
            startActivity(intent);
        });

        TextView edit_genre = binding.editGenre;
        edit_genre.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditGenreActivity.class);
            startActivity(intent);
        });

        binding.recommendations11ButtonOpen.setOnClickListener(v -> {
            open_Profile_WebView_Recommendations(
                    chordsRecommendations.get_RecommendationsGenre1Title().get(0),
                    chordsRecommendations.get_RecommendationsGenre1Artist().get(0),
                    chordsRecommendations.get_Genres().get(0),
                    chordsRecommendations.get_RecommendationsGenre1URL().get(0)
            );
        });

        binding.recommendations12ButtonOpen.setOnClickListener(v -> {
            open_Profile_WebView_Recommendations(
                    chordsRecommendations.get_RecommendationsGenre1Title().get(1),
                    chordsRecommendations.get_RecommendationsGenre1Artist().get(1),
                    chordsRecommendations.get_Genres().get(0),
                    chordsRecommendations.get_RecommendationsGenre1URL().get(1)
            );
        });

        binding.recommendations2ButtonOpen.setOnClickListener(v -> {
            open_Profile_WebView_Recommendations(
                    chordsRecommendations.get_RecommendationsGenre2Title().get(0),
                    chordsRecommendations.get_RecommendationsGenre2Artist().get(0),
                    chordsRecommendations.get_Genres().get(1),
                    chordsRecommendations.get_RecommendationsGenre2URL().get(0)
            );
        });

        binding.recommendations3ButtonOpen.setOnClickListener(v -> {
            open_Profile_WebView_Recommendations(
                    chordsRecommendations.get_RecommendationsGenre3Title().get(1),
                    chordsRecommendations.get_RecommendationsGenre3Artist().get(1),
                    chordsRecommendations.get_Genres().get(2),
                    chordsRecommendations.get_RecommendationsGenre3URL().get(1)
            );
        });

        //final TextView textView = binding.textProfile;
        //profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void get_SongRecommendations() {

        ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();

        String songTITLE_11 = chordsRecommendations.get_RecommendationsGenre1Title().get(0);
        String songTITLE_12 = chordsRecommendations.get_RecommendationsGenre1Title().get(1);
        String songTITLE_2 = chordsRecommendations.get_RecommendationsGenre2Title().get(0);
        String songTITLE_3 = chordsRecommendations.get_RecommendationsGenre3Title().get(0);

        String songARTIST_11 = chordsRecommendations.get_RecommendationsGenre1Artist().get(0);
        String songARTIST_12 = chordsRecommendations.get_RecommendationsGenre1Artist().get(1);
        String songARTIST_2 = chordsRecommendations.get_RecommendationsGenre2Artist().get(0);
        String songARTIST_3 = chordsRecommendations.get_RecommendationsGenre3Artist().get(0);

        String genre1 = chordsRecommendations.get_Genres().get(0);
        String genre2 = chordsRecommendations.get_Genres().get(1);
        String genre3 = chordsRecommendations.get_Genres().get(2);

        binding.recommendations11.setText(songTITLE_11);
        binding.recommendations12.setText(songTITLE_12);
        binding.recommendations2.setText(songTITLE_2);
        binding.recommendations3.setText(songTITLE_3);

        binding.recommendations11Artist.setText(songARTIST_11);
        binding.recommendations12Artist.setText(songARTIST_12);
        binding.recommendations2Artist.setText(songARTIST_2);
        binding.recommendations3Artist.setText(songARTIST_3);

        binding.recommendations11Genre.setText(genre1);
        binding.recommendations12Genre.setText(genre1);
        binding.recommendations2Genre.setText(genre2);
        binding.recommendations3Genre.setText(genre3);

        // Set tooltip to show full text on long press
        TooltipCompat.setTooltipText(binding.recommendations11, songTITLE_11);
        TooltipCompat.setTooltipText(binding.recommendations12, songTITLE_12);
        TooltipCompat.setTooltipText(binding.recommendations2, songTITLE_2);
        TooltipCompat.setTooltipText(binding.recommendations3, songTITLE_3);

    }

    private void open_Profile_WebView_Recommendations(String use_Title, String use_Artist, String use_Genre, String use_URL) {

        ChordsWebView chordsWebView = ChordsWebView.getInstance();
        chordsWebView.set_Title(use_Title);
        chordsWebView.set_Artist(use_Artist);
        chordsWebView.set_Genre(use_Genre);
        chordsWebView.set_Url(use_URL);

        Intent intent = new Intent(requireActivity(), ProfileWebViewActivity.class);
        startActivity(intent);

    }

}
