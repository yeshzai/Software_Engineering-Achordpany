package com.example.achordpany;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChordsSearchedHistory {

    /*
        THIS WILL BE USED BY THE FF. JAVA CLASSES:

        1. HomeFragment.java - Show two(2) latest/recent history content.
        2. HistoryFragment.java - Show all history content.
        3. Main_EverythingLocalDatabase.java - Save all history content to Firebase.
    */

    private static ChordsSearchedHistory instance;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> artist = new ArrayList<>();
    private ArrayList<String> genre = new ArrayList<>();
    private ArrayList<String> site = new ArrayList<>();
    private ArrayList<String> url = new ArrayList<>();
    private ArrayList<String> isBookmarked = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<>();

    public static ChordsSearchedHistory getInstance() {
        if (instance == null) {
            instance = new ChordsSearchedHistory();
        }
        return instance;
    }

    // Individual Transfer (String > add > ArrayList)
    public void set_Title(String title) {
        this.title.add(title);
    }
    public void set_Artist(String artist) {
        this.artist.add(artist);
    }
    public void set_Genre(String genre) {
        this.genre.add(genre);
    }
    public void set_Site(String site) {
        this.site.add(site);
    }
    public void set_URL(String url) {
        this.url.add(url);
    }
    public void set_isBookmarked(String isBookmarked) {
        this.isBookmarked.add(isBookmarked);
    }
    public void set_UID(String uid) {
        this.uid.add(uid);
    }

    // One Time Transfer (ArrayList > ArrayList)
    public void set_Title(ArrayList<String> title) {
        this.title = title;
    }
    public void set_Artist(ArrayList<String> artist) {
        this.artist = artist;
    }
    public void set_Genre(ArrayList<String> genre) {
        this.genre = genre;
    }
    public void set_Site(ArrayList<String> site) {
        this.site = site;
    }
    public void set_URL(ArrayList<String> url) {
        this.url = url;
    }
    public void set_isBookmarked(ArrayList<String> isBookmarked) {
        this.isBookmarked = isBookmarked;
    }
    public void set_UID(ArrayList<String> uid) {
        this.uid = uid;
    }

    public ArrayList<String> get_Title() {
        return title;
    }
    public ArrayList<String> get_Artist() {
        return artist;
    }
    public ArrayList<String> get_Genre() {
        return genre;
    }
    public ArrayList<String> get_Site() {
        return site;
    }
    public ArrayList<String> get_URL() {
        return url;
    }
    public ArrayList<String> get_isBookmarked() {
        return isBookmarked;
    }
    public ArrayList<String> get_UID() { return uid; }

    public void initialize_SongHistory(ArrayList<String> from_database_history) {

        /*
            [EMPTY]|HT|HistoryTitle|HA|HistoryArtist|HG|HistoryGenre|HS|HistorySite|HU|HistoryURL|HB|HistoryIsBookmarked|HUID|HistoryUID - Always at the beginning

            Hence if from_database_history.size() == 1, then it is empty. So, no need to do anything.
            If from_database_history.size() > 1, then we proceed with retrieving from database.
        */

        if(from_database_history.size() > 1) {

            for(int i = 1; i < from_database_history.size(); i++) {

                String database_history_title = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HT|") + 4, from_database_history.get(i).indexOf("|HA|"));
                String database_history_artist = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HA|") + 4, from_database_history.get(i).indexOf("|HG|"));
                String database_history_genre = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HG|") + 4, from_database_history.get(i).indexOf("|HS|"));
                String database_history_site = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HS|") + 4, from_database_history.get(i).indexOf("|HU|"));
                String database_history_url = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HU|") + 4, from_database_history.get(i).indexOf("|HB|"));
                String database_history_isBookmarked = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HB|") + 4, from_database_history.get(i).indexOf("|HUID|"));
                String database_history_uid = from_database_history.get(i).substring(
                        from_database_history.get(i).indexOf("|HUID|") + 4);

                this.set_Title(database_history_title);
                this.set_Artist(database_history_artist);
                this.set_Genre(database_history_genre);
                this.set_Site(database_history_site);
                this.set_URL(database_history_url);
                this.set_isBookmarked(database_history_isBookmarked);
                this.set_UID(database_history_uid);

            }

        }

    }

    // Called when application is closed.
    public void updateHistory_FirebaseDatabase(String passed_username) {

        // FORMAT: |HT|HistoryTitle|HA|HistoryArtist|HG|HistoryGenre|HS|HistorySite|HU|HistoryURL
        ArrayList<String> updated_HistoryList = new ArrayList<>();
        updated_HistoryList.add("[EMPTY]|HT|HistoryTitle|HA|HistoryArtist|HG|HistoryGenre|HS|HistorySite|HU|HistoryURL|HB|HistoryIsBookmarked|HUID|HistoryUID");

        for(int i = 0; i < this.get_Title().size(); i++) {

            String history_node =
                    "|HT|" + this.get_Title().get(i) +
                            "|HA|" + this.get_Artist().get(i) +
                            "|HG|" + this.get_Genre().get(i) +
                            "|HS|" + this.get_Site().get(i) +
                            "|HU|" + this.get_URL().get(i) +
                            "|HB|" + this.get_isBookmarked().get(i) +
                            "|HUID|" + this.get_UID().get(i);
            updated_HistoryList.add(history_node);

        }

        DatabaseReference users_RecommendationData_History = FirebaseDatabase.getInstance().getReference("Users_RecommendationData")
                .child(passed_username)
                .child("History");

        users_RecommendationData_History.setValue(updated_HistoryList);
        Log.d("[FIREBASE UPDATE]", "[UPDATED] HISTORY");

    }

}