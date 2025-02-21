package com.example.achordpany.ui.signup;

import java.util.List;

public class UsersRecommendationData {

    public List<String> Genre;
    public List<String> History;
    public List<String> Bookmarks;

    public UsersRecommendationData() {

    }

    public UsersRecommendationData(List<String> genre, List<String> history, List<String> bookmarks) {
        this.Genre = genre;
        this.History = history;
        this.Bookmarks = bookmarks;
    }

}