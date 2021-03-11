package uk.ac.tees.t7099806.mediatracker2;

import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SearchMoviesActivity extends AppCompatActivity {


    private static String JSON_URL =  "https://api.themoviedb.org/3/search/multi?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&query="; //"https://api.themoviedb.org/3/movie/popular?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269";

    private RequestQueue requestQueue;
    private ArrayList<MovieInformation> moviesInfoArrayList;
    private EditText searchEdt;
    private Button searchBtn;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);


        moviesInfoArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.movieList);

        searchEdt = findViewById(R.id.editSearchMovies);
        searchBtn = findViewById(R.id.searchMoviesButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                    return;
                }
                JSON_URL =  "https://api.themoviedb.org/3/search/movie?api_key=e2fb14eac3f8a8dc0f6b924ca1a8c269&query=";
                String search = searchEdt.getText().toString();
                search.replaceAll(" ", "+");
                JSON_URL = JSON_URL + search;

                GetMovieData getMovieData = new GetMovieData();
                getMovieData.execute();
            }
        });
    }

    public class GetMovieData extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
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

            moviesInfoArrayList.clear();

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
                    moviesInfoArrayList.add(movieInformation);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(moviesInfoArrayList);
        }
    }

    private void PutDataIntoRecyclerView(ArrayList<MovieInformation> movieInformationeList)
    {
        MovieAdapter adapter = new MovieAdapter(this, movieInformationeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


}