package com.example.achordpany;

public class ChordsBookmarks {

    private static ChordsBookmarks instance;


    public static ChordsBookmarks getInstance() {
        if (instance == null) {
            instance = new ChordsBookmarks();
        }
        return instance;
    }

}
