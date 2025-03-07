package com.example.achordpany;

import java.util.ArrayList;

public class ChordsWebView {

    private static ChordsWebView instance;
    private String title;
    private String artist;
    private String genre;
    private String url;

    public static ChordsWebView getInstance() {
        if (instance == null) {
            instance = new ChordsWebView();
        }
        return instance;
    }

    public void set_Title(String title) {
        this.title = title;
    }
    public void set_Artist(String artist) {
        this.artist = artist;
    }
    public void set_Genre(String genre) {
        this.genre = genre;
    }
    public void set_Url(String url) {
        this.url = url;
    }

    public String get_Title() {
        return title;
    }
    public String get_Artist() {
        return artist;
    }
    public String get_Genre() {
        return genre;
    }
    public String get_Url() {
        return url;
    }

}
