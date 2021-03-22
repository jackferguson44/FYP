package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
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

    //Gets information that is passed from MovieAdapter class
    private String title, genre, release, backDrop, overview, language, image;

    //Information to be displayed on screen
    private TextView titleTV, genreTV, releaseTV, runTimeTV, yourRatingTV, avgRatingTV, overviewTV;
    private ImageView backDropIV;

    private DatabaseReference databaseReference;
    private DatabaseReference checkInList;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Button buttonAddToList;
    private RatingBar ratingBar;
    private Spinner spinner;
    private String spinValue;

    //Keeps track of numbers of shows watched to decrease on increase if show is added or removed from watched list
    private String increaseWatched;
    //The users rating of the show
    private String changeMovieRating;
    //The average rating of the show
    private String changeAverageRating;
    //Amount of ratings users has done, used to work out the average rating the user has gave to shows
    private String increaseMovieScoreCount;
    //Amount of ratings show has, used to work out the average rating all users has gave a show
    private String increaseAverageScoreCount;

    //The shows average rating and count of ratings
    private String movieRatingAvg, movieRatingCount;

    //If true the button adds show to lists, if false the button removes show from lists
    private boolean buttonAdd;

    //Both used to get the show id
    private DatabaseReference movieRef;
    private String movieKey;
    private Query movieQuery;
    private boolean movieExists;

    //Usd to format the ratings of the show
    private DecimalFormat decimalFormat;
    private String avgMovieRatingSet;
    private float movieS;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_page);

        titleTV = findViewById(R.id.movieTitle);
        genreTV = findViewById(R.id.movieGenre);
        releaseTV = findViewById(R.id.movieRelease);
        runTimeTV = findViewById(R.id.runTime);
        yourRatingTV = findViewById(R.id.movieRating);
        avgRatingTV = findViewById(R.id.movieAverageRating);
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

        //used to check what list user has checked on dropdown
        spinValue ="plantowatchlist";

        //Used to create and make dropdown functional
        spinner = (Spinner) findViewById(R.id.spinnerAddShowToList);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MovieDetailsActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner_list_2));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();



        //Used to make rating bar functional
        ratingBar = findViewById(R.id.showRatingBar);
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

        //Used to set buttons functionality depending on whether show is in user's list or not
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

        //Used to get parent id of the show
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

        //used to get users list parent id
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

        //Used to set average and user rating
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

        //Save to list if show not in list
        if (v == buttonAddToList && buttonAdd == true)
        {
            saveToList(spinValue);
        }
        //Removes show from list if in list
        if(v == buttonAddToList && buttonAdd == false)
        {
            removeFromList();
        }

    }

    //Adds show to list
    private void saveToList(final String spin)
    {


        final MovieInformation movieInformation = new MovieInformation(title, image, overview, release, language, genre, backDrop);
        final MovieInfoData movieInfoData = new MovieInfoData(title, image, overview, release, language, genre, backDrop, "0", "0");

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spin);


        //Check show already exists in database
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

        //Checks show already exists in list
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

    //Adds genre to users information, which is used on the discover page to give show suggestions based on most popular genres
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
                }
                else
                {
                    checker[0] = false;
                    checkRef.child("genres").child(genre).child("value").setValue(1);
                }

                if(checker[0] == true) {
                    checkrRefI.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(genre).exists()) {
                                String genreS = snapshot.child(genre).child("value").getValue().toString();
                                int genreInt = Integer.parseInt(genreS);
                                genreInt++;
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
    }

    //Removes show from list
    private void removeFromList()
    {

        final DatabaseReference checkRef = databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue);

        //Used to check if show is in list
        checkRef.orderByChild("image").equalTo(image).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String parentI = "";

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    parentI = dataSnapshot.getKey();
                }
                //Removes show from list
                if(snapshot.exists())
                {

                    setButtonToAdd();
                    databaseReference.child("lists").child(firebaseUser.getUid()).child(spinValue).child(parentI).removeValue();
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

    //Sets the users average and total score in firebase, which is shown on users profile page
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

    //Sets the show's average score and score count in firebase
    private void addMovieRating()
    {
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



    //Make the buttons functionality adding show to list
    private void setButtonToAdd()
    {
        buttonAddToList.setText("Add to list");
        buttonAdd = true;
    }

    //Make the buttons functionality removing show from list
    private void setButtonToRemove()
    {
        buttonAddToList.setText("Remove from list");
        buttonAddToList.setTextSize(12);
        buttonAdd = false;
    }

    //Shows toast if show is added to list
    private void toastMaker(String checkC)
    {
        if(checkC == "not exist to add")
        {
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //calls changeButton to change button functionality if show is or isn't in chosen list
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

        //gets the parent id of the users list
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

    //Checks if show is/isn't in chosen list, changes button functionality based on result
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


    //Gets the parent ID of the show
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

    //Sets the average rating of the show
    private void setMovieAvgRating(DataSnapshot snapshot)
    {

        if(movieExists == true)
        {

            movieS = Float.parseFloat(snapshot.child("shows").child(movieKey).child("rating").getValue().toString());
            avgMovieRatingSet = decimalFormat.format(movieS);
            avgRatingTV.setText("Average rating: " + avgMovieRatingSet);

        }
    }

    //Sets the user rating of the show
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
                        yourRatingTV.setText("Your rating: " + avgListRatingSet);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}