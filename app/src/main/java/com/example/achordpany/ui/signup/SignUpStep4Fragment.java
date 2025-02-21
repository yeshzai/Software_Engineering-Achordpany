package com.example.achordpany.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.WelcomeActivity;

import java.io.IOException;
import java.io.InputStream;

public class SignUpStep4Fragment extends Fragment {
    private ImageView profileImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_4, container, false);

        // Sign Up button logic
        Button btnSignUpEnd = view.findViewById(R.id.btnSignupEnd);
        btnSignUpEnd.setOnClickListener(v -> {
            Uri selectedImageUri = ((SignUpActivity) requireActivity()).getSelectedProfileImageUri();

            if (selectedImageUri != null) {
                SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                prefs.edit().putString("profile_image_uri", selectedImageUri.toString()).apply();
            }

            // Navigate to MainActivity
            Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
            startActivity(intent);
            requireActivity().finish(); // Close SignUpActivity
        });

        // Back button logic
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(3);
        });

        // Get ImageView reference
        profileImageView = view.findViewById(R.id.profileImage);

        // Get the selected image URI from SignUpActivity
        Uri selectedImageUri = ((SignUpActivity) requireActivity()).getSelectedProfileImageUri();

        Log.d("SignUpStep4", "Retrieved Image URI: " + selectedImageUri);

        // If an image was selected in Step 2, set it to the ImageView
        if (selectedImageUri != null) {
            String uriString = selectedImageUri.toString();
            Log.d("SignUpStep4", "Retrieved Image URI: " + uriString);

            if (uriString.startsWith("file:///android_asset/")) {
                // Load from assets
                String filePath = uriString.replace("file:///android_asset/", "");
                loadImageFromAssets(filePath, profileImageView);
            } else {
                // Load from normal URI
                profileImageView.setImageURI(selectedImageUri);
            }
        } else {
            Log.e("SignUpStep4", "No image URI found!");
        }

        return view;

    }

    private void loadImageFromAssets(String filePath, ImageView imageView) {
        try {
            // Ensure the path does NOT include "profile_images/" twice
            if (filePath.startsWith("profile_images/")) {
                filePath = filePath.replace("profile_images/", ""); // Remove extra prefix
            }

            InputStream inputStream = requireContext().getAssets().open("profile_images/" + filePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            inputStream.close();
            Log.d("SignUpStep4", "Successfully loaded image from assets: " + filePath);
        } catch (IOException e) {
            Log.e("SignUpStep4", "Error loading avatar: " + e.getMessage(), e);
        }
    }

}
