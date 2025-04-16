package com.example.achordpany;

import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.util.ArrayList;
import java.util.List;

public class ChordsRecommendations {

    public static ChordsRecommendations instance;
    private ArrayList<String> genres = new ArrayList<>();
    private ArrayList<String> recommendations_genre1 = new ArrayList<>();
    private ArrayList<String> recommendations_genre2 = new ArrayList<>();
    private ArrayList<String> recommendations_genre3 = new ArrayList<>();

    public static ChordsRecommendations getInstance() {
        if (instance == null) {
            instance = new ChordsRecommendations();
        }
        return instance;
    }

    public void set_Genres(String genre) {
        this.genres.add(genre);
    }
    public void set_RecommendationsGenre1(String genre1) {
        this.recommendations_genre1.add(genre1);
    }
    public void set_RecommendationsGenre2(String genre2) {
        this.recommendations_genre2.add(genre2);
    }
    public void set_RecommendationsGenre3(String genre3) {
        this.recommendations_genre3.add(genre3);
    }

    public ArrayList<String> get_Genres() {
        return genres;
    }
    public ArrayList<String> get_RecommendationsGenre1() {
        return recommendations_genre1;
    }
    public ArrayList<String> get_RecommendationsGenre2() {
        return recommendations_genre2;
    }
    public ArrayList<String> get_RecommendationsGenre3() {
        return recommendations_genre3;
    }

    public void initialize_SongRecommendations(String genre1, String genre2, String genre3) {

        Python python = Python.getInstance();

        PyObject pyModule = python.getModule("search_recommendations");
        if(pyModule == null) {
            Log.d("SONG RECOMMENDATIONS", "Python Module Not Found");
            return;
        }

        PyObject pyObjectResultGenre1 = pyModule.callAttr("get_songs_by_genre", genre1);
        PyObject pyObjectResultGenre2 = pyModule.callAttr("get_songs_by_genre", genre2);
        PyObject pyObjectResultGenre3 = pyModule.callAttr("get_songs_by_genre", genre3);
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

        List<PyObject> pyListGenre1 = pyObjectResultGenre1.asList();
        List<PyObject> pyListGenre2 = pyObjectResultGenre2.asList();
        List<PyObject> pyListGenre3 = pyObjectResultGenre3.asList();

        for(PyObject obj : pyListGenre1) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.toString();
            set_RecommendationsGenre1(songTitle);

        }

        for(PyObject obj : pyListGenre2) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.toString();
            set_RecommendationsGenre2(songTitle);

        }

        for(PyObject obj : pyListGenre3) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.toString();
            set_RecommendationsGenre3(songTitle);

        }

        set_Genres(genre1);
        set_Genres(genre2);
        set_Genres(genre3);

        Log.d("[CHORDS RECOMMENDATIONS]", "[CREATED] RECOMMENDATIONS");

    }

    public void reset_recommendations() {

        this.genres.removeAll(this.genres);
        this.recommendations_genre1.removeAll(this.recommendations_genre1);
        this.recommendations_genre2.removeAll(this.recommendations_genre2);
        this.recommendations_genre3.removeAll(this.recommendations_genre3);

    }

}