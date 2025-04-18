package com.example.achordpany;

import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;
import java.util.List;

public class ChordsRecommendations {

    public static ChordsRecommendations instance;

    private ArrayList<String> genres = new ArrayList<>();

    private ArrayList<String> recommendations_genre1_title = new ArrayList<>();
    private ArrayList<String> recommendations_genre1_artist = new ArrayList<>();
    private ArrayList<String> recommendations_genre1_url = new ArrayList<>();

    private ArrayList<String> recommendations_genre2_title = new ArrayList<>();
    private ArrayList<String> recommendations_genre2_artist = new ArrayList<>();
    private ArrayList<String> recommendations_genre2_url = new ArrayList<>();

    private ArrayList<String> recommendations_genre3_title = new ArrayList<>();
    private ArrayList<String> recommendations_genre3_artist = new ArrayList<>();
    private ArrayList<String> recommendations_genre3_url = new ArrayList<>();

    public static ChordsRecommendations getInstance() {
        if (instance == null) {
            instance = new ChordsRecommendations();
        }
        return instance;
    }

    public void set_Genres(String genre) {
        this.genres.add(genre);
    }

    public void set_RecommendationsGenre1(String title, String artist, String url) {
        this.recommendations_genre1_title.add(title);
        this.recommendations_genre1_artist.add(artist);
        this.recommendations_genre1_url.add(url);
    }

    public void set_RecommendationsGenre2(String title, String artist, String url) {
        this.recommendations_genre2_title.add(title);
        this.recommendations_genre2_artist.add(artist);
        this.recommendations_genre2_url.add(url);
    }

    public void set_RecommendationsGenre3(String title, String artist, String url) {
        this.recommendations_genre3_title.add(title);
        this.recommendations_genre3_artist.add(artist);
        this.recommendations_genre3_url.add(url);

    }

    public ArrayList<String> get_Genres() {
        return genres;
    }

    public ArrayList<String> get_RecommendationsGenre1Title() {
        return recommendations_genre1_title;
    }
    public ArrayList<String> get_RecommendationsGenre1Artist() {
        return recommendations_genre1_artist;
    }
    public ArrayList<String> get_RecommendationsGenre1URL() {
        return recommendations_genre1_url;
    }

    public ArrayList<String> get_RecommendationsGenre2Title() {
        return recommendations_genre2_title;
    }
    public ArrayList<String> get_RecommendationsGenre2Artist() {
        return recommendations_genre2_artist;
    }
    public ArrayList<String> get_RecommendationsGenre2URL() {
        return recommendations_genre2_url;
    }

    public ArrayList<String> get_RecommendationsGenre3Title() {
        return recommendations_genre3_title;
    }
    public ArrayList<String> get_RecommendationsGenre3Artist() {
        return recommendations_genre3_artist;
    }
    public ArrayList<String> get_RecommendationsGenre3URL() {
        return recommendations_genre3_url;
    }

    public void initialize_SongRecommendations(String genre1, String genre2, String genre3) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("recommendations_generator");
        if(pyModule == null) {
            Log.d("SONG RECOMMENDATIONS", "Python Module Not Found");
            return;
        }

        PyObject pyObjectResultGenre1 = pyModule.callAttr("generate_recommendations", genre1);
        PyObject pyObjectResultGenre2 = pyModule.callAttr("generate_recommendations", genre2);
        PyObject pyObjectResultGenre3 = pyModule.callAttr("generate_recommendations", genre3);
        if(pyObjectResultGenre1 == null || pyObjectResultGenre1.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [1]", "Python Module Not Found");
            return;
        }
        if(pyObjectResultGenre2 == null || pyObjectResultGenre2.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [2]", "Python Module Not Found");
            return;
        }
        if(pyObjectResultGenre3 == null || pyObjectResultGenre3.toString().equals("None")) {
            Log.d("SONG RECOMMENDATIONS [3]", "Python Module Not Found");
            return;
        }

        List<PyObject> generated_Genre1 = pyObjectResultGenre1.asList();
        List<PyObject> generated_Genre2 = pyObjectResultGenre2.asList();
        List<PyObject> generated_Genre3 = pyObjectResultGenre3.asList();

        // Debug Purposes
        Log.d("GENERATED_GENRE1", generated_Genre1.toString());
        Log.d("GENERATED_GENRE2", generated_Genre2.toString());
        Log.d("GENERATED_GENRE3", generated_Genre3.toString());

        for(PyObject obj : generated_Genre1) {

            String[] song = obj.toString().split("<00>");
            String title = song[0];
            String artist = song[1];
            String url = song[2];

            set_RecommendationsGenre1(title, artist, url);

        }

        for(PyObject obj : generated_Genre2) {

            String[] song = obj.toString().split("<00>");
            String title = song[0];
            String artist = song[1];
            String url = song[2];

            set_RecommendationsGenre2(title, artist, url);

        }

        for(PyObject obj : generated_Genre3) {

            String[] song = obj.toString().split("<00>");
            String title = song[0];
            String artist = song[1];
            String url = song[2];

            set_RecommendationsGenre3(title, artist, url);

        }

        set_Genres(genre1);
        set_Genres(genre2);
        set_Genres(genre3);

        Log.d("[CHORDS RECOMMENDATIONS]", "[CREATED] RECOMMENDATIONS");

    }

    public void reset_recommendations() {

        this.genres.removeAll(this.genres);

        this.recommendations_genre1_title.removeAll(this.recommendations_genre1_title);
        this.recommendations_genre1_artist.removeAll(this.recommendations_genre1_artist);
        this.recommendations_genre1_url.removeAll(this.recommendations_genre1_url);

        this.recommendations_genre2_title.removeAll(this.recommendations_genre2_title);
        this.recommendations_genre2_artist.removeAll(this.recommendations_genre2_artist);
        this.recommendations_genre2_url.removeAll(this.recommendations_genre2_url);

        this.recommendations_genre3_title.removeAll(this.recommendations_genre3_title);
        this.recommendations_genre3_artist.removeAll(this.recommendations_genre3_artist);
        this.recommendations_genre3_url.removeAll(this.recommendations_genre3_url);

    }

}