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
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.achordpany.R;
import com.example.achordpany.ui.auth.LoginActivity;
import com.example.achordpany.ui.auth.WelcomeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpStep4Fragment extends Fragment {

    private FirebaseAuth auth;
    FirebaseHelper firebaseHelper;

    private ImageView profileImageView;
    private TextView profileName;
    private TextView passwordValue;
    private TextView genresValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_4, container, false);
        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();

        auth = FirebaseAuth.getInstance();
        firebaseHelper = new FirebaseHelper(getContext());

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {

            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();

        });

        profileImageView = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        passwordValue = view.findViewById(R.id.passwordValue);
        genresValue = view.findViewById(R.id.genresValue);
        CheckBox checkBox_Terms = view.findViewById(R.id.checkBox_Terms);

        profileName.setText(signUpCredentials.get_credential_usernameText());
        passwordValue.setText(signUpCredentials.get_credential_passwordText());
        genresValue.setText(signUpCredentials.get_credential_genre().toString());

        view.findViewById(R.id.checkBox_TermsText).setOnClickListener(v -> {

            ((SignUpActivity) requireActivity()).navigateToStep(5);

        });

        Button btnSignUpEnd = view.findViewById(R.id.btnSignupEnd);
        btnSignUpEnd.setOnClickListener(v -> {

            if(checkBox_Terms.isChecked()) {

                saveToFirebaseDatabase();   // New User saved to Firebase (Authentication and Realtime) Database
                resetSignUpCredentials();   // Ready for next new Sign-Up

                Toast.makeText(getContext(), "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), WelcomeActivity.class);
                startActivity(intent);
                requireActivity().finish();

            } else {
                Toast.makeText(getContext(), "You need to accept the Terms & Conditions.", Toast.LENGTH_SHORT).show();
            }

        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(3);
        });

        // Get the selected image URI from SignUpActivity
        Uri selectedImageUri = ((SignUpActivity) requireActivity()).getSelectedProfileImageUri();

        Log.d("SignUpStep4", "Retrieved Image URI: " + selectedImageUri);
        signUpCredentials.set_credential_avatarUID(selectedImageUri.toString());        // Path to Profile Avatar

        // If an image was selected in Step 2, set it to the ImageView
        if (selectedImageUri != null) {
            String uriString = selectedImageUri.toString();
            Log.d("SignUpStep4", "Retrieved Image URI: " + uriString);
            // avatar path

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

    private void saveToFirebaseDatabase() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        String new_username = signUpCredentials.get_credential_usernameText();
        String new_emailAddress = signUpCredentials.get_credential_emailAddressText();
        String new_password = signUpCredentials.get_credential_passwordText();
        String new_avatarUID = signUpCredentials.get_credential_avatarUID();
        ArrayList<String> new_genres = signUpCredentials.get_credential_genre();

        // Firebase Authentication
        auth.createUserWithEmailAndPassword(new_emailAddress, new_password).addOnCompleteListener(task -> {

            if(task.isSuccessful()) {
                Log.d("SignUpStep4Fragment", "[SUCCESS] Sign Up Successful!");
            } else {
                Log.e("SignUpStep4Fragment", "[FAILED] Sign Up Error: " + Objects.requireNonNull(task.getException()).getMessage());
            }

        });

        // Firebase Realtime Database
        ArrayList<String> new_history = new ArrayList<>();
        new_history.add("[EMPTY]|HT|HistoryTitle|HA|HistoryArtist|HG|HistoryGenre|HS|HistorySite|HU|HistoryURL|HB|HistoryIsBookmarked|HUID|HistoryUID"); // FORMAT

        ArrayList<String> new_bookmarks = new ArrayList<>();
        new_bookmarks.add("[EMPTY]|BT|BookmarkTitle|BA|BookmarkArtist|BG|BookmarkGenre|BS|BookmarkSite|BU|BookmarkURL|BUID|BookmarkUID"); // FORMAT

        //                          USERNAME                EMAIL                  AVATAR        GENRES      HISTORY      BOOKMARKS
        firebaseHelper.addNewUser(new_username, new_emailAddress.toLowerCase(), new_avatarUID, new_genres, new_history, new_bookmarks);

    }

    private void resetSignUpCredentials() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        signUpCredentials.set_credential_usernameText("");
        signUpCredentials.set_credential_emailAddressText("");
        signUpCredentials.set_credential_passwordText("");
        signUpCredentials.set_credential_confirmPasswordText("");
        signUpCredentials.set_credential_avatarUID("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

}