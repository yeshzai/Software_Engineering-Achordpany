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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.ChordsRecommendations;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentHomeBinding;
import com.example.achordpany.ui.history.HistoryFragment;

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
    ArrayList<String> recentIsBookmarked;
    String artist_genre;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[FRAGMENT]", "[CREATED] HOME FRAGMENT");

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Home Page Functions
        recentTitle = chordsSearchedHistory.get_Title();
        recentArtist = chordsSearchedHistory.get_Artist();
        recentGenre = chordsSearchedHistory.get_Genre();
        recentSite = chordsSearchedHistory.get_Site();
        recentURL = chordsSearchedHistory.get_URL();
        recentIsBookmarked = chordsSearchedHistory.get_isBookmarked();

        load_Bookmarks();
        load_RecentSearches();
        load_Recommendations();

        binding.recentSearch1BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(1, recentIsBookmarked.size() - 1); });
        binding.recentSearch2BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(2, recentIsBookmarked.size() - 2); });
        binding.recentSearchesOpenAllButton.setOnClickListener(v -> {

            // Code here when OpenAll for recent search is clicked.

        });
        binding.bookmarksOpenAllButton.setOnClickListener(v -> {

            // Code here when OpenAll for bookmark is clicked.

        });

        // Home Page Functions

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

    private void historyBookmarks_Function(int whichBoard, int whichHistoryIndex) {

        int bookmark_status_icon = 0;

        switch (recentIsBookmarked.get(whichHistoryIndex)) {
            case "false":
                bookmark_status_icon = R.drawable.ic_bookmark_active;
                recentIsBookmarked.set(whichHistoryIndex, "true");
                break;
            case "true":
                bookmark_status_icon = R.drawable.ic_bookmark;
                recentIsBookmarked.set(whichHistoryIndex, "false");
                break;
        }

        switch (whichBoard) {
            case 1:
                binding.recentSearch1BookmarkButton.setImageResource(bookmark_status_icon);
                break;
            case 2:
                binding.recentSearch2BookmarkButton.setImageResource(bookmark_status_icon);
                break;
        }

    }

    public void load_Bookmarks() {



    }

    public void load_RecentSearches() {

        // Upper Board - history.get(history.size() - 1) | Latest Added
        // Lower Board - history.get(history.size() - 2) | Second Latest Added

        binding.recentSearchBoard1.setVisibility(!recentTitle.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        binding.recentSearchBoard2.setVisibility(recentTitle.size() > 1 ? View.VISIBLE : View.INVISIBLE);

        if(!recentTitle.isEmpty()) {

            artist_genre = recentArtist.get(recentArtist.size() - 1) + " - " + recentGenre.get(recentArtist.size() - 1);
            binding.recentSearch1Title.setText(recentTitle.get(recentTitle.size() - 1));
            binding.recentSearch1ArtistGenre.setText(artist_genre);
            binding.recentSearch1Site.setText(recentSite.get(recentSite.size() - 1));

            binding.recentSearch1BookmarkButton.setImageResource((recentIsBookmarked.get(recentIsBookmarked.size() - 1).equals("false"))
                    ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active);

            if(recentTitle.size() > 1) {

                artist_genre = recentArtist.get(recentArtist.size() - 2) + " - " + recentGenre.get(recentGenre.size() - 2);
                binding.recentSearch2Title.setText(recentTitle.get(recentTitle.size() - 2));
                binding.recentSearch2ArtistGenre.setText(artist_genre);
                binding.recentSearch2Site.setText(recentSite.get(recentSite.size() - 2));

                binding.recentSearch2BookmarkButton.setImageResource((recentIsBookmarked.get(recentIsBookmarked.size() - 2).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active);

            }

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