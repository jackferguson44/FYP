package uk.ac.tees.t7099806.mediatracker2.ui.Discover;

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

import uk.ac.tees.t7099806.mediatracker2.DiscoverShowsActivity;
import uk.ac.tees.t7099806.mediatracker2.R;


public class DiscoverFragment extends Fragment implements View.OnClickListener {

    private DiscoverViewModel discoverViewModel;

    private ImageView discoverBooks, discoverShows;
    private TextView discoverBooksText, discoverShowsText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discoverViewModel =
                ViewModelProviders.of(this).get(DiscoverViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discover, container, false);


        discoverBooks = root.findViewById(R.id.discover_books);
        discoverBooks.setOnClickListener(this);

        discoverBooksText = root.findViewById(R.id.discover_books_text);
        discoverBooksText.setOnClickListener(this);

        discoverShows = root.findViewById(R.id.discover_shows);
        discoverShows.setOnClickListener(this);

        discoverShowsText = root.findViewById(R.id.discover_shows_text);
        discoverShowsText.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == discoverBooks || v == discoverBooksText)
        {
            startActivity(new Intent(getActivity(), DiscoverShowsActivity.class));
        }
        if(v == discoverShows || v == discoverShowsText)
        {
            startActivity(new Intent(getActivity(), DiscoverShowsActivity.class));
        }

    }
}