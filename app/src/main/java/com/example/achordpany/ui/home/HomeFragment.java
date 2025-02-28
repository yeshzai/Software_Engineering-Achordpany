package com.example.achordpany.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentHomeBinding;
import com.example.achordpany.ui.chords.ChordsSearchedHistory;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ArrayList<String> recentTitle;
    ArrayList<String> recentArtist;
    ArrayList<String> recentGenre;
    ArrayList<String> recentSite;
    ArrayList<String> recentTime;
    ArrayList<String> recentURL;
    String artist_genre;
    String site_time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "HomeFragment is created");

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        ArrayList<String> genre_list = main_EverythingLocalDatabase.get_Genre();

        initialize_SongRecommendations(genre_list.get(0), genre_list.get(1), genre_list.get(2));
        load_Bookmarks();
        load_RecentSearches();
        load_Recommendations();

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

        // Note: Load only 2 items in the List
        ChordsSearchedHistory chordsSearchedHistory = ChordsSearchedHistory.getInstance();
        recentTitle = chordsSearchedHistory.get_Title();
        recentArtist = chordsSearchedHistory.get_Artist();
        recentGenre = chordsSearchedHistory.get_Genre();
        recentSite = chordsSearchedHistory.get_Site();
        recentTime = chordsSearchedHistory.get_Time();
        recentURL = chordsSearchedHistory.get_URL();

        if(recentTitle.isEmpty()) {

            binding.recentSearchBoard1.setVisibility(View.INVISIBLE);
            binding.recentSearchBoard2.setVisibility(View.INVISIBLE);

        } else if (recentTitle.size() == 1) {

            binding.recentSearchBoard1.setVisibility(View.VISIBLE);
            binding.recentSearchBoard2.setVisibility(View.INVISIBLE);

            artist_genre = recentArtist.get(0) + " - " + recentGenre.get(0);
            site_time = recentSite.get(0) + " | " + recentTime.get(0);
            binding.recentSearch1Title.setText(recentTitle.get(0));
            binding.recentSearch1ArtistGenre.setText(artist_genre);
            binding.recentSearch1SiteTime.setText(site_time);

        } else {

            binding.recentSearchBoard1.setVisibility(View.VISIBLE);
            binding.recentSearchBoard2.setVisibility(View.VISIBLE);

            artist_genre = recentArtist.get(0) + " - " + recentGenre.get(0);
            site_time = recentSite.get(0) + " | " + recentTime.get(0);
            binding.recentSearch1Title.setText(recentTitle.get(0));
            binding.recentSearch1ArtistGenre.setText(artist_genre);
            binding.recentSearch1SiteTime.setText(site_time);

            artist_genre = recentArtist.get(1) + " - " + recentGenre.get(1);
            site_time = recentSite.get(1) + " | " + recentTime.get(1);
            binding.recentSearch2Title.setText(recentTitle.get(1));
            binding.recentSearch2ArtistGenre.setText(artist_genre);
            binding.recentSearch2SiteTime.setText(site_time);

        }

    }

    public void load_Recommendations() {

        ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();

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

    private void initialize_SongRecommendations(String genre1, String genre2, String genre3) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_recommendations");
        if(pyModule == null) {
            Log.d("SONG RECOMMENDATIONS", "Python Module Not Found");
            return;
        }

        PyObject pyObjectResultGenre1 = pyModule.callAttr("get_songs_by_genre", genre1);
        PyObject pyObjectResultGenre2 = pyModule.callAttr("get_songs_by_genre", genre2);
        PyObject pyObjectResultGenre3 = pyModule.callAttr("get_songs_by_genre", genre3);
        if(pyObjectResultGenre1 == null || pyObjectResultGenre1.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [1]", "Python Module Not Found");
            return;
        }
        if(pyObjectResultGenre2 == null || pyObjectResultGenre2.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [2]", "Python Module Not Found");
            return;
        }
        if(pyObjectResultGenre3 == null || pyObjectResultGenre3.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [3]", "Python Module Not Found");
            return;
        }

        ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();
        List<PyObject> pyListGenre1 = pyObjectResultGenre1.asList();
        List<PyObject> pyListGenre2 = pyObjectResultGenre2.asList();
        List<PyObject> pyListGenre3 = pyObjectResultGenre3.asList();

        for(PyObject obj : pyListGenre1) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            chordsRecommendations.set_RecommendationsGenre1(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        for(PyObject obj : pyListGenre2) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            chordsRecommendations.set_RecommendationsGenre2(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        for(PyObject obj : pyListGenre3) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            chordsRecommendations.set_RecommendationsGenre3(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        chordsRecommendations.set_Genres(genre1);
        chordsRecommendations.set_Genres(genre2);
        chordsRecommendations.set_Genres(genre3);

    }

}