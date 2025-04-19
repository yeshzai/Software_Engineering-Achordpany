package com.example.achordpany;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChordsBookmarks {

    private static ChordsBookmarks instance;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> artist = new ArrayList<>();
    private ArrayList<String> genre = new ArrayList<>();
    private ArrayList<String> site = new ArrayList<>();
    private ArrayList<String> url = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<>();

    public static ChordsBookmarks getInstance() {
        if (instance == null) {
            instance = new ChordsBookmarks();
        }
        return instance;
    }

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
    public void set_UID(String uid) {
        this.uid.add(uid);
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
    public ArrayList<String> get_UID() {
        return uid;
    }

    public void initialize_Bookmarks(ArrayList<String> from_database_bookmarks) {

        // [EMPTY]|BT|BookmarkTitle|BA|BookmarkArtist|BG|BookmarkGenre|BS|BookmarkSite|BU|BookmarkURL|BUID|BookmarkUID

        if(from_database_bookmarks.size() > 1) {

            for(int i = 1; i < from_database_bookmarks.size(); i++) {

                String database_bookmarks_title = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BT|") + 4, from_database_bookmarks.get(i).indexOf("|BA|"));
                String database_bookmarks_artist = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BA|") + 4, from_database_bookmarks.get(i).indexOf("|BG|"));
                String database_bookmarks_genre = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BG|") + 4, from_database_bookmarks.get(i).indexOf("|BS|"));
                String database_bookmarks_site = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BS|") + 4, from_database_bookmarks.get(i).indexOf("|BU|"));
                String database_bookmarks_url = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BU|") + 4, from_database_bookmarks.get(i).indexOf("|BUID|"));
                String database_bookmarks_uid = from_database_bookmarks.get(i).substring(
                        from_database_bookmarks.get(i).indexOf("|BUID|") + 4);

                this.set_Title(database_bookmarks_title);
                this.set_Artist(database_bookmarks_artist);
                this.set_Genre(database_bookmarks_genre);
                this.set_Site(database_bookmarks_site);
                this.set_URL(database_bookmarks_url);
                this.set_UID(database_bookmarks_uid);

            }

        }

    }

    // Called when application is closed.
    public void updateBookmarks_FirebaseDatabase(String passed_username) {

        // FORMAT: "[EMPTY]|BT|BookmarkTitle|BA|BookmarkArtist|BG|BookmarkGenre|BS|BookmarkSite|BU|BookmarkURL|BUID|BookmarkUID"

        ArrayList<String> updated_BookmarksList = new ArrayList<>();
        updated_BookmarksList.add("[EMPTY]|BT|BookmarkTitle|BA|BookmarkArtist|BG|BookmarkGenre|BS|BookmarkSite|BU|BookmarkURL|BUID|BookmarkUID");

        for(int i = 0; i < this.get_Title().size(); i++) {

            String history_node =
                    "|BT|" + this.get_Title().get(i) +
                            "|BA|" + this.get_Artist().get(i) +
                            "|BG|" + this.get_Genre().get(i) +
                            "|BS|" + this.get_Site().get(i) +
                            "|BU|" + this.get_URL().get(i) +
                            "|BUID|" + this.get_UID().get(i);
            updated_BookmarksList.add(history_node);

        }

        DatabaseReference users_RecommendationData_Bookmarks = FirebaseDatabase.getInstance().getReference("Users_RecommendationData")
                .child(passed_username)
                .child("Bookmarks");

        users_RecommendationData_Bookmarks.setValue(updated_BookmarksList);
        Log.d("[FIREBASE UPDATE]", "[UPDATED] BOOKMARKS");

    }

}