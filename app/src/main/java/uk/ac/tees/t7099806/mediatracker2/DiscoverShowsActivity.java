package uk.ac.tees.t7099806.mediatracker2;

import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscoverShowsActivity extends AppCompatActivity {

    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269";
    private static String GENRE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&with_genres=";
    private static String GENRE_URL_2 = "https://api.themoviedb.org/3/discover/movie?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&with_genres=";

    private RequestQueue requestQueue;
    private ArrayList<MovieInformation> moviesInfoArrayList;
    private EditText searchEdt;
    private Button searchBtn;
    RecyclerView recyclerView;


    private ArrayList<MovieInformation> movieGenreArrayList, movieGenreArrayList2;
    RecyclerView genreRecyclerView, genreRecyclerView2;


    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference checkGenre;
    private String genre;
    private String genreTwo;
    private int genreSearch;

    private TextView genreTitle, genreTitle2;
    private String adder;

    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_shows);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        genreTitle = findViewById(R.id.genreText);
        genreTitle.setText("Based off Your Lists");

        genreTitle2 = findViewById(R.id.genre2Text);

        i = 0;
        checkGenre = databaseReference.child("users").child(firebaseUser.getUid());
        Query query =  checkGenre.child("genres").orderByChild("value").limitToLast(2);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren())
                {
                    moviesInfoArrayList = new ArrayList<>();
                    recyclerView = findViewById(R.id.popularMovieList);
                    GetPopularMovieData getPopularMovieData = new GetPopularMovieData(JSON_URL, recyclerView, moviesInfoArrayList);
                    getPopularMovieData.execute();

                    if(i == 1)
                    {
                        genre = childSnapshot.getKey();
                        System.out.println("key " + genre);
                        genreTitle.setText("Genre: " + genre);
                        setGenre();
                        movieGenreArrayList = new ArrayList<>();
                        genreRecyclerView = findViewById(R.id.genre1list);
                        GetPopularMovieData getPopularMovieData1 = new GetPopularMovieData(GENRE_URL, genreRecyclerView, movieGenreArrayList);
                        getPopularMovieData1.execute();
                    }
                    if(i == 0)
                    {
                        genreTwo = childSnapshot.getKey();
                        System.out.println("genre");
                        System.out.println("genreTwo : " + genreTwo);
                        genreTitle2.setText("Genre: " + genreTwo);
                        setGenre2();
                        System.out.println("GENRE URL 2" + GENRE_URL_2);
                        movieGenreArrayList2 = new ArrayList<>();
                        genreRecyclerView2 = findViewById(R.id.genre2list);
                        GetPopularMovieData getPopularMovieData2 = new GetPopularMovieData(GENRE_URL_2, genreRecyclerView2, movieGenreArrayList2);
                        getPopularMovieData2.execute();
                    }


                    JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269";
                    GENRE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&with_genres=";
                    GENRE_URL_2 = "https://api.themoviedb.org/3/discover/movie?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&with_genres=";

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException(); }
        });


        }


        private void setGenre()
        {
            switch(genre)
            {
                case "Action":
                    genreSearch = 28;
                    break;
                case "Adventure":
                    genreSearch = 12;
                    break;
                case "Animation":
                    genreSearch = 16;
                    break;
                case "Comedy":
                    genreSearch = 35;
                    break;
                case "Crime":
                    genreSearch = 80;
                    break;
                case "Documentary":
                    genreSearch = 99;
                    break;
                case "Drama":
                    genreSearch = 18;
                    break;
                case "Family":
                    genreSearch = 10751;
                    break;
                case "Fantasy":
                    genreSearch = 14;
                    break;
                case "History":
                    genreSearch = 36;
                    break;
                case "Horror":
                    genreSearch = 27;
                    break;
                case "Music":
                    genreSearch = 10402;
                    break;
                case "Science Fiction":
                    genreSearch = 878;
                    break;
                case "TV Movie":
                    genreSearch = 10770;
                    break;
                case "Thriller":
                    genreSearch = 53;
                    break;
                case "War":
                    genreSearch = 10752;
                    break;
                case "Western":
                    genreSearch = 37;
                case "null":
                    genreSearch = 0;

            }

            System.out.println("genre search= " + genreSearch);
            String str = String.valueOf(genreSearch);
            GENRE_URL = GENRE_URL + str;
            System.out.println("Genre url: " + GENRE_URL);

        }

    private void setGenre2()
    {
        switch(genreTwo)
        {
            case "Action":
                genreSearch = 28;
                break;
            case "Adventure":
                genreSearch = 12;
                break;
            case "Animation":
                genreSearch = 16;
                break;
            case "Comedy":
                genreSearch = 35;
                break;
            case "Crime":
                genreSearch = 80;
                break;
            case "Documentary":
                genreSearch = 99;
                break;
            case "Drama":
                genreSearch = 18;
                break;
            case "Family":
                genreSearch = 10751;
                break;
            case "Fantasy":
                genreSearch = 14;
                break;
            case "History":
                genreSearch = 36;
                break;
            case "Horror":
                genreSearch = 27;
                break;
            case "Music":
                genreSearch = 10402;
                break;
            case "Science Fiction":
                genreSearch = 878;
                break;
            case "TV Movie":
                genreSearch = 10770;
                break;
            case "Thriller":
                genreSearch = 53;
                break;
            case "War":
                genreSearch = 10752;
                break;
            case "Western":
                genreSearch = 37;
            case "null":
                genreSearch = 0;

        }

        System.out.println("genre search= " + genreSearch);
        String str = String.valueOf(genreSearch);
        GENRE_URL_2 = GENRE_URL_2 + str;
        System.out.println("Genre url: " + GENRE_URL_2);
    }

    public class GetPopularMovieData extends AsyncTask<String, String, String>
    {
        String searchURL;
        RecyclerView recyclerViewI;
        ArrayList<MovieInformation> movieInformationArrayList;

        GetPopularMovieData(String searchURL, RecyclerView recyclerViewI, ArrayList<MovieInformation> movieInformationArrayList)
        {
            this.searchURL = searchURL;
            this.recyclerViewI = recyclerViewI;
            this.movieInformationArrayList = movieInformationArrayList;

        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(searchURL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data  != -1)
                    {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(urlConnection != null)
                    {
                        urlConnection.disconnect();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieInformation movieInformation = new MovieInformation();
                    movieInformation.setName(jsonObject1.getString("title"));
                    movieInformation.setImage(jsonObject1.getString("poster_path"));
                    movieInformation.setReleaseDate(jsonObject1.getString("release_date"));
                    movieInformation.setLanguage(jsonObject1.getString("original_language"));
                    movieInformation.setOverview(jsonObject1.getString("overview"));
                    movieInformation.setBackDropPath(jsonObject1.getString("backdrop_path"));
                    String genre = jsonObject1.getString("genre_ids");
                    String splitGenre = genre.split(",")[0];
                    splitGenre = splitGenre.replaceAll("\\p{P}","");
                    if(splitGenre.isEmpty())
                    {
                        movieInformation.setGenre("23");
                    }
                    else
                    {
                        movieInformation.setGenre(splitGenre);
                    }
                    movieInformationArrayList.add(movieInformation);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(movieInformationArrayList, recyclerViewI);
        }
    }

    private void PutDataIntoRecyclerView(ArrayList<MovieInformation> movieInformationeList, RecyclerView recyclerView)
    {
        MovieAdapter adapter = new MovieAdapter(this, movieInformationeList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }



}