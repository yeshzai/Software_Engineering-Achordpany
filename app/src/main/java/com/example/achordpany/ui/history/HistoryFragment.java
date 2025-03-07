package com.example.achordpany.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.achordpany.ChordsBookmarks;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.ChordsWebView;
import com.example.achordpany.MainActivity;
import com.example.achordpany.R;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentHistoryBinding;
import com.example.achordpany.ui.bookmark.BookmarkFragment;
import com.example.achordpany.ui.chords.ChordsDisplayActivity;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    ChordsSearchedHistory chordsSearchedHistory;
    ChordsBookmarks chordsBookmarks;
    private FragmentHistoryBinding binding;

    ArrayList<String> recentTitle;
    ArrayList<String> recentArtist;
    ArrayList<String> recentGenre;
    ArrayList<String> recentSite;
    ArrayList<String> recentURL;
    ArrayList<String> recentIsBookmarked;
    ArrayList<String> recentUID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[HISTORY FRAGMENT]", "[CREATED] HISTORY FRAGMENT");

        HistoryViewModel historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        // Page Functions
        chordsSearchedHistory = ChordsSearchedHistory.getInstance();
        chordsBookmarks = ChordsBookmarks.getInstance();

        recentTitle = chordsSearchedHistory.get_Title();
        recentArtist = chordsSearchedHistory.get_Artist();
        recentGenre = chordsSearchedHistory.get_Genre();
        recentSite = chordsSearchedHistory.get_Site();
        recentURL = chordsSearchedHistory.get_URL();
        recentIsBookmarked = chordsSearchedHistory.get_isBookmarked();
        recentUID = chordsSearchedHistory.get_UID();

        load_HistoryFragment();

        binding.historyBoard1BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(1, recentIsBookmarked.size() - 1); } );
        binding.historyBoard2BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(2, recentIsBookmarked.size() - 2); } );
        binding.historyBoard3BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(3, recentIsBookmarked.size() - 3); } );
        binding.historyBoard4BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(4, recentIsBookmarked.size() - 4); } );
        binding.historyBoard5BookmarkButton.setOnClickListener(v -> { historyBookmarks_Function(5, recentIsBookmarked.size() - 5); } );

        binding.historyBoard1OpenButton.setOnClickListener(v -> open_Website( recentURL.size() - 1) );
        binding.historyBoard2OpenButton.setOnClickListener(v -> open_Website( recentURL.size() - 2) );
        binding.historyBoard3OpenButton.setOnClickListener(v -> open_Website( recentURL.size() - 3) );
        binding.historyBoard4OpenButton.setOnClickListener(v -> open_Website( recentURL.size() - 4) );
        binding.historyBoard5OpenButton.setOnClickListener(v -> open_Website( recentURL.size() - 5) );
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

        /*
            ALGORITHM:
            1. We will only be able to add to bookmarks everytime bookmark button for history board is "false".
            2. We will only be able to remove from bookmarks everytime bookmark button for history board is:
                2.1. TRUE
                2.2. Still in History (if gone, then only way to remove is through bookmarks page
        */

        switch (recentIsBookmarked.get(whichHistoryIndex)) {

            case "false":
                bookmark_status_icon = R.drawable.ic_bookmark_active;
                recentIsBookmarked.set(whichHistoryIndex, "true");

                // Add to bookmarks
                if(chordsBookmarks.get_Title().size() >= 5) {

                    chordsBookmarks.get_Title().remove(0);
                    chordsBookmarks.get_Artist().remove(0);
                    chordsBookmarks.get_Genre().remove(0);
                    chordsBookmarks.get_Site().remove(0);
                    chordsBookmarks.get_URL().remove(0);
                    chordsBookmarks.get_UID().remove(0);

                }

                chordsBookmarks.set_Title(recentTitle.get(whichHistoryIndex));
                chordsBookmarks.set_Artist(recentArtist.get(whichHistoryIndex));
                chordsBookmarks.set_Genre(recentGenre.get(whichHistoryIndex));
                chordsBookmarks.set_Site(recentSite.get(whichHistoryIndex));
                chordsBookmarks.set_URL(recentURL.get(whichHistoryIndex));
                chordsBookmarks.set_UID(recentUID.get(whichHistoryIndex));
                break;

            case "true":
                bookmark_status_icon = R.drawable.ic_bookmark;
                recentIsBookmarked.set(whichHistoryIndex, "false");

                // Remove from bookmarks
                delete_Bookmarked(recentUID.get(whichHistoryIndex));
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

    private void delete_Bookmarked(String to_remove_UID) {

        int index_ToRemove = chordsBookmarks.get_UID().indexOf(to_remove_UID);
        if(index_ToRemove != -1) {

            chordsBookmarks.get_Title().remove(index_ToRemove);
            chordsBookmarks.get_Artist().remove(index_ToRemove);
            chordsBookmarks.get_Genre().remove(index_ToRemove);
            chordsBookmarks.get_Site().remove(index_ToRemove);
            chordsBookmarks.get_URL().remove(index_ToRemove);
            chordsBookmarks.get_UID().remove(index_ToRemove);

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

    private void open_Website(int whichIndex) {

        ChordsWebView chordsWebView = ChordsWebView.getInstance();
        String the_Title = recentTitle.get(whichIndex);
        String the_Artist = recentArtist.get(whichIndex);
        String the_Genre = recentGenre.get(whichIndex);
        String the_URL = recentURL.get(whichIndex);

        chordsWebView.set_Title(the_Title);
        chordsWebView.set_Artist(the_Artist);
        chordsWebView.set_Genre(the_Genre);
        chordsWebView.set_Url(the_URL);

        Intent intent = new Intent(requireActivity(), HistoryWebViewActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}