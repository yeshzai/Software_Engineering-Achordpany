package com.example.achordpany;

import java.util.ArrayList;

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

    public static ChordsSearchedHistory getInstance() {
        if (instance == null) {
            instance = new ChordsSearchedHistory();
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

}