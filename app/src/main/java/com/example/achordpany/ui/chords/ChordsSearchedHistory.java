package com.example.achordpany.ui.chords;

import java.util.ArrayList;

public class ChordsSearchedHistory {

    // Used for fragment_home
    // Used for HomeFragment.java

    private static ChordsSearchedHistory instance;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> artist = new ArrayList<>();
    private ArrayList<String> genre = new ArrayList<>();
    private ArrayList<String> site = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();

    public static ChordsSearchedHistory getInstance() {
        if (instance == null) {
            instance = new ChordsSearchedHistory();
        }
        return instance;
    }

}