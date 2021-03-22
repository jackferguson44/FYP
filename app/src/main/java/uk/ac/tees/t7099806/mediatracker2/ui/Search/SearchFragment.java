package uk.ac.tees.t7099806.mediatracker2.ui.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import uk.ac.tees.t7099806.mediatracker2.R;
import uk.ac.tees.t7099806.mediatracker2.SearchBooksActivity;
import uk.ac.tees.t7099806.mediatracker2.SearchMoviesActivity;

public class SearchFragment extends Fragment implements View.OnClickListener {



    private ImageView searchBooks, searchShows;
    private TextView searchBooksText, searchShowsText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);


        searchBooks = root.findViewById(R.id.search_books);
        searchBooks.setOnClickListener(this);

        searchBooksText = root.findViewById(R.id.search_books_text);
        searchBooksText.setOnClickListener(this);

        searchShows = root.findViewById(R.id.search_shows);
        searchShows.setOnClickListener(this);

        searchShowsText = root.findViewById(R.id.search_shows_text);
        searchShowsText.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == searchBooks || v == searchBooksText)
        {
            startActivity(new Intent(getActivity(), SearchBooksActivity.class));
        }
        if(v == searchShows || v == searchShowsText)
        {
            startActivity(new Intent(getActivity(), SearchMoviesActivity.class));
        }

    }
}