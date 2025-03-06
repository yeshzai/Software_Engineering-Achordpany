package com.example.achordpany.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;

import androidx.appcompat.widget.TooltipCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentProfileBinding;
import com.example.achordpany.ChordsRecommendations;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    private ImageView profile_image;
    public String songRecommendation_TITLE1_1;
    public String songRecommendation_ARTIST1_1;
    public String songRecommendation_TITLE1_2;
    public String songRecommendation_ARTIST1_2;
    public String songRecommendation_TITLE2;
    public String songRecommendation_ARTIST2;
    public String songRecommendation_TITLE3;
    public String songRecommendation_ARTIST3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "ProfileFragment is created");

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Data Insertion
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
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

        String[] songTITLEARTIST_Genre1_1 = chordsRecommendations.get_RecommendationsGenre1().get(0).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre1_2 = chordsRecommendations.get_RecommendationsGenre1().get(1).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre2 = chordsRecommendations.get_RecommendationsGenre2().get(0).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre3 = chordsRecommendations.get_RecommendationsGenre3().get(0).split("\\|\\|\\|\\|\\|");
        String genre1 = chordsRecommendations.get_Genres().get(0);
        String genre2 = chordsRecommendations.get_Genres().get(1);
        String genre3 = chordsRecommendations.get_Genres().get(2);

        songRecommendation_TITLE1_1 = songTITLEARTIST_Genre1_1[0];
        songRecommendation_ARTIST1_1 = songTITLEARTIST_Genre1_1.length > 1 ? songTITLEARTIST_Genre1_1[1] : ""; // Avoid index errors
        songRecommendation_TITLE1_2 = songTITLEARTIST_Genre1_2[0];
        songRecommendation_ARTIST1_2 = songTITLEARTIST_Genre1_2.length > 1 ? songTITLEARTIST_Genre1_2[1] : ""; // Avoid index errors
        songRecommendation_TITLE2 = songTITLEARTIST_Genre2[0];
        songRecommendation_ARTIST2 = songTITLEARTIST_Genre2.length > 1 ? songTITLEARTIST_Genre2[1] : ""; // Avoid index errors
        songRecommendation_TITLE3 = songTITLEARTIST_Genre3[0];
        songRecommendation_ARTIST3 = songTITLEARTIST_Genre3.length > 1 ? songTITLEARTIST_Genre3[1] : ""; // Avoid index errors

        binding.recommendations11.setText(songRecommendation_TITLE1_1);
        binding.recommendations11Artist.setText(songRecommendation_ARTIST1_1);
        binding.recommendations11Genre.setText(genre1);

        binding.recommendations12.setText(songRecommendation_TITLE1_2);
        binding.recommendations12Artist.setText(songRecommendation_ARTIST1_2);
        binding.recommendations12Genre.setText(genre1);

        binding.recommendations2.setText(songRecommendation_TITLE2);
        binding.recommendations2Artist.setText(songRecommendation_ARTIST2);
        binding.recommendations2Genre.setText(genre2);

        binding.recommendations3.setText(songRecommendation_TITLE3);
        binding.recommendations3Artist.setText(songRecommendation_ARTIST3);
        binding.recommendations3Genre.setText(genre3);

        // Set tooltip to show full text on long press
        TooltipCompat.setTooltipText(binding.recommendations11, songRecommendation_TITLE1_1);
        TooltipCompat.setTooltipText(binding.recommendations12, songRecommendation_TITLE1_2);
        TooltipCompat.setTooltipText(binding.recommendations2, songRecommendation_TITLE2);
        TooltipCompat.setTooltipText(binding.recommendations3, songRecommendation_TITLE3);
    }

}
