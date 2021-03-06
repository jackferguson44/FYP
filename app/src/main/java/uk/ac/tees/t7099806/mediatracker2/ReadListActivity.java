package uk.ac.tees.t7099806.mediatracker2;

import android.animation.ValueAnimator;
import android.net.UrlQuerySanitizer;
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
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class ReadListActivity extends AppCompatActivity {

    private BookInfoFirebase bookInformation;
    private BookListAdapter adapter;
    private ArrayList<BookInfoFirebase> bookInfoFirebaseArrayList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;


    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private DatabaseReference listsRef;
    private String userID;

    ValueEventListener valueEventListener;

    private String publisher;
    private String noPages;
    private String publishDate;
    private String title;
    private String subTitle;
    private String description;
    private String bookImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Read List");


        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        listsRef = rootRef.child("lists").child(userID).child("read list");


        bookInfoFirebaseArrayList = new ArrayList<>();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    bookImage = ds.child("bookImage").getValue(String.class);
                    description = ds.child("description").getValue(String.class);
                    noPages = ds.child("numberOfPages").getValue(String.class);
                    publishDate = ds.child("publishDate").getValue(String.class);
                    publisher = ds.child("publisher").getValue(String.class);
                    subTitle = ds.child("subTitle").getValue(String.class);
                    title = ds.child("title").getValue(String.class);



                    bookInformation = new BookInfoFirebase(publisher, noPages, publishDate, title, subTitle, description, bookImage);
                    bookInfoFirebaseArrayList.add(bookInformation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        listsRef.addListenerForSingleValueEvent(valueEventListener);

        adapter = new BookListAdapter(bookInfoFirebaseArrayList, ReadListActivity.this);

        linearLayoutManager = new LinearLayoutManager(ReadListActivity.this, RecyclerView.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.readListRec);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


}