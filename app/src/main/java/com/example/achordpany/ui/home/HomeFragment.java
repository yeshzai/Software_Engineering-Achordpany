package com.example.achordpany.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.ChordsRecommendations;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
    ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();
    ChordsSearchedHistory chordsSearchedHistory = ChordsSearchedHistory.getInstance();

    private FragmentHomeBinding binding;
    ArrayList<String> recentTitle;
    ArrayList<String> recentArtist;
    ArrayList<String> recentGenre;
    ArrayList<String> recentSite;
    ArrayList<String> recentURL;
    String artist_genre;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[FRAGMENT]", "[CREATED] HOME FRAGMENT");

        load_Bookmarks();
        load_RecentSearches();
        load_Recommendations();

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the SharedViewModel instance
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Update the header title via SharedViewModel
        sharedViewModel.setTitle("Dashboard");
        sharedViewModel.setSubtext("Navigation section");

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void load_Bookmarks() {



    }

    public void load_RecentSearches() {

        //  Note: Load only 2 items in the List
        /*  ALGORITHM:
            Upper Board - history.get(history.size() - 1) | Latest Added
            Lower Board - history.get(history.size() - 2) | Second Latest Added
        */

        recentTitle = chordsSearchedHistory.get_Title();
        recentArtist = chordsSearchedHistory.get_Artist();
        recentGenre = chordsSearchedHistory.get_Genre();
        recentSite = chordsSearchedHistory.get_Site();
        recentURL = chordsSearchedHistory.get_URL();

        if(recentTitle.isEmpty()) {

            binding.recentSearchBoard1.setVisibility(View.INVISIBLE);
            binding.recentSearchBoard2.setVisibility(View.INVISIBLE);

        } else if (recentTitle.size() == 1) {

            binding.recentSearchBoard1.setVisibility(View.VISIBLE);
            binding.recentSearchBoard2.setVisibility(View.INVISIBLE);

            artist_genre = recentArtist.get(recentArtist.size() - 1) + " - " + recentGenre.get(recentArtist.size() - 1);
            binding.recentSearch1Title.setText(recentTitle.get(recentTitle.size() - 1));
            binding.recentSearch1ArtistGenre.setText(artist_genre);
            binding.recentSearch1Site.setText(recentSite.get(recentSite.size() - 1));

        } else {

            binding.recentSearchBoard1.setVisibility(View.VISIBLE);
            binding.recentSearchBoard2.setVisibility(View.VISIBLE);

            artist_genre = recentArtist.get(recentArtist.size() - 1) + " - " + recentGenre.get(recentGenre.size() - 1);
            binding.recentSearch1Title.setText(recentTitle.get(recentTitle.size() - 1));
            binding.recentSearch1ArtistGenre.setText(artist_genre);
            binding.recentSearch1Site.setText(recentSite.get(recentSite.size() - 1));

            artist_genre = recentArtist.get(recentArtist.size() - 2) + " - " + recentGenre.get(recentGenre.size() - 2);
            binding.recentSearch2Title.setText(recentTitle.get(recentTitle.size() - 2));
            binding.recentSearch2ArtistGenre.setText(artist_genre);
            binding.recentSearch2Site.setText(recentSite.get(recentSite.size() - 2));

        }

    }

    public void load_Recommendations() {

        // Recommendation Title and Artist
        if(chordsRecommendations.get_RecommendationsGenre1().isEmpty()) {

            binding.recommendations1Board.setVisibility(View.INVISIBLE);
            binding.recommendations1Board.setVisibility(View.INVISIBLE);

        } else if (chordsRecommendations.get_RecommendationsGenre1().size() == 1) {

            binding.recommendations1Board.setVisibility(View.VISIBLE);
            binding.recommendations2Board.setVisibility(View.INVISIBLE);

            String[] recommendation_1 = chordsRecommendations.get_RecommendationsGenre1().get(0).split("\\|\\|\\|\\|\\|");
            binding.recommendations1Title.setText(recommendation_1[0]);
            binding.recommendations1Artist.setText(recommendation_1[1]);
            binding.recommendations1Genre.setText(chordsRecommendations.get_Genres().get(0));

        } else {

            binding.recommendations1Board.setVisibility(View.VISIBLE);
            binding.recommendations2Board.setVisibility(View.VISIBLE);

            String[] recommendation_1 = chordsRecommendations.get_RecommendationsGenre1().get(0).split("\\|\\|\\|\\|\\|");
            binding.recommendations1Title.setText(recommendation_1[0]);
            binding.recommendations1Artist.setText(recommendation_1[1]);
            binding.recommendations1Genre.setText(chordsRecommendations.get_Genres().get(0));

            String[] recommendation_2 = chordsRecommendations.get_RecommendationsGenre2().get(0).split("\\|\\|\\|\\|\\|");
            binding.recommendations2Title.setText(recommendation_2[0]);
            binding.recommendations2Artist.setText(recommendation_2[1]);
            binding.recommendations2Genre.setText(chordsRecommendations.get_Genres().get(1));

        }

    }

}