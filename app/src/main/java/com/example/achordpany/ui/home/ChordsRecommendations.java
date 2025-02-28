package com.example.achordpany.ui.home;

import java.util.ArrayList;

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

}