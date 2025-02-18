package com.example.achordpany.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.achordpany.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "SearchFragment is created");

        //SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textSearch;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Open SongSearchActivity when this fragment is selected
        //Intent intent = new Intent(getActivity(), SongSearchActivity.class);
        //startActivity(intent);

        // Close the fragment so it does not remain in the background
        //requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Open SongSearchActivity when this fragment is visible
        Intent intent = new Intent(getActivity(), SongSearchActivity.class);
        startActivity(intent);

        // Close the fragment to prevent it from staying in the background
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
