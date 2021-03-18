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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoverBooksActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private ArrayList<BookInformation> bookInfoArrayList;
    private EditText searchEdt;
    private Button searchBtn;
    private String urlGet;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference checkGenre;
    private String genre;
    private int genreSearch;
    private String genreEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_books);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        checkGenre = databaseReference.child("users").child(firebaseUser.getUid());
        Query query =  checkGenre.child("bookGenres").orderByChild("value").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot : snapshot.getChildren())
                {
                    genre = childSnapshot.getKey();
                    System.out.println("key " + genre);

                    genre.replaceAll(" ", "+");

                    getBookInfo();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




       // getBookInfo();
    }

    private void getBookInfo()
    {
        bookInfoArrayList = new ArrayList<>();


        requestQueue = Volley.newRequestQueue(DiscoverBooksActivity.this);


        requestQueue.getCache().clear();


        final String urlI = "https://www.googleapis.com/books/v1/volumes?q=subject:" + genre;
        urlGet = "https://www.googleapis.com/books/v1/volumes?q=subject:Graphic+Novel" + genre;

        RequestQueue queue = Volley.newRequestQueue(DiscoverBooksActivity.this);

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, urlI, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        //JSONObject selfLinkObj = itemsObj.getJSONObject("selfLink");
                        //String selfLink = itemsObj.optString("selfLink");
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
                        JSONObject saleInfoObj = itemsObj.optJSONObject("saleInfo");
                        String buyLink = saleInfoObj.optString("buyLink");
                        //  JSONArray categoryArray = volumeObj.getJSONArray("categories");
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }
//                        ArrayList<String> categoryArrayList = new ArrayList<>();
//                        if(categoryArray.length() != 0)
//                        {
//                            for(int j = 0; j < categoryArray.length(); j++)
//                            {
//                                categoryArrayList.add(categoryArray.optString(i));
//                            }
//                        }
//                        String category = categoryArrayList.get(0);
//                        System.out.println("category:  " + category);


                        AddS addS = new AddS(thumbnail);
                        String thumbnailS = addS.add();
                        BookInformation bookInfo = new BookInformation(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnailS, previewLink, "category");


                        bookInfoArrayList.add(bookInfo);

                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, DiscoverBooksActivity.this);


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DiscoverBooksActivity.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.bookDiscoverList);


                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
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