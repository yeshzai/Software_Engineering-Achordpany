package com.example.achordpany.ui.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.achordpany.ChordsBookmarks;
import com.example.achordpany.ChordsSearchedHistory;
import com.example.achordpany.ChordsWebView;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentBookmarkBinding;
import com.example.achordpany.ui.history.HistoryWebViewActivity;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    Main_EverythingLocalDatabase main_EverythingLocalDatabase;
    ChordsSearchedHistory chordsSearchedHistory;
    ChordsBookmarks chordsBookmarks;
    private FragmentBookmarkBinding binding;

    ArrayList<String> bookmarkTitle;
    ArrayList<String> bookmarkArtist;
    ArrayList<String> bookmarkGenre;
    ArrayList<String> bookmarkSite;
    ArrayList<String> bookmarkURL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[FRAGMENT]", "[CREATED] BOOKMARK FRAGMENT");

        BookmarkViewModel bookmarkViewModel =
                new ViewModelProvider(this).get(BookmarkViewModel.class);

        binding = FragmentBookmarkBinding.inflate(inflater, container, false);

        // Page Functions

        main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        chordsSearchedHistory = ChordsSearchedHistory.getInstance();
        chordsBookmarks = ChordsBookmarks.getInstance();

        bookmarkTitle = chordsBookmarks.get_Title();
        bookmarkArtist = chordsBookmarks.get_Artist();
        bookmarkGenre = chordsBookmarks.get_Genre();
        bookmarkSite = chordsBookmarks.get_Site();
        bookmarkURL = chordsBookmarks.get_URL();

        load_BookmarkFragment();

        // Remove bookmarked
        binding.bookmarkBoard1BookmarkButton.setOnClickListener(v -> { bookmarked_functions(bookmarkTitle.size() - 1); }); // 4
        binding.bookmarkBoard2BookmarkButton.setOnClickListener(v -> { bookmarked_functions(bookmarkTitle.size() - 2); }); // 3
        binding.bookmarkBoard3BookmarkButton.setOnClickListener(v -> { bookmarked_functions(bookmarkTitle.size() - 3); }); // 2
        binding.bookmarkBoard4BookmarkButton.setOnClickListener(v -> { bookmarked_functions(bookmarkTitle.size() - 4); }); // 1
        binding.bookmarkBoard5BookmarkButton.setOnClickListener(v -> { bookmarked_functions(bookmarkTitle.size() - 5); }); // 0

        // Page Functions
        binding.bookmarkBoard1OpenButton.setOnClickListener(v -> open_Website( bookmarkURL.size() - 1) );
        binding.bookmarkBoard2OpenButton.setOnClickListener(v -> open_Website( bookmarkURL.size() - 2) );
        binding.bookmarkBoard3OpenButton.setOnClickListener(v -> open_Website( bookmarkURL.size() - 3) );
        binding.bookmarkBoard4OpenButton.setOnClickListener(v -> open_Website( bookmarkURL.size() - 4) );
        binding.bookmarkBoard5OpenButton.setOnClickListener(v -> open_Website( bookmarkURL.size() - 5) );

        View root = binding.getRoot();

        // Get the SharedViewModel instance
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // ✅ Update the header title
        sharedViewModel.setTitle("Bookmarks");
        sharedViewModel.setSubtext("Bookmarked songs");

        //final TextView textView = binding.textBookmark;
        //bookmarkViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void bookmarked_functions(int whichIndex) {

        int index_ToChange = chordsSearchedHistory.get_UID().indexOf(chordsBookmarks.get_UID().get(whichIndex));

        if(index_ToChange != -1)
            chordsSearchedHistory.get_isBookmarked().set(index_ToChange, "false");

        chordsBookmarks.get_Title().remove(whichIndex);
        chordsBookmarks.get_Artist().remove(whichIndex);
        chordsBookmarks.get_Genre().remove(whichIndex);
        chordsBookmarks.get_Site().remove(whichIndex);
        chordsBookmarks.get_URL().remove(whichIndex);
        chordsBookmarks.get_UID().remove(whichIndex);

        // Refresh Page
        load_BookmarkFragment();

        // Update Firebase
        // This a good solution? NO.
        // Might as well save user preferences (bookmarks) once closed ang program?
        //chordsBookmarks.updateBookmarks_FirebaseDatabase(main_EverythingLocalDatabase.get_Username());

    }

    private void load_BookmarkFragment() {

        binding.bookmarkBoard1.setVisibility(!bookmarkTitle.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        binding.bookmarkBoard2.setVisibility(bookmarkTitle.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        binding.bookmarkBoard3.setVisibility(bookmarkTitle.size() > 2 ? View.VISIBLE : View.INVISIBLE);
        binding.bookmarkBoard4.setVisibility(bookmarkTitle.size() > 3 ? View.VISIBLE : View.INVISIBLE);
        binding.bookmarkBoard5.setVisibility(bookmarkTitle.size() > 4 ? View.VISIBLE : View.INVISIBLE);
        int historySize = bookmarkTitle.size();

        if(historySize > 0)
            updater_BookmarkFragment(1, historySize - 1);
        if(historySize > 1)
            updater_BookmarkFragment(2, historySize - 2);
        if(historySize > 2)
            updater_BookmarkFragment(3, historySize - 3);
        if(historySize > 3)
            updater_BookmarkFragment(4, historySize - 4);
        if(historySize > 4)
            updater_BookmarkFragment(5, historySize - 5);

        chordsBookmarks.updateBookmarks_FirebaseDatabase(main_EverythingLocalDatabase.get_Username());
        chordsSearchedHistory.updateHistory_FirebaseDatabase(main_EverythingLocalDatabase.get_Username());

    }

    private void updater_BookmarkFragment(int boardNumber, int historyIndex) {

        String artist_genre = bookmarkArtist.get(historyIndex) + " - " + bookmarkGenre.get(historyIndex);

        switch (boardNumber) {
            case 1:
                binding.bookmarkBoard1Title.setText(bookmarkTitle.get(historyIndex));
                binding.bookmarkBoard1ArtistGenre.setText(artist_genre);
                binding.bookmarkBoard1Site.setText(bookmarkSite.get(historyIndex));
                break;
            case 2:
                binding.bookmarkBoard2Title.setText(bookmarkTitle.get(historyIndex));
                binding.bookmarkBoard2ArtistGenre.setText(artist_genre);
                binding.bookmarkBoard2Site.setText(bookmarkSite.get(historyIndex));
                break;
            case 3:
                binding.bookmarkBoard3Title.setText(bookmarkTitle.get(historyIndex));
                binding.bookmarkBoard3ArtistGenre.setText(artist_genre);
                binding.bookmarkBoard3Site.setText(bookmarkSite.get(historyIndex));
                break;
            case 4:
                binding.bookmarkBoard4Title.setText(bookmarkTitle.get(historyIndex));
                binding.bookmarkBoard4ArtistGenre.setText(artist_genre);
                binding.bookmarkBoard4Site.setText(bookmarkSite.get(historyIndex));
                break;
            case 5:
                binding.bookmarkBoard5Title.setText(bookmarkTitle.get(historyIndex));
                binding.bookmarkBoard5ArtistGenre.setText(artist_genre);
                binding.bookmarkBoard5Site.setText(bookmarkSite.get(historyIndex));
                break;
        }

    }

    private void open_Website(int whichIndex) {

        ChordsWebView chordsWebView = ChordsWebView.getInstance();
        String the_Title = bookmarkTitle.get(whichIndex);
        String the_Artist = bookmarkArtist.get(whichIndex);
        String the_Genre = bookmarkGenre.get(whichIndex);
        String the_URL = bookmarkURL.get(whichIndex);

        chordsWebView.set_Title(the_Title);
        chordsWebView.set_Artist(the_Artist);
        chordsWebView.set_Genre(the_Genre);
        chordsWebView.set_Url(the_URL);

        Intent intent = new Intent(requireActivity(), BookmarksWebViewActivity.class);
        startActivity(intent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}