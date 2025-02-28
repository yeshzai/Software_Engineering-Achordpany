package com.example.achordpany;

import android.util.Log;

import java.util.ArrayList;

import com.example.achordpany.ui.signup.UsersCredentials;
import com.example.achordpany.ui.signup.UsersRecommendationData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main_EverythingLocalDatabase {

    // To be the one to access Firebase automatically when the MAIN/DASHBOARD is opened.
    // WHERE TO CALL: Call at LogInActivity.java when LOGIN SUCCESSFUL.

    /* NOTES - README
        DATA LOADING:
        1. User logs in.
        2. Program opens Main Dashboard.
        3. Program loads Main Dashboard content.

        DATA REFRESH:
        1. User changes History, Bookmark, Genre - Prompt them to refresh.
            1.1. The program will not refresh their preferences until they re-login.
            1.2. However, their data will be saved locally. Once logged out, new data will be passed to Firebase.
        2. User changes Username, Email - Prompt them to refresh.
            2.1. The program will not refresh their credentials until they re-login.
            2.2. However, their credentials will be saved locally. Once logged out, new credentials will be passed to Firebase.

        LOCAL DATABASE REFRESH:
        - REFRESH LOCAL DATABASE CONTENT (Username, Password, Genre, History, Bookmarks)
        1. Call Main_EverythingLocalDatabase to run Constructor.
        2. Call Main_EverythingLocalDatabase.mainPage_RetrieveFirebase(String passed_username)
            2.1. This allows for all 5 Variables to be initialized with data from Firebase directly.

    */

    private static Main_EverythingLocalDatabase instance;

    // Firebase
    private DatabaseReference users_Credentials;
    private DatabaseReference users_RecommendationData;

    // User Credentials
    private String username;
    private String email;
    private String avatarUID;

    // User Data
    private ArrayList<String> genre;
    private ArrayList<String> history;
    private ArrayList<String> bookmark;

    public static Main_EverythingLocalDatabase getInstance() {
        if (instance == null) {
            instance = new Main_EverythingLocalDatabase();
        }
        return instance;
    }

    // Setter
    public void set_Username(String username) { // No Feature for Edit yet.
        this.username = username;
    }
    public void set_Email(String email) {       // No Feature for Edit yet.
        this.email = email;
    }
    public void set_AvatarUID(String avatarUID) {
        this.avatarUID = avatarUID;
    }
    public void set_Genre(ArrayList<String> genre) {
        this.genre = genre;
    }
    public void set_History(ArrayList<String> history) {
        this.history = history;
    }
    public void set_Bookmark(ArrayList<String> bookmark) {
        this.bookmark = bookmark;
    }

    // Getter
    public String get_Username() {  // No Feature for Edit yet.
        return username;
    }
    public String get_Email() {     // No Feature for Edit yet.
        return email;
    }
    public String get_AvatarUID() {
        return avatarUID;
    }
    public ArrayList<String> get_Genre() {
        return genre;
    }
    public ArrayList<String> get_History() {
        return history;
    }
    public ArrayList<String> get_Bookmark() {
        return bookmark;
    }

    public void mainPage_RetrieveFirebase(String passed_username) {

        users_Credentials = FirebaseDatabase.getInstance().getReference("Users_Credentials");
        users_RecommendationData = FirebaseDatabase.getInstance().getReference("Users_RecommendationData");

        // Credentials
        users_Credentials.child(passed_username).get().addOnSuccessListener(dataSnapshot -> {

            if(dataSnapshot.exists()) {

                UsersCredentials usersCredentials = dataSnapshot.getValue(UsersCredentials.class);
                this.set_Username(usersCredentials.username);
                this.set_Email(usersCredentials.email);
                this.set_AvatarUID(usersCredentials.avatarUID);
                Log.d("[FIREBASE RETRIEVE]", "[" + passed_username + "] [SUCCESS] USER CREDENTIALS");

            }

        }).addOnFailureListener(e -> {

            Log.e("[FIREBASE RETRIEVE]", "[" + passed_username + "] [FAILED] CREDENTIALS: " + e.getMessage());

        });

        // Recommendation Data
        users_RecommendationData.child(passed_username).get().addOnSuccessListener(dataSnapshot -> {

            if(dataSnapshot.exists()) {

                UsersRecommendationData userRecommendationData = dataSnapshot.getValue(UsersRecommendationData.class);
                this.set_Genre((ArrayList<String>) userRecommendationData.Genre);
                this.set_History((ArrayList<String>) userRecommendationData.History);
                this.set_Bookmark((ArrayList<String>) userRecommendationData.Bookmarks);
                Log.d("[FIREBASE RETRIEVE]", "[" + passed_username + "] [SUCCESS] RECOMMENDATION DATA");

                ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();
                chordsRecommendations.initialize_SongRecommendations(
                        get_Genre().get(0),
                        get_Genre().get(1),
                        get_Genre().get(2));

            }

        }).addOnFailureListener(e -> {

            Log.e("[FIREBASE RETRIEVE]", "[" + passed_username + "] [FAILED] RECOMMENDATION DATA: " + e.getMessage());

        });

    }

    public void mainPage_UpdateFirebase(String passed_username) {



    }

}