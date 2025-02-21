package com.example.achordpany.ui.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.achordpany.R;
import com.example.achordpany.ui.adapters.AvatarAdapter;
import com.example.achordpany.ui.auth.LoginActivity;
import com.example.achordpany.ui.auth.WelcomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpStep2Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_signup_2, container, false);

        //FloatingActionButton fabProfile = view.findViewById(R.id.fab_profile);
        ImageView avatarImageView = view.findViewById(R.id.avatarImageView);
        RecyclerView recyclerViewAvatars = view.findViewById(R.id.recyclerViewAvatars);

        // Load avatar list from assets
        List<String> avatarList = getAvatarFiles();

        // Setup RecyclerView
        recyclerViewAvatars.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        AvatarAdapter adapter = new AvatarAdapter(requireContext(), avatarList, avatarPath -> {
            loadImageFromAssets(avatarPath, avatarImageView); // Set selected avatar to FAB
        });
        recyclerViewAvatars.setAdapter(adapter);

        view.findViewById(R.id.textHaveAccount).setOnClickListener(v -> {
            resetSignUpCredentials();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(3);
        });

        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).navigateToStep(1);
        });

        return view;

    }

    // Load avatar images from assets
    private List<String> getAvatarFiles() {
        List<String> fileList = new ArrayList<>();
        try {
            String[] files = requireContext().getAssets().list("profile_images"); // Read avatars from assets folder
            if (files != null) {
                fileList.addAll(Arrays.asList(files));
            }
            Log.d("SignUpStep2Fragment", "Total avatars loaded: " + fileList.size());
        } catch (IOException e) {
            Log.e("SignupActivity2", "Error listing avatar files: " + e.getMessage(), e);
        }
        return fileList;
    }

    // Load selected avatar into ImageView
    private void loadImageFromAssets(String filePath, ImageView imageView) {
        try {
            Log.d("SignUpStep2Fragment", "Loading avatar: " + filePath);
            InputStream inputStream = requireContext().getAssets().open("profile_images/" + filePath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //fab.setImageBitmap(bitmap); // Set the image on FloatingActionButton
            inputStream.close();

            /*if (bitmap == null) {
                Log.e("SignUpStep2Fragment", "Failed to decode image: " + filePath);
            } else {
                Log.d("SignUpStep2Fragment", "Successfully loaded image: " + filePath);
            }*/

            requireActivity().runOnUiThread(() -> { // Ensure UI updates on main thread
                Log.d("SignUpStep2Fragment", "Setting FAB image for: " + filePath);
                imageView.setImageBitmap(bitmap);
                imageView.invalidate(); // Force UI refresh
                //fab.requestLayout(); // Ensure layout updates
            });

            /*requireActivity().runOnUiThread(() -> {
                if (bitmap != null) {
                    fab.setImageBitmap(bitmap);
                    fab.invalidate();
                    Log.d("SignUpStep2Fragment", "Successfully loaded image: " + filePath);
                } else {
                    Log.e("SignUpStep2Fragment", "Failed to decode image, setting default placeholder.");
                    fab.setImageResource(R.drawable.default_avatar); // ✅ Set placeholder if image fails
                }
            });*/
        } catch (IOException e) {
            Log.e("SignupActivity2", "Error loading avatar: " + e.getMessage(), e);
            //requireActivity().runOnUiThread(() -> fab.setImageResource(R.drawable.default_avatar)); // Set placeholder
        }
    }

    private void resetSignUpCredentials() {

        SignUpCredentials signUpCredentials = SignUpCredentials.getInstance();
        signUpCredentials.set_credential_usernameText("");
        signUpCredentials.set_credential_emailAddressText("");
        signUpCredentials.set_credential_passwordText("");
        signUpCredentials.set_credential_confirmPasswordText("");
        signUpCredentials.set_credential_genre(new ArrayList<>());

    }

}
