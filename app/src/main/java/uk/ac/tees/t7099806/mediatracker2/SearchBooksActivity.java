package uk.ac.tees.t7099806.mediatracker2;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;
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

public class SearchBooksActivity extends AppCompatActivity{


    private RequestQueue requestQueue;
    private ArrayList<BookInformation> bookInfoArrayList;
    private EditText searchEdt;
    private Button searchBtn;
    private String urlGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        searchEdt = findViewById(R.id.editSearchBooks);
        searchBtn = findViewById(R.id.searchBooksButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (searchEdt.getText().toString().isEmpty()) {
                    searchEdt.setError("Please enter search query");
                    return;
                }
                getBooksInfo(searchEdt.getText().toString());
            }
        });
    }

    private void getBooksInfo(String query) {

        query.replaceAll(" ", "+");

        bookInfoArrayList = new ArrayList<>();


        requestQueue = Volley.newRequestQueue(SearchBooksActivity.this);


        requestQueue.getCache().clear();

        final String urlI = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        urlGet = "https://www.googleapis.com/books/v1/volumes?q=" + query ;

        RequestQueue queue = Volley.newRequestQueue(SearchBooksActivity.this);

        final String[] category = new String[1];

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
                        if(volumeObj.has("categories"))
                        {
                            JSONArray categoryArray = volumeObj.getJSONArray("categories");
                            ArrayList<String> categoryArrayList = new ArrayList<>();
                            if(categoryArray.length() != 0)
                            {
                                for(int j = 0; j < categoryArray.length(); j++)
                                {
                                    categoryArrayList.add(categoryArray.optString(i));
                                }
                            }
                            category[0] = categoryArrayList.get(0);
                            System.out.println("category:  " + category[0]);
                        }
                        ArrayList<String> authorsArrayList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsArrayList.add(authorsArray.optString(i));
                            }
                        }



                        AddS addS = new AddS(thumbnail);
                        String thumbnailS = addS.add();
                        BookInformation bookInfo = new BookInformation(title, subtitle, authorsArrayList, publisher, publishedDate, description, pageCount, thumbnailS, previewLink, category[0]);


                        bookInfoArrayList.add(bookInfo);

                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, SearchBooksActivity.this);


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchBooksActivity.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.bookList);


                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(SearchBooksActivity.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchBooksActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(booksObjrequest);
    }
}
