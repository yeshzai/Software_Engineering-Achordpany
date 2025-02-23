package com.example.achordpany.ui.chords;

public class SongTitleProcessing {

    // To be accessed from other classes.
    private static SongTitleProcessing instance;
    private String songLyrics;
    private String songTitle;
    private String songAuthor;

    private SongTitleProcessing() {
    }

    public static SongTitleProcessing getInstance() {
        if (instance == null) {
            instance = new SongTitleProcessing();
        }
        return instance;
    }

    // Setter
    public void set_SongLyrics(String songLyrics) {
        this.songLyrics = songLyrics;
    }

    public void set_SongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public void set_SongAuthor(String songAuthor) {
        this.songAuthor = songAuthor;
    }

    // Getter
    public String get_SongLyrics() {
        return songLyrics;
    }
    public String get_SongTitle() {
        return songTitle;
    }
    public String get_SongAuthor() {
        return songAuthor;
    }

}
