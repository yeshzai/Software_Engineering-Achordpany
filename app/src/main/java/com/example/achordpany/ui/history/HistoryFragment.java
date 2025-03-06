package com.example.achordpany.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.R;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentHistoryBinding;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    ArrayList<String> recentTitle;
    ArrayList<String> recentArtist;
    ArrayList<String> recentGenre;
    ArrayList<String> recentSite;
    ArrayList<String> recentURL;
    ArrayList<String> recentIsBookmarked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[HISTORY FRAGMENT]", "[CREATED] HISTORY FRAGMENT");

        HistoryViewModel historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        // Page Functions
        ChordsSearchedHistory chordsSearchedHistory = ChordsSearchedHistory.getInstance();

        recentTitle = chordsSearchedHistory.get_Title();
        recentArtist = chordsSearchedHistory.get_Artist();
        recentGenre = chordsSearchedHistory.get_Genre();
        recentSite = chordsSearchedHistory.get_Site();
        recentURL = chordsSearchedHistory.get_URL();
        recentIsBookmarked = chordsSearchedHistory.get_isBookmarked();

        load_HistoryFragment();

        binding.historyBoard1BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(1, recentIsBookmarked.size() - 1); } );
        binding.historyBoard2BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(2, recentIsBookmarked.size() - 2); } );
        binding.historyBoard3BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(3, recentIsBookmarked.size() - 3); } );
        binding.historyBoard4BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(4, recentIsBookmarked.size() - 4); } );
        binding.historyBoard5BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(5, recentIsBookmarked.size() - 5); } );
        // Page Functions

        View root = binding.getRoot();

        // Get the SharedViewModel instance
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Update the header title via SharedViewModel
        sharedViewModel.setTitle("History");
        sharedViewModel.setSubtext("Recent searches");

        //final TextView textView = binding.textHistory;
        //historyViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
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
                binding.historyBoard1BookmarkButton.setImageResource(bookmark_status_icon);
                break;
            case 2:
                binding.historyBoard2BookmarkButton.setImageResource(bookmark_status_icon);
                break;
            case 3:
                binding.historyBoard3BookmarkButton.setImageResource(bookmark_status_icon);
                break;
            case 4:
                binding.historyBoard4BookmarkButton.setImageResource(bookmark_status_icon);
                break;
            case 5:
                binding.historyBoard5BookmarkButton.setImageResource(bookmark_status_icon);
                break;
        }

    }

    private void load_HistoryFragment() {

        binding.historyBoard1.setVisibility(!recentTitle.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        binding.historyBoard2.setVisibility(recentTitle.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        binding.historyBoard3.setVisibility(recentTitle.size() > 2 ? View.VISIBLE : View.INVISIBLE);
        binding.historyBoard4.setVisibility(recentTitle.size() > 3 ? View.VISIBLE : View.INVISIBLE);
        binding.historyBoard5.setVisibility(recentTitle.size() > 4 ? View.VISIBLE : View.INVISIBLE);
        int historySize = recentTitle.size();

        if(historySize > 0)
            updater_HistoryFragment(1, historySize - 1);
        if(historySize > 1)
            updater_HistoryFragment(2, historySize - 2);
        if(historySize > 2)
            updater_HistoryFragment(3, historySize - 3);
        if(historySize > 3)
            updater_HistoryFragment(4, historySize - 4);
        if(historySize > 4)
            updater_HistoryFragment(5, historySize - 5);

    }

    private void updater_HistoryFragment(int boardNumber, int historyIndex) {

        String artist_genre = recentArtist.get(historyIndex) + " - " + recentGenre.get(historyIndex);

        switch (boardNumber) {
            case 1:
                binding.historyBoard1Title.setText(recentTitle.get(historyIndex));
                binding.historyBoard1ArtistGenre.setText(artist_genre);
                binding.historyBoard1Site.setText(recentSite.get(historyIndex));
                binding.historyBoard1BookmarkButton.setImageResource((recentIsBookmarked.get(historyIndex).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active );
                break;
            case 2:
                binding.historyBoard2Title.setText(recentTitle.get(historyIndex));
                binding.historyBoard2ArtistGenre.setText(artist_genre);
                binding.historyBoard2Site.setText(recentSite.get(historyIndex));
                binding.historyBoard2BookmarkButton.setImageResource((recentIsBookmarked.get(historyIndex).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active );
                break;
            case 3:
                binding.historyBoard3Title.setText(recentTitle.get(historyIndex));
                binding.historyBoard3ArtistGenre.setText(artist_genre);
                binding.historyBoard3Site.setText(recentSite.get(historyIndex));
                binding.historyBoard3BookmarkButton.setImageResource((recentIsBookmarked.get(historyIndex).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active );
                break;
            case 4:
                binding.historyBoard4Title.setText(recentTitle.get(historyIndex));
                binding.historyBoard4ArtistGenre.setText(artist_genre);
                binding.historyBoard4Site.setText(recentSite.get(historyIndex));
                binding.historyBoard4BookmarkButton.setImageResource((recentIsBookmarked.get(historyIndex).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active );
                break;
            case 5:
                binding.historyBoard5Title.setText(recentTitle.get(historyIndex));
                binding.historyBoard5ArtistGenre.setText(artist_genre);
                binding.historyBoard5Site.setText(recentSite.get(historyIndex));
                binding.historyBoard5BookmarkButton.setImageResource((recentIsBookmarked.get(historyIndex).equals("false"))
                        ? R.drawable.ic_bookmark : R.drawable.ic_bookmark_active );
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}