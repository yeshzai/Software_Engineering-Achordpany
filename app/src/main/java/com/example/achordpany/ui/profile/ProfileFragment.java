package com.example.achordpany.ui.profile;

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
import com.example.achordpany.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "ProfileFragment is created");

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
}
