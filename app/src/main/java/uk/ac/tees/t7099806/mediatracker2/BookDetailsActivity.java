package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,  View.OnClickListener {

    private DatabaseReference databaseReference;
    private DatabaseReference checkInList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;


    private String[] parent;


    private String title, subtitle, publisher, publishedDate, description, thumbnail;
    private int pageCount;
    private ArrayList<String> authors;

    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV;
    private ImageView bookIV;
    private Button buttonAddToList;
    private RatingBar ratingBar;
    private Spinner spinner;
    private String spinValue;


    private String increaseRead;
    private String changeBookRating;
    private String changeAverageRating;
    private String increaseBookScoreCount;
    private String increaseAverageScoreCount;

    private boolean buttonAdd;




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

//        buttonAddToList = findViewById(R.id.buttonAddToList);
//        buttonAddToList.setOnClickListener(this);


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


        parent = new String[1];

        ratingBar = findViewById(R.id.bookRatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                addRating();
            }
        });
//        ratingBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP)
//                {
//                    addRating();
//                }
//                return true;
//            }
//        });



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

        checkInList = databaseReference.child("lists").child(firebaseUser.getUid()).child("plantoreadlist");
        checkInList.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    setButtonToRemove();
                }
                else
                {
                    setButtonToAdd();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    private void setButtonToAdd()
    {
        buttonAddToList.setText("Add to list");
        buttonAdd = true;
    }

    private void setButtonToRemove()
    {
        buttonAddToList.setText("Remove Book");
        buttonAdd = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).equals("Plan To Read"))
        {
           spinValue = "plantoreadlist";
           changeButton("plantoreadlist");
        }
        else if(parent.getItemAtPosition(position).equals("Read"))
        {
            spinValue = "read list";
            changeButton("read list");
        }
        else
        {
            spinValue = "reading list";
            changeButton("reading list");
        }

    }



    private void changeButton(String list)
    {

        checkInList = databaseReference.child("lists").child(firebaseUser.getUid()).child(list);
        checkInList.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    setButtonToRemove();
                }
                else
                {
                    setButtonToAdd();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonAddToList && buttonAdd == true)
        {
            System.out.println("add tolist button");
            saveToList(spinValue);
        }
        if(v == buttonAddToList && buttonAdd == false)
        {
            System.out.println("remove from list button");
            removeFromList();
        }


    }


    private void removeFromList()
    {

        String pages = String.valueOf(pageCount);
        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);



        final Query query = checkRef.orderByChild("bookImage");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String myParent = snapshot.getKey();
                parent[0] = myParent;
                System.out.println("myParent: " + myParent);
                for(DataSnapshot child: snapshot.getChildren())
                {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parent[0]).removeValue();
                    if(spinValue == "read list")
                    {

                        int amount = Integer.parseInt(increaseRead);
                        amount = amount-1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("booksRead").setValue(amount);
                        // changeButton(spin);

                    }
                }
                else
                {
                    toastMaker("not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void saveToList(final String spin)
    {

        String pages = String.valueOf(pageCount);
        final BookInfoFirebase bookInfoFirebase = new BookInfoFirebase(publisher, pages, publishedDate, title, subtitle, description, thumbnail);

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spin);

        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    toastMaker("exists");
                    System.out.println(snapshot.getKey());

                }
                else
                {
                    System.out.println(snapshot.getKey());
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spin).push().setValue(bookInfoFirebase);
                    if(spin == "read list")
                    {

                        int amount = Integer.parseInt(increaseRead);
                        amount = amount+1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("booksRead").setValue(amount);
                       // changeButton(spin);

                    }
                    toastMaker("not exist to add");
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
        changeBookRating = dataSnapshot.child("users").child(firebaseUser.getUid()).child("bookScore").getValue().toString();
        changeAverageRating = dataSnapshot.child("users").child(firebaseUser.getUid()).child("totalScore").getValue().toString();
        increaseBookScoreCount =  dataSnapshot.child("users").child(firebaseUser.getUid()).child("bookScoreCount").getValue().toString();
        increaseAverageScoreCount = dataSnapshot.child("users").child(firebaseUser.getUid()).child("totalScoreCount").getValue().toString();


    }

    private void toastMaker(String checkC)
    {
        if(checkC == "exists")
        {
            Toast.makeText(this, "already exists in list", Toast.LENGTH_SHORT).show();
        }
        else if(checkC == "not exist to add")
        {
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "This book has already been removed", Toast.LENGTH_LONG).show();
        }

    }


    private void addRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
        final int numStars = (int) ratingBar.getRating();
        System.out.println(numStarsS);

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);



        final Query query = checkRef.orderByChild("bookImage");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String myParent = snapshot.getKey();
                parent[0] = myParent;
                System.out.println("myParent: " + myParent);
                for(DataSnapshot child: snapshot.getChildren())
                {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    System.out.print(parent[0]);
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parent[0]).child("Rating").setValue(numStarsS);


                    int bookScoreCount = Integer.parseInt(increaseBookScoreCount);
                    float bookRating = Float.parseFloat(changeBookRating);
                    bookRating = bookRating * bookScoreCount;
                    bookRating = bookRating + numStars;
                    bookScoreCount++;
                    bookRating = bookRating / bookScoreCount;

                    int totalScoreCount = Integer.parseInt(increaseAverageScoreCount);
                    float averageRating = Float.parseFloat(changeAverageRating);
                    averageRating = averageRating * totalScoreCount;
                    averageRating = averageRating + numStars;
                    totalScoreCount++;
                    averageRating = averageRating / totalScoreCount;


                    databaseReference.child("users").child(firebaseUser.getUid()).child("bookScoreCount").setValue(bookScoreCount);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("bookScore").setValue(bookRating);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("totalScoreCount").setValue(totalScoreCount);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("totalScore").setValue(averageRating);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }



}