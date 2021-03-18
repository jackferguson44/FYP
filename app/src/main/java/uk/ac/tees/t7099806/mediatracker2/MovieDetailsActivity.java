package uk.ac.tees.t7099806.mediatracker2;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MovieDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,  View.OnClickListener {

    private String title, genre, release, runTime, yourRating, avgRating, description, backDrop, overview, language, image;

    private TextView titleTV, genreTV, releaseTV, runTimeTV, yourRatingTV, avgRatingTV, descriptionTV, overviewTV;
    private ImageView backDropIV;

    private DatabaseReference databaseReference;
    private DatabaseReference checkInList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //used to get parent of users show/movie in list in firebase
    private String[] parent;
    //used to get parent of show/movie in list in firebase
    private String[] movieParent;


    private Button buttonAddToList;
    private RatingBar ratingBar;
    private Spinner spinner;
    private String spinValue;

    private String increaseWatched;
    private String changeMovieRating;
    private String changeAverageRating;
    private String increaseMovieScoreCount;
    private String increaseAverageScoreCount;
    private String movieRatingAvg, movieRatingCount;

    private boolean buttonAdd;

    private DatabaseReference movieRef;
    private String movieKey;
    private Query movieQuery;
    private boolean movieExists;
    private DecimalFormat decimalFormat;
    private String avgMovieRatingSet;
    private float movieS;

    private DatabaseReference listRef;
    private String listKey;
    private Query listQuery;
    private boolean listExist;
    private String avgListRatingSet;
    private float listS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_page);

        titleTV = findViewById(R.id.movieTitle);
        genreTV = findViewById(R.id.movieGenre);
        releaseTV = findViewById(R.id.movieRelease);
        runTimeTV = findViewById(R.id.runTime);
        yourRatingTV = findViewById(R.id.movieRating);
        avgRatingTV = findViewById(R.id.movieAverageRating);
        descriptionTV = findViewById(R.id.movieDescription);
        overviewTV = findViewById(R.id.movieDescription);
        backDropIV = findViewById(R.id.movieImage);

        title = getIntent().getStringExtra("title");
        genre = getIntent().getStringExtra("genre");
        release = getIntent().getStringExtra("release date");
        overview = getIntent().getStringExtra("overview");
        backDrop = getIntent().getStringExtra("backdrop");
        image = getIntent().getStringExtra("image");
        language = getIntent().getStringExtra("language");

        titleTV.setText(title);
        genreTV.setText("Genre: " + genre);
        releaseTV.setText("Release Date: " +release);
        overviewTV.setText(overview);
        runTimeTV.setText("Runtime: ");

        if(backDrop == "null")
        {
            Picasso.get().load("https://image.tmdb.org/t/p/w500/" + image).into(backDropIV);
        }
        else
        {
            Picasso.get().load("https://image.tmdb.org/t/p/w500/" + backDrop).into(backDropIV);
        }


        buttonAddToList = findViewById(R.id.buttonAddShowToList);
        buttonAddToList.setOnClickListener(this);

        decimalFormat =  new DecimalFormat("#.00");

        spinValue ="plantowatchlist";

        spinner = (Spinner) findViewById(R.id.spinnerAddShowToList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MovieDetailsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner_list_2));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();

        parent = new String[999];
        movieParent = new String[1];

        ratingBar = findViewById(R.id.showRatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                 addRating();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        checkInList = databaseReference.child("lists").child(firebaseUser.getUid()).child("plantowatchlist");
        checkInList.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
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

        movieRef = databaseReference.child("shows");
        movieQuery = movieRef.orderByChild("image").equalTo(image);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               getMovieId(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        movieQuery.addListenerForSingleValueEvent(valueEventListener);


        listRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);
        listQuery = listRef.orderByChild("image").equalTo(image);
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
                setMovieAvgRating(snapshot);
                setListAvgRating(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showData(DataSnapshot dataSnapshot)
    {
        increaseWatched = dataSnapshot.child("users").child(firebaseUser.getUid()).child("showsWatched").getValue().toString();
        changeMovieRating = dataSnapshot.child("users").child(firebaseUser.getUid()).child("showScore").getValue().toString();
        changeAverageRating = dataSnapshot.child("users").child(firebaseUser.getUid()).child("totalScore").getValue().toString();
        increaseMovieScoreCount =  dataSnapshot.child("users").child(firebaseUser.getUid()).child("showScoreCount").getValue().toString();
        increaseAverageScoreCount = dataSnapshot.child("users").child(firebaseUser.getUid()).child("totalScoreCount").getValue().toString();


    }


    @Override
    public void onClick(View v) {
        if (v == buttonAddToList && buttonAdd == true)
        {
            saveToList(spinValue);
        }
        if(v == buttonAddToList && buttonAdd == false)
        {
            removeFromList();
        }

    }

    private void saveToList(final String spin)
    {


        final MovieInformation movieInformation = new MovieInformation(title, image, overview, release, language, genre, backDrop);
        final MovieInfoData movieInfoData = new MovieInfoData(title, image, overview, release, language, genre, backDrop, "0", "0");

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spin);


        //check book already exists in database
        final boolean[] checker = new boolean[1];
        final DatabaseReference checkRefI = databaseReference.child("movies");
        checkRefI.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
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


        checkRef.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    toastMaker("exists");
                }
                else
                {
                    setButtonToRemove();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spin).push().setValue(movieInformation);

                    if(checker[0] == false)
                    {
                        databaseReference.child("shows").push().setValue(movieInfoData);
                    }

                    if(spin == "watched list")
                    {
                        int amount = Integer.parseInt(increaseWatched);
                        amount = amount+1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("showsWatched").setValue(amount);
                    }
                    toastMaker("not exist to add");
                    genreAdd();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void genreAdd()
    {
        final DatabaseReference checkRef = databaseReference.child("users").child(firebaseUser.getUid());
        final DatabaseReference checkrRefI = checkRef.child("genres");
        final boolean[] checker = new boolean[1];

        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("genres").exists())
                {
                    checker[0] = true;
                    System.out.println("should be first");
                }
                else
                {
                    checker[0] = false;
                    checkRef.child("genres").child(genre).child("value").setValue(1);
                }

                if(checker[0] == true) {
                    System.out.println("should be second");
                    checkrRefI.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(genre).exists()) {

                                System.out.println(genre);
                                String genreS = snapshot.child(genre).child("value").getValue().toString();
                                int genreInt = Integer.parseInt(genreS);
                                genreInt++;
                                System.out.println("genreint " + genreInt);
                                checkRef.child("genres").child(genre).child("value").setValue(genreInt);
                            }
                            else
                            {
                                checkRef.child("genres").child(genre).child("value").setValue(1);
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





        //checkrRefI.orderByChild("gen")


    }

    private void removeFromList()
    {

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);



        final Query query = checkRef.orderByChild("image");
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

        checkRef.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    setButtonToAdd();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parent[0]).removeValue();
                    if(spinValue == "watched list")
                    {

                        int amount = Integer.parseInt(increaseWatched);
                        amount = amount-1;
                        databaseReference.child("users").child(firebaseUser.getUid()).child("showsWatched").setValue(amount);


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

    private void addRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
        final int numStars = (int) ratingBar.getRating();

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);




        final String[] key = new String[1];
        Query query = checkRef.orderByChild("image").equalTo(image);
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

                                }
                                else
                                {

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


        checkRef.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(key[0]).child("Rating").setValue(numStarsS);


                    int showScoreCount = Integer.parseInt(increaseMovieScoreCount);
                    float showRating = Float.parseFloat(changeMovieRating);
                    showRating = showRating * showScoreCount;
                    showRating = showRating + numStars;
                    showScoreCount++;
                    showRating = showRating / showScoreCount;

                    int totalScoreCount = Integer.parseInt(increaseAverageScoreCount);
                    float averageRating = Float.parseFloat(changeAverageRating);
                    averageRating = averageRating * totalScoreCount;
                    averageRating = averageRating + numStars;
                    totalScoreCount++;
                    averageRating = averageRating / totalScoreCount;


                    databaseReference.child("users").child(firebaseUser.getUid()).child("showScoreCount").setValue(showScoreCount);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("showScore").setValue(showRating);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("totalScoreCount").setValue(totalScoreCount);
                    databaseReference.child("users").child(firebaseUser.getUid()).child("totalScore").setValue(averageRating);


                    String avgRatingSet = decimalFormat.format(numStars);

                    yourRatingTV.setText("Your Rating: " + avgRatingSet);

                    addMovieRating();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addMovieRating()
    {
        final String numStarsS = String.valueOf(ratingBar.getRating());
        final int numStars = (int) ratingBar.getRating();

        final DatabaseReference checkRef = databaseReference.child("shows");


        final String[] key = new String[1];
        Query query = checkRef.orderByChild("image").equalTo(image);
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
                movieRatingAvg = snapshot.child("shows").child(key[0]).child("rating").getValue().toString();
                movieRatingCount = snapshot.child("shows").child(key[0]).child("ratingCount").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        checkRef.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //   databaseReference.child("books").child(parent[0]).child("Rating").setValue(numStarsS);


                    int movieScoreCount = Integer.parseInt(movieRatingCount);
                    float movieRating = Float.parseFloat(movieRatingAvg);
                    movieRating = movieRating * movieScoreCount;
                    movieRating = movieRating + numStars;
                    movieScoreCount++;
                    movieRating = movieRating / movieScoreCount;

                    int totalScoreCount = Integer.parseInt(increaseAverageScoreCount);
                    float averageRating = Float.parseFloat(changeAverageRating);
                    averageRating = averageRating * totalScoreCount;
                    averageRating = averageRating + numStars;
                    totalScoreCount++;
                    averageRating = averageRating / totalScoreCount;


                    databaseReference.child("shows").child(key[0]).child("rating").setValue(movieRating);
                    databaseReference.child("shows").child(key[0]).child("ratingCount").setValue(movieScoreCount);


                    String avgRatingSet = decimalFormat.format(movieRating);

                    avgRatingTV.setText("Average Rating: " + avgRatingSet);



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
        buttonAddToList.setText("Remove from list");
        buttonAddToList.setTextSize(12);
        buttonAdd = false;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getItemAtPosition(position).equals("Plan To Watch"))
        {
            spinValue = "plantowatchlist";
            changeButton("plantowatchlist");
        }
        else if(parent.getItemAtPosition(position).equals("Watched"))
        {
            spinValue = "watched list";
            changeButton("watched list");
        }
        else
        {
            spinValue = "watching list";
            changeButton("watching list");
        }

    }

    private void changeButton(String list)
    {
        checkInList = databaseReference.child("lists").child(firebaseUser.getUid()).child(list);
        checkInList.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
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


    private void getMovieId(DataSnapshot snapshot)
    {
        for(DataSnapshot ds : snapshot.getChildren())
        {

            movieKey = ds.getKey();
            if(ds.exists())
            {
                movieExists = true;
            }
            else
            {
                movieExists = false;
            }

        }
    }

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

    private void setMovieAvgRating(DataSnapshot snapshot)
    {

        if(movieExists == true)
        {

            movieS = Float.parseFloat(snapshot.child("shows").child(movieKey).child("rating").getValue().toString());
            avgMovieRatingSet = decimalFormat.format(movieS);
            avgRatingTV.setText("Average rating: " + avgMovieRatingSet);

        }
    }

    private void setListAvgRating(DataSnapshot snapshot)
    {
        if(listExist == true)
        {
            listS = Float.parseFloat(snapshot.child("lists").child(firebaseUser.getUid()).child(spinValue).child(listKey).child("Rating").getValue().toString());
            avgListRatingSet = decimalFormat.format(listS);
            yourRatingTV.setText("Your rating: " + avgListRatingSet);
        }
    }
}