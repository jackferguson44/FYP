package uk.ac.tees.t7099806.mediatracker2;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        bookInformation = new BookInfoFirebase("VIM", "7", "07/09/12", "Tomie", "Junji Collection" ,"Worth the read", "https://books.google.com/books?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        bookInfoFirebaseArrayList = new ArrayList<>();
        bookInfoFirebaseArrayList.add(bookInformation);

        adapter = new BookListAdapter(bookInfoFirebaseArrayList, ReadListActivity.this);

        linearLayoutManager = new LinearLayoutManager(ReadListActivity.this, RecyclerView.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.readListRec);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);





    }
}