package uk.ac.tees.t7099806.mediatracker2;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoverBooksActivity extends AppCompatActivity {

    //Used to add books' informations
    private RequestQueue requestQueue;
    private ArrayList<BookInformation> bookInfoArrayList, bookInfoArrayList2, bookInfoArrayList3;
    private TextView discoverText, discoverText2, discoverText3;
    RecyclerView mRecyclerView, mRecyclerView2, mRecyclerView3;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference checkGenre;
    private String genre, genre2, genre3;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_books);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        discoverText = findViewById(R.id.discoverBookText);
        discoverText2 = findViewById(R.id.discoverBookText2);
        discoverText3 = findViewById(R.id.discoverBookText3);

        checkGenre = databaseReference.child("users").child(firebaseUser.getUid());

        mRecyclerView = (RecyclerView) findViewById(R.id.bookDiscoverList);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.bookDiscoverList2);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.bookDiscoverList3);

        bookInfoArrayList = new ArrayList<>();
        bookInfoArrayList2 = new ArrayList<>();
        bookInfoArrayList3 = new ArrayList<>();

        i = 0;
        Query query =  checkGenre.child("bookGenres").orderByChild("value").limitToLast(3);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren())
                {

                    ////shows third popular genre user has in lists
                    if(i == 2)
                    {
                        genre = childSnapshot.getKey();
                        genre.replaceAll(" ", "+");
                        discoverText.setText("Genre: " + genre);
                        getBookInfo(mRecyclerView, genre, bookInfoArrayList);
                    }
                    //shows second popular genre user has in lists
                    if(i == 1)
                    {
                        genre2 = childSnapshot.getKey();
                        genre2.replaceAll(" ", "+");
                        discoverText2.setText("Genre: " + genre2);
                        getBookInfo(mRecyclerView2, genre2, bookInfoArrayList2);
                    }
                    //shows most popular genre user has in lists
                    if(i == 0)
                    {
                        genre3 = childSnapshot.getKey();
                        genre3.replaceAll(" ", "+");
                        discoverText3.setText("Genre: " + genre3);
                        getBookInfo(mRecyclerView3, genre3, bookInfoArrayList3);
                    }


                    i++;


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //gets information of the book
    private void getBookInfo(final RecyclerView recView, String genreI, final ArrayList<BookInformation> bookInformations)
    {

        requestQueue = Volley.newRequestQueue(DiscoverBooksActivity.this);
        requestQueue.getCache().clear();

        final String urlI = "https://www.googleapis.com/books/v1/volumes?q=subject:" + genreI;

        RequestQueue queue = Volley.newRequestQueue(DiscoverBooksActivity.this);

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, urlI, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = imageLinks.optString("thumbnail");
                        String previewLink = volumeObj.optString("previewLink");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }

                        AddS addS = new AddS(thumbnail);
                        String thumbnailS = addS.add();
                        BookInformation bookInfo = new BookInformation(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnailS, previewLink, "category");

                        bookInformations.add(bookInfo);

                        BookAdapter adapter = new BookAdapter(bookInformations, DiscoverBooksActivity.this);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DiscoverBooksActivity.this, RecyclerView.HORIZONTAL, false);

                        recView.setLayoutManager(linearLayoutManager);
                        recView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(DiscoverBooksActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DiscoverBooksActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(booksObjrequest);
    }
}