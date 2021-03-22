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

import android.provider.ContactsContract;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,  View.OnClickListener {

    private DatabaseReference databaseReference;
    private DatabaseReference checkInList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;




    //Gets information that is passed from BookAdapter class
    private String title, subtitle, publisher, publishedDate, description, thumbnail, category;
    private int pageCount;

    //Information to be displayed on screen
    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV, ratingTV, avgRatingTV;
    private ImageView bookIV;


    private Button buttonAddToList;
    private RatingBar ratingBar;
    private Spinner spinner;
    private String spinValue;

    //Keeps track of numbers of books read to decrease on increase if book is added or removed from read list
    private String increaseRead;
    //The users rating of the book
    private String changeBookRating;
    //The average rating of the book
    private String changeAverageRating;
    //Amount of ratings users has done, used to work out the average rating the user has gave to books
    private String increaseBookScoreCount;
    //Amount of ratings book has, used to work out the average rating all users has gave a book
    private String increaseAverageScoreCount;

    //If true the button adds book to lists, if false the button removes book from lists
    private boolean buttonAdd;


    //The books average rating and count of ratings
    private String bookRatingAvg, bookRatingCount;


    //Both used to get the book id
    private DatabaseReference bookRef;
    private String bookKey;
    private Query bookQuery;
    private boolean bookExists;

    //Usd to format the ratings of the book
    private DecimalFormat decimalFormat;
    private String avgBookRatingSet;
    private float bookS;

    //used to get reference of type of list
    private DatabaseReference listRef;
    //Parent key of the types of lists
    private String listKey;
    //Used to get list id and set ratings
    private Query listQuery;
    //checks the list exits so no crashes occur when setting ratings
    private boolean listExist;
    //Both used to set ratings
    private String avgListRatingSet;
    private float listS;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTV = findViewById(R.id.TVTitle);
        subtitleTV = findViewById(R.id.TVSubTitle);
        publisherTV = findViewById(R.id.TVPublisher);
        descTV = findViewById(R.id.TVDescription);
        pageTV = findViewById(R.id.TVNoOfPages);
        publishDateTV = findViewById(R.id.TVPublishDate);
        bookIV = findViewById(R.id.IVbook);
        ratingTV = findViewById(R.id.TVRating);
        avgRatingTV = findViewById(R.id.TVAverageRating);

        buttonAddToList = findViewById(R.id.buttonAddToList);
        buttonAddToList.setOnClickListener(this);

        decimalFormat =  new DecimalFormat("#.00");

        //used to check what list user has checked on dropdown
        spinValue ="plantoreadlist";

        //Used to create and make dropdown functional
        spinner = (Spinner) findViewById(R.id.spinnerAddToList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(BookDetailsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner_list));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);


        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        category = getIntent().getStringExtra("subject");

        titleTV.setText(title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Publish Date : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Page Count: " + pageCount);

        Picasso.get().load(thumbnail).into(bookIV);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();


        //Used to make rating bar functional
        ratingBar = findViewById(R.id.bookRatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                addRating();
            }
        });


        //Used to get data of various things
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Used to set buttons functionality depending on whether book is in user's list or not
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


        //Used to get parent id of the book
        bookRef = databaseReference.child("books");
        bookQuery = bookRef.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getBookId(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookQuery.addListenerForSingleValueEvent(valueEventListener);



        //used to get users list parent id
        listRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);
        listQuery = listRef.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getListId(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        listQuery.addListenerForSingleValueEvent(valueEventListener1);

        //Used to set average and user rating
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                setBookAvgRating(snapshot);
                setListAvgRating(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //Gets the parent ID of the book
    private void getBookId(DataSnapshot snapshot)
    {
        for(DataSnapshot ds : snapshot.getChildren())
        {

            bookKey = ds.getKey();
            if(ds.exists())
            {

                bookExists = true;
            }
            else
            {
                bookExists = false;
            }

        }

    }

    //Sets the average rating of the book
    private void setBookAvgRating(DataSnapshot snapshot)
    {
        if(bookExists == true)
        {
            bookS = Float.parseFloat(snapshot.child("books").child(bookKey).child("rating").getValue().toString());
            avgBookRatingSet = decimalFormat.format(bookS);
            avgRatingTV.setText("Average rating: " + avgBookRatingSet);
        }
    }


    //Sets the user rating of the book
    private void setListAvgRating(final DataSnapshot snapshot)
    {
        if(listExist == true)
        {
            DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(listKey);
            checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotI) {
                    if(snapshotI.child("Rating").exists())
                    {
                        listS = Float.parseFloat(snapshot.child("lists").child(firebaseUser.getUid()).child(spinValue).child(listKey).child("Rating").getValue().toString());
                        avgListRatingSet = decimalFormat.format(listS);
                        ratingTV.setText("Your rating: " + avgListRatingSet);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    //Gets parent ID of the user list
    private void getListId(DataSnapshot snapshot)
    {
        for(DataSnapshot ds : snapshot.getChildren())
        {
            listKey = ds.getKey();
            if(ds.exists())
            {
                listExist = true;

            }
            else
            {
                listExist = false;
            }
        }
    }

    //Make the buttons functionality adding book to list
    private void setButtonToAdd()
    {
        buttonAddToList.setText("Add to list");
        buttonAdd = true;
    }

    //Make the buttons functionality removing book from list
    private void setButtonToRemove()
    {
        buttonAddToList.setText("Remove Book");
        buttonAdd = false;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //calls changeButton to change button functionality if book is or isn't in chosen list
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

        //gets the parent id of the users list
        listRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);
        listQuery = listRef.orderByChild("image").equalTo(thumbnail);
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                getListId(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        listQuery.addListenerForSingleValueEvent(valueEventListener1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                setBookAvgRating(snapshot);
                setListAvgRating(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //Checks if book is/isn't in chosen list, changes button functionality based on result
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
        //Save to list if book not in list
        if (v == buttonAddToList && buttonAdd == true)
        {
            saveToList(spinValue);
        }
        //Removes book from list if in list
        if(v == buttonAddToList && buttonAdd == false)
        {
            removeFromList();
        }


    }


    //Removes book from list
    private void removeFromList()
    {

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);

        //Used to check if book is in list
        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String parentI = "";

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    parentI = dataSnapshot.getKey();
                }

                //Removes book from list
                if(snapshot.exists())
                {
                    setButtonToAdd();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parentI).removeValue();
                    if(spinValue == "read list")
                    {

                        int amount = Integer.parseInt(increaseRead);
                        amount = amount-1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("booksRead").setValue(amount);


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


    //Adds book to list
    private void saveToList(final String spin)
    {

        String pages = String.valueOf(pageCount);
        final BookInfoFirebase bookInfoFirebase = new BookInfoFirebase(publisher, pages, publishedDate, title, subtitle, description, thumbnail);
        final BookInfoData bookInfoData = new BookInfoData(publisher, pages, publishedDate, title, subtitle, description, thumbnail, "0", "0");



        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spin);


        //Check book already exists in database
        final boolean[] checker = new boolean[1];
        final DatabaseReference checkRefI = databaseReference.child("books");
        checkRefI.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    checker[0] = true;
                }
                else
                {
                    checker[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Checks book already exists in list
        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    toastMaker("exists");
                }
                //Saves book to list
                else
                {
                    setButtonToRemove();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spin).push().setValue(bookInfoFirebase);

                    if(checker[0] == false)
                    {
                        databaseReference.child("books").push().setValue(bookInfoData);
                    }

                    if(spin == "read list")
                    {
                        int amount = Integer.parseInt(increaseRead);
                        amount = amount+1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("booksRead").setValue(amount);
                    }
                    toastMaker("not exist to add");

                    if(category.equals(null) || category.equals(""))
                    {

                    }
                    else
                    {
                        genreAdd();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //Adds genre to users information, which is used on the discover page to give book suggestions based on most popular genres
    private void genreAdd()
    {
        final DatabaseReference checkRef = databaseReference.child("users").child(firebaseUser.getUid());
        final DatabaseReference checkrRefI = checkRef.child("bookGenres");
        final boolean[] checker = new boolean[1];



            checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("bookGenres").exists())
                    {
                        checker[0] = true;
                    }
                    else
                    {
                        checker[0] = false;
                        checkRef.child("bookGenres").child(category).child("value").setValue(1);
                    }

                    if(checker[0] == true) {
                        checkrRefI.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(category).exists()) {

                                    String genreS = snapshot.child(category).child("value").getValue().toString();
                                    int genreInt = Integer.parseInt(genreS);
                                    genreInt++;
                                    checkRef.child("bookGenres").child(category).child("value").setValue(genreInt);
                                }
                                else
                                {
                                    checkRef.child("bookGenres").child(category).child("value").setValue(1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


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

    //Shows toast if book is added to list
    private void toastMaker(String checkC)
    {
        if(checkC == "not exist to add")
        {
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show();
        }
    }


    //Sets the users average and total score in firebase, which is shown on users profile page
    private void addRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
        final int numStars = (int) ratingBar.getRating();

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);


        final String[] key = new String[1];
        Query query = checkRef.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    key[0] = ds.getKey();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(key[0]).child("Rating").setValue(numStarsS);


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


                    String avgRatingSet = decimalFormat.format(numStars);

                    ratingTV.setText("Your Rating: " + avgRatingSet);

                    addBookRating();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //Sets the book's average score and score count in firebase
    private void addBookRating()
    {

        final int numStars = (int) ratingBar.getRating();

        final DatabaseReference checkRef = databaseReference.child("books");


        final String[] key = new String[1];
        Query query = checkRef.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    key[0] = ds.getKey();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookRatingAvg = snapshot.child("books").child(key[0]).child("rating").getValue().toString();
                bookRatingCount = snapshot.child("books").child(key[0]).child("ratingCount").getValue().toString();
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

                    int bookScoreCount = Integer.parseInt(bookRatingCount);
                    float bookRating = Float.parseFloat(bookRatingAvg);
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


                    databaseReference.child("books").child(key[0]).child("rating").setValue(bookRating);
                    databaseReference.child("books").child(key[0]).child("ratingCount").setValue(bookScoreCount);


                    String avgRatingSet = decimalFormat.format(bookRating);

                    avgRatingTV.setText("Average Rating: " + avgRatingSet);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}