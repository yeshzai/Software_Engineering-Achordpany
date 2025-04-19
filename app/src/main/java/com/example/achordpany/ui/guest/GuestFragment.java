package com.example.achordpany.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.achordpany.databinding.FragmentHomeBinding;
import com.example.achordpany.ui.SharedViewModel;

public class GuestFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.setTitle("Welcome Guest");
        sharedViewModel.setSubtext("Login to unlock full features");

        disableAccessForGuests();

        return root;
    }

    private void disableAccessForGuests() {

        // "Open All" Buttons → Show login dialog
        binding.bookmarksOpenAllButton.setOnClickListener(v -> showLoginDialog());
        binding.recentSearchesOpenAllButton.setOnClickListener(v -> showLoginDialog());
        binding.recommendationsOpenAllButton.setOnClickListener(v -> showLoginDialog());

        // Optional: dim or visually mark them as disabled
        binding.bookmarksOpenAllButton.setAlpha(0.5f);
        binding.recentSearchesOpenAllButton.setAlpha(0.5f);
        binding.recommendationsOpenAllButton.setAlpha(0.5f);
    }

    private void showLoginDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Login Required")
                .setMessage("Please log in to access this feature.")
                .setPositiveButton("Login", (dialog, which) -> {
                    // You can redirect to login activity here
                    // startActivity(new Intent(requireContext(), LoginActivity.class));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
