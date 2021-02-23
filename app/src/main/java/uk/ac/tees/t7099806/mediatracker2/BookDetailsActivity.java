package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,  View.OnClickListener {

    private DatabaseReference databaseReference;
    private DatabaseReference readListsRef;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;



    private String title, subtitle, publisher, publishedDate, description, thumbnail;
    private int pageCount;
    private ArrayList<String> authors;

    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    private ImageView bookIV;
    private Button buttonAddToList;
    private Spinner spinner;
    private String spinValue;
    private String increaseRead;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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


        spinner = (Spinner) findViewById(R.id.spinnerAddToList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(BookDetailsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner_list));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);

//        checkImageBool = false;

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


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        readListsRef = databaseReference.child("lists").child(userId).child("read list");
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//              for(DataSnapshot ds : snapshot.getChildren())
//              {
//                  String bookImage = ds.child("bookImage").getValue(String.class);
//                  if(bookImage == thumbnail)
//                  {
//                      checkImageBool = true;
//                      return;
//                  }
//              }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).equals("Plan To Read"))
        {
           spinValue = "plantoreadlist";
        }
        else if(parent.getItemAtPosition(position).equals("Read"))
        {
            spinValue = "read list";
        }
        else
        {
            spinValue = "reading list";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonAddToList)
        {
            saveToList(spinValue);
        }
    }



    private void saveToList(final String spin)
    {

        String pages = String.valueOf(pageCount);
        final BookInfoFirebase bookInfoFirebase = new BookInfoFirebase(publisher, pages, publishedDate, title, subtitle, description, thumbnail);

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child("read list");

        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    toastMaker(true);

                }
                else
                {
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spin).push().setValue(bookInfoFirebase);
                    if(spin == "read list")
                    {

                        int amount = Integer.parseInt(increaseRead);
                        amount = amount+1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("booksRead").setValue(amount);

                    }
                    toastMaker(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot)
    {
        increaseRead = dataSnapshot.child("users").child(firebaseUser.getUid()).child("booksRead").getValue().toString();

    }

    private void toastMaker(Boolean checkC)
    {
        if(checkC == true)
        {
            Toast.makeText(this, "already exists in list", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show();
        }

    }



}