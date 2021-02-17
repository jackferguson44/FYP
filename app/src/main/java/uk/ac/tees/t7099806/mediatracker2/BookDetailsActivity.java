package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookDetailsActivity extends AppCompatActivity implements  View.OnClickListener{

    public DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;



    private String title, subtitle, publisher, publishedDate, description, thumbnail;
    int pageCount;
    private ArrayList<String> authors;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    private ImageView bookIV;
    private Button buttonAddToList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTV = findViewById(R.id.TVTitle);
        subtitleTV = findViewById(R.id.TVSubTitle);
        publisherTV = findViewById(R.id.TVpublisher);
        descTV = findViewById(R.id.TVDescription);
        pageTV = findViewById(R.id.TVNoOfPages);
        publishDateTV = findViewById(R.id.TVPublishDate);
        bookIV = findViewById(R.id.IVbook);

        buttonAddToList = findViewById(R.id.buttonAddToList);
        buttonAddToList.setOnClickListener(this);

        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");

        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Published On : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("No Of Pages : " + pageCount);

        Picasso.get().load(thumbnail).into(bookIV);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();

    }

    @Override
    public void onClick(View v) {
        if(v == buttonAddToList)
        {
            saveToList();
        }
    }

    private void saveToList()
    {
        String pages = String.valueOf(pageCount);

        BookInfoFirebase bookInfoFirebase = new BookInfoFirebase(publisher, pages, publishedDate, title, subtitle, description, thumbnail);
        databaseReference.child("lists").child(firebaseUser.getUid()).child("read list").push().setValue(bookInfoFirebase);
        Toast.makeText(this, "Added to read list", Toast.LENGTH_SHORT).show();
    }


}