package com.example.achordpany.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.ui.SharedViewModel;
import com.example.achordpany.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    private ImageView profile_image;
    public String songRecommendation_TITLE1_1;
    public String songRecommendation_ARTIST1_1;
    public String songRecommendation_TITLE1_2;
    public String songRecommendation_ARTIST1_2;
    public String songRecommendation_TITLE2;
    public String songRecommendation_ARTIST2;
    public String songRecommendation_TITLE3;
    public String songRecommendation_ARTIST3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("Fragment", "ProfileFragment is created");

        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Data Insertion
        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();

        String username = main_EverythingLocalDatabase.get_Username();
        String email = main_EverythingLocalDatabase.get_Email();
        String avatar_UID = main_EverythingLocalDatabase.get_AvatarUID();
        ArrayList<String> genre = main_EverythingLocalDatabase.get_Genre();
        Uri avatarUID_Path = Uri.parse(main_EverythingLocalDatabase.get_AvatarUID());
        get_SongRecommendations(genre.get(0), genre.get(1), genre.get(2));

        binding.username.setText(username);
        binding.emailPlaceholder.setText(email);
        binding.genrePlaceholder1.setText(genre.get(0));
        binding.genrePlaceholder2.setText(genre.get(1));
        binding.genrePlaceholder3.setText(genre.get(2));
        Glide.with(this).load(avatar_UID).into(binding.profileImage);

        binding.recommendations11.setText(songRecommendation_TITLE1_1);
        binding.recommendations11Artist.setText(songRecommendation_ARTIST1_1);
        binding.recommendations11Genre.setText(genre.get(0));

        binding.recommendations12.setText(songRecommendation_TITLE1_2);
        binding.recommendations12Artist.setText(songRecommendation_ARTIST1_2);
        binding.recommendations12Genre.setText(genre.get(0));

        binding.recommendations2.setText(songRecommendation_TITLE2);
        binding.recommendations2Artist.setText(songRecommendation_ARTIST2);
        binding.recommendations2Genre.setText(genre.get(1));

        binding.recommendations3.setText(songRecommendation_TITLE3);
        binding.recommendations3Artist.setText(songRecommendation_ARTIST3);
        binding.recommendations3Genre.setText(genre.get(2));

        // Get the SharedViewModel instance
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Update the header title via SharedViewModel
        sharedViewModel.setTitle("Profile");
        sharedViewModel.setSubtext("Account section");



        //final TextView textView = binding.textProfile;
        //profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void get_SongRecommendations(String genre1, String genre2, String genre3) {

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

        List<String> songRecommendationListGenre1 = new ArrayList<>();
        List<String> songRecommendationListGenre2 = new ArrayList<>();
        List<String> songRecommendationListGenre3 = new ArrayList<>();

        for(PyObject obj : pyListGenre1) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            songRecommendationListGenre1.add(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        for(PyObject obj : pyListGenre2) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            songRecommendationListGenre2.add(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        for(PyObject obj : pyListGenre3) {

            List<PyObject> tuple = obj.asList(); // Convert tuple to List
            String songTitle = tuple.get(0).toString();
            String artist = tuple.get(1).toString();
            songRecommendationListGenre3.add(songTitle + "|||||" + artist); // ||||| is the separator to be used later

        }

        String[] songTITLEARTIST_Genre1_1 = songRecommendationListGenre1.get(0).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre1_2 = songRecommendationListGenre1.get(1).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre2 = songRecommendationListGenre2.get(0).split("\\|\\|\\|\\|\\|");
        String[] songTITLEARTIST_Genre3 = songRecommendationListGenre3.get(0).split("\\|\\|\\|\\|\\|");

        songRecommendation_TITLE1_1 = songTITLEARTIST_Genre1_1[0];
        songRecommendation_ARTIST1_1 = songTITLEARTIST_Genre1_1.length > 1 ? songTITLEARTIST_Genre1_1[1] : ""; // Avoid index errors
        songRecommendation_TITLE1_2 = songTITLEARTIST_Genre1_2[0];
        songRecommendation_ARTIST1_2 = songTITLEARTIST_Genre1_2.length > 1 ? songTITLEARTIST_Genre1_2[1] : ""; // Avoid index errors
        songRecommendation_TITLE2 = songTITLEARTIST_Genre2[0];
        songRecommendation_ARTIST2 = songTITLEARTIST_Genre2.length > 1 ? songTITLEARTIST_Genre2[1] : ""; // Avoid index errors
        songRecommendation_TITLE3 = songTITLEARTIST_Genre3[0];
        songRecommendation_ARTIST3 = songTITLEARTIST_Genre3.length > 1 ? songTITLEARTIST_Genre3[1] : ""; // Avoid index errors

    }

}
