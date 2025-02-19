package com.example.achordpany.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> subtext = new MutableLiveData<>();

    // Getter and setter for title
    public LiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        title.setValue(newTitle);
    }

    // Getter and setter for subtext
    public LiveData<String> getSubtext() {
        return subtext;
    }

    public void setSubtext(String newSubtext) {
        subtext.setValue(newSubtext);
    }
}
