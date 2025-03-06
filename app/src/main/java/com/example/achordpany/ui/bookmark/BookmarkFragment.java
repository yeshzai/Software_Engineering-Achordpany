package com.example.achordpany.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentBookmarkBinding;

public class BookmarkFragment extends Fragment {

    private FragmentBookmarkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("[FRAGMENT]", "[CREATED] BOOKMARK FRAGMENT");

        BookmarkViewModel bookmarkViewModel =
                new ViewModelProvider(this).get(BookmarkViewModel.class);

        binding = FragmentBookmarkBinding.inflate(inflater, container, false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}