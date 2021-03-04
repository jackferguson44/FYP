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
    private String userId;


    //used to get parent of users book in list in firebase
    private String[] parent;
    //used to get parent of book in list in firebase
    private String[] bookParent;


    private String title, subtitle, publisher, publishedDate, description, thumbnail;
    private int pageCount;
    private ArrayList<String> authors;

    private TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV, ratingTV, avgRatingTV;
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

    private int i;

    private String bookRatingAvg, bookRatingCount;


    private DatabaseReference bookImage;
    private String bookKey;
    private Query bookQuery;
    private boolean bookExists;


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
        ratingTV = findViewById(R.id.TVRating);
        avgRatingTV = findViewById(R.id.TVAverageRating);

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


        parent = new String[999];
        bookParent = new String[1];
        i = 0;

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


        bookImage = databaseReference.child("books");
        //private String bookKey;
        bookQuery = bookImage.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    bookKey = ds.getKey();
                    System.out.println("bookKey: " + bookKey);
                    if(ds.exists())
                    {

                        bookExists = true;
                    }
                    else
                    {
                        bookExists = false;
                        System.out.println("bookKey: " + bookKey);
                    }

                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        bookQuery.addListenerForSingleValueEvent(valueEventListener);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(bookExists == true)
                {
                    avgRatingTV.setText(snapshot.child("books").child(bookKey).child("rating").getValue().toString());
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
            saveToList(spinValue);
        }
        if(v == buttonAddToList && buttonAdd == false)
        {
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
                    setButtonToAdd();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parent[0]).removeValue();
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


    private void saveToList(final String spin)
    {

        String pages = String.valueOf(pageCount);
        final BookInfoFirebase bookInfoFirebase = new BookInfoFirebase(publisher, pages, publishedDate, title, subtitle, description, thumbnail);
        final BookInfoData bookInfoData = new BookInfoData(publisher, pages, publishedDate, title, subtitle, description, thumbnail, "0", "0");

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spin);


        //check book already exists in database
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


        checkRef.orderByChild("bookImage").equalTo(thumbnail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    toastMaker("exists");
                }
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



//    private boolean checkBookExists(String bookI)
//    {
//
//    }


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
          //  Toast.makeText(this, "already exists in list", Toast.LENGTH_SHORT).show();
        }
        else if(checkC == "not exist to add")
        {
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(this, "This book has already been removed", Toast.LENGTH_LONG).show();
        }

    }


    private void addRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
        final int numStars = (int) ratingBar.getRating();

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);

        final String parentAdd;



        final String[] key = new String[1];
        Query query = checkRef.orderByChild("bookImage").equalTo(thumbnail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    key[0] = ds.getKey();

                    final DatabaseReference checkRatingExists;
                    checkRatingExists = checkRef.child(key[0]);

                    checkRatingExists.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot data: snapshot.getChildren())
                            {
                                if(data.child("Rating").exists())
                                {

                                    System.out.println("Rating exists");
                                }
                                else
                                {
                                    System.out.println("Rating dont exist");
                                }
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

                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String avgRatingSet = decimalFormat.format(numStars);

                    ratingTV.setText("Your Rating: " + avgRatingSet);

                    addBookRating();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        i = 0;
    }






    private void addBookRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
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
                 //   databaseReference.child("books").child(parent[0]).child("Rating").setValue(numStarsS);


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


                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String avgRatingSet = decimalFormat.format(bookRating);

                    avgRatingTV.setText("Average Rating: " + avgRatingSet);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDataBook(DataSnapshot dataSnapshot, String bookA, String bookT)
    {
        bookA = databaseReference.child("books").child(bookParent[0]).child("rating").toString();
        bookT = databaseReference.child("books").child(bookParent[0]).child("ratingCount").toString();
    }





}