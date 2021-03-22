package uk.ac.tees.t7099806.mediatracker2;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;



public class PlanToWatchActivity extends AppCompatActivity {


    private MovieInformation movieInformation;
    private MovieListAdapter movieListAdapter;
    private ArrayList<MovieInformation> movieInformationArrayList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    private FirebaseAuth auth;
    private DatabaseReference rootRef;
    private DatabaseReference listsRef;
    private String userID;

    ValueEventListener valueEventListener;

    private String name;
    private String image;
    private String overview;
    private String releaseDate;
    private String language;
    private String genre;
    private String backDropPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Plan To Watch List");

        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = auth.getCurrentUser();
        userID = user.getUid();
        listsRef = rootRef.child("lists").child(userID).child("plantowatchlist");




        movieInformationArrayList = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    name = ds.child("name").getValue(String.class);
                    System.out.println("title = " + name);
                    genre = ds.child("genre").getValue(String.class);
                    image = ds.child("image").getValue(String.class);
                    language = ds.child("language").getValue(String.class);
                    backDropPath = ds.child("backDropPath").getValue(String.class);
                    overview = ds.child("overview").getValue(String.class);
                    releaseDate = ds.child("releaseDate").getValue(String.class);




                    movieInformation = new MovieInformation(name, image, overview, releaseDate, language, genre, backDropPath);
                    movieInformationArrayList.add(movieInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
       listsRef.addListenerForSingleValueEvent(valueEventListener);



        movieListAdapter = new MovieListAdapter(movieInformationArrayList, PlanToWatchActivity.this, "");

        linearLayoutManager = new LinearLayoutManager(PlanToWatchActivity.this, RecyclerView.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.readListRec);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieListAdapter);

    }
}