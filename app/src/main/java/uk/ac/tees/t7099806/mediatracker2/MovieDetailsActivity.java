package uk.ac.tees.t7099806.mediatracker2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailsActivity extends AppCompatActivity {

    private String title, genre, release, runTime, yourRating, avgRating, description, backDrop, overview;

    private TextView titleTV, genreTV, releaseTV, runTimeTV, yourRatingTV, avgRatingTV, descriptionTV, overviewTV;
    private ImageView backDropIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_page);

        titleTV = findViewById(R.id.movieTitle);
        genreTV = findViewById(R.id.movieGenre);
        releaseTV = findViewById(R.id.movieRelease);
        runTimeTV = findViewById(R.id.runTime);
        yourRatingTV = findViewById(R.id.movieRating);
        avgRatingTV = findViewById(R.id.movieAverageRating);
        descriptionTV = findViewById(R.id.movieDescription);
        overviewTV = findViewById(R.id.movieDescription);
        backDropIV = findViewById(R.id.movieImage);

        title = getIntent().getStringExtra("title");
        genre = getIntent().getStringExtra("genre");
        release = getIntent().getStringExtra("release date");
        overview = getIntent().getStringExtra("overview");
        backDrop = getIntent().getStringExtra("backdrop");

        titleTV.setText(title);
        genreTV.setText("Genre: " + genre);
        releaseTV.setText("Release Date: " +release);
        overviewTV.setText(overview);
        runTimeTV.setText("Runtime: ");
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + backDrop).into(backDropIV);




    }

}