package com.example.achordpany.ui.signup;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FirebaseHelper {

    private DatabaseReference users_Credentials;
    private DatabaseReference users_UsernameList;
    private DatabaseReference users_RecommendationData;
    private Context context;

    public FirebaseHelper(Context context) {

        this.users_Credentials = FirebaseDatabase.getInstance().getReference("Users_Credentials");
        this.users_UsernameList = FirebaseDatabase.getInstance().getReference("Users_UsernameList");
        this.users_RecommendationData = FirebaseDatabase.getInstance().getReference("Users_RecommendationData");
        this.context = context;

    }

    public void addNewUser(String username, String email, List<String> genre, List<String> history, List<String> bookmarks) {

        UsersCredentials users_Credentials_Class = new UsersCredentials(username, email);
        UsersRecommendationData users_RecommendationData_Class = new UsersRecommendationData(genre, history, bookmarks);

        // Add to Users_Credentials in Firebase (Realtime Database)
        users_Credentials.child(username).setValue(users_Credentials_Class).addOnSuccessListener(aVoid -> {
            Log.d("FirebaseHelper", "[SUCCESS] Users_Credentials Added Successfully!");
        }).addOnFailureListener(e -> {
            Log.e("FirebaseHelper", "[FAILED] Users_Credentials Error: " + e.getMessage());
        });

        // Add to Users_UsernameList in Firebase (Realtime Database)
        users_UsernameList.child(username).setValue("").addOnSuccessListener(aVoid -> {
            Log.d("FirebaseHelper", "[SUCCESS] Users_UsernameList Added Successfully!");
        }).addOnFailureListener(e -> {
            Log.e("FirebaseHelper", "[FAILED] Users_UsernameList Error: " + e.getMessage());
        });

        // Add to Users_RecommendationData in Firebase (Realtime Database)
        users_RecommendationData.child(username).setValue(users_RecommendationData_Class).addOnSuccessListener(aVoid -> {
            Log.d("FirebaseHelper", "[SUCCESS] Users_RecommendationData Added Successfully!");
        }).addOnFailureListener(e -> {
            Log.e("FirebaseHelper", "[FAILED] Users_RecommendationData Error: " + e.getMessage());
        });

    }

}