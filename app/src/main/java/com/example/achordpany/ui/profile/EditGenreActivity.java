package com.example.achordpany.ui.profile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.achordpany.ChordsRecommendations;
import com.example.achordpany.MainActivity;
import com.example.achordpany.Main_EverythingLocalDatabase;
import com.example.achordpany.R;
import com.example.achordpany.ui.signup.SignUpActivity;
import com.example.achordpany.ui.signup.SignUpCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditGenreActivity extends AppCompatActivity {

    private Button editgenre_genreButton_Rock;
    private Button editgenre_genreButton_Blues;
    private Button editgenre_genreButton_Jazz;
    private Button editgenre_genreButton_Classical;
    private Button editgenre_genreButton_Pop;
    private Button editgenre_genreButton_Reggae;

    private ArrayList<String> genres;
    /*private boolean genreButton_Rock_Clicked = false;
    private boolean genreButton_Blues_Clicked = false;
    private boolean genreButton_Jazz_Clicked = false;
    private boolean genreButton_Classical_Clicked = false;
    private boolean genreButton_Pop_Clicked = false;
    private boolean genreButton_Reggae_Clicked = false;*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgenre);

        genres = new ArrayList<>();
        editgenre_genreButton_Rock = findViewById(R.id.editgenre_genreButton_Rock);
        editgenre_genreButton_Blues = findViewById(R.id.editgenre_genreButton_Blues);
        editgenre_genreButton_Jazz = findViewById(R.id.editgenre_genreButton_Jazz);
        editgenre_genreButton_Classical = findViewById(R.id.editgenre_genreButton_Classical);
        editgenre_genreButton_Pop = findViewById(R.id.editgenre_genreButton_Pop);
        editgenre_genreButton_Reggae = findViewById(R.id.editgenre_genreButton_Reggae);

        setupGenreButton(editgenre_genreButton_Rock, "Rock");
        setupGenreButton(editgenre_genreButton_Blues, "Blues");
        setupGenreButton(editgenre_genreButton_Jazz, "Jazz");
        setupGenreButton(editgenre_genreButton_Classical, "Classical");
        setupGenreButton(editgenre_genreButton_Pop, "Pop");
        setupGenreButton(editgenre_genreButton_Reggae, "Reggae");

        /*editgenre_genreButton_Rock.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Rock")) {

                if(!genreButton_Rock_Clicked) {
                    genres.add("Rock");
                    editgenre_genreButton_Rock.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Rock");
                    editgenre_genreButton_Rock.setBackgroundColor(Color.WHITE);
                }
                genreButton_Rock_Clicked = !genreButton_Rock_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        editgenre_genreButton_Blues.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Blues")) {

                if(!genreButton_Blues_Clicked) {
                    genres.add("Blues");
                    editgenre_genreButton_Blues.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Blues");
                    editgenre_genreButton_Blues.setBackgroundColor(Color.WHITE);
                }
                genreButton_Blues_Clicked = !genreButton_Blues_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        editgenre_genreButton_Jazz.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Jazz")) {

                if(!genreButton_Jazz_Clicked) {
                    genres.add("Jazz");
                    editgenre_genreButton_Jazz.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Jazz");
                    editgenre_genreButton_Jazz.setBackgroundColor(Color.WHITE);
                }
                genreButton_Jazz_Clicked = !genreButton_Jazz_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        editgenre_genreButton_Classical.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Classical")) {

                if(!genreButton_Classical_Clicked) {
                    genres.add("Classical");
                    editgenre_genreButton_Classical.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Classical");
                    editgenre_genreButton_Classical.setBackgroundColor(Color.WHITE);
                }
                genreButton_Classical_Clicked = !genreButton_Classical_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        editgenre_genreButton_Pop.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Pop")) {

                if(!genreButton_Pop_Clicked) {
                    genres.add("Pop");
                    editgenre_genreButton_Pop.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Pop");
                    editgenre_genreButton_Pop.setBackgroundColor(Color.WHITE);
                }
                genreButton_Pop_Clicked = !genreButton_Pop_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });

        editgenre_genreButton_Reggae.setOnClickListener(v -> {

            if(genres.size() != 3 || genres.contains("Reggae")) {

                if(!genreButton_Reggae_Clicked) {
                    genres.add("Reggae");
                    editgenre_genreButton_Reggae.setBackgroundColor(Color.GREEN);
                } else {
                    genres.remove("Reggae");
                    editgenre_genreButton_Reggae.setBackgroundColor(Color.WHITE);
                }
                genreButton_Reggae_Clicked = !genreButton_Reggae_Clicked;

            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }

        });*/

        Button editgenre_btnContinue = findViewById(R.id.editgenre_btnContinue);
        editgenre_btnContinue.setOnClickListener(v -> {

            if(genres.size() == 3) {

                Log.d("[PROFILE - EDIT GENRE]", genres.toString());
                update_Database();

            } else {
                Toast.makeText(this, "Please Select 3 Genres!", Toast.LENGTH_SHORT).show();
            }

        });

        ImageButton editgenre_btnBack = findViewById(R.id.editgenre_btnBack);
        editgenre_btnBack.setOnClickListener(v -> {

            // Just go back to the Profile Page (No Changes)
            finish();

        });

    }

    private void setupGenreButton(Button button, String genreName) {
        button.setOnClickListener(v -> {
            if (genres.size() != 3 || genres.contains(genreName)) {
                if (!button.isSelected()) {
                    genres.add(genreName);
                    button.setSelected(true);
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00C853"))); // green
                    button.setTextColor(Color.WHITE);
                } else {
                    genres.remove(genreName);
                    button.setSelected(false);
                    button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF"))); // white
                    button.setTextColor(Color.BLACK);
                }
            } else {
                Toast.makeText(this, "Selected 3 Genres Already!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update_Database() {

        Main_EverythingLocalDatabase main_EverythingLocalDatabase = Main_EverythingLocalDatabase.getInstance();
        String user_username = main_EverythingLocalDatabase.get_Username();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_RecommendationData");
        DatabaseReference genreReference = databaseReference.child(user_username).child("Genre");
        List<String> updatedGenres = genres;

        // Update Genre in Firebase
        genreReference.setValue(updatedGenres).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                Log.d("[FIREBASE - EDIT GENRE]", "[SUCCESS] Genre Changed!");
                Toast.makeText(this, "Your genres have been updated!", Toast.LENGTH_SHORT).show();

                // Refresh recommendations
                ChordsRecommendations chordsRecommendations = ChordsRecommendations.getInstance();
                chordsRecommendations.reset_recommendations();
                chordsRecommendations.initialize_SongRecommendations(genres.get(0), genres.get(1), genres.get(2));

                main_EverythingLocalDatabase.mainPage_ResetGenres();
                main_EverythingLocalDatabase.set_Genre(genres);

                // We have to go back to MainActivity to refresh everything (esp. Recommendations)
                Intent intent = new Intent(EditGenreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                Log.e("[FIREBASE - EDIT GENRE]", "[SUCCESS] Genre Change Failed.", task.getException());
            }
        });

    }

}
