package uk.ac.tees.t7099806.mediatracker2.ui.Lists;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import uk.ac.tees.t7099806.mediatracker2.PlanToWatchActivity;
import uk.ac.tees.t7099806.mediatracker2.PlantToReadActivity;
import uk.ac.tees.t7099806.mediatracker2.R;
import uk.ac.tees.t7099806.mediatracker2.ReadListActivity;
import uk.ac.tees.t7099806.mediatracker2.ReadingListActivity;
import uk.ac.tees.t7099806.mediatracker2.WatchingListActivity;

public class ListsFragment extends Fragment implements View.OnClickListener {

    private ListsViewModel listsViewModel;

    Button readButton, readingButton, planToReadButton, plantToWatchButton, watchingButton, watchedButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        listsViewModel = ViewModelProviders.of(this).get(ListsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lists, container, false);

        readButton = root.findViewById(R.id.readButton);
        readButton.setOnClickListener(this);
        readingButton = root.findViewById(R.id.readingButton);
        readingButton.setOnClickListener(this);
        planToReadButton = root.findViewById(R.id.planToReadButton);
        planToReadButton.setOnClickListener(this);
        plantToWatchButton = root.findViewById(R.id.plantToWatchButton);
        plantToWatchButton.setOnClickListener(this);
        watchingButton = root.findViewById(R.id.watchingButton);
        watchingButton.setOnClickListener(this);
        watchedButton = root.findViewById(R.id.watchedButton);
        watchedButton.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {
        if(v == readButton)
        {
            startActivity(new Intent(getActivity(), ReadListActivity.class));
        }
        if(v == readingButton)
        {
            startActivity(new Intent(getActivity(), ReadingListActivity.class));
        }
        if(v ==planToReadButton)
        {
            startActivity(new Intent(getActivity(), PlantToReadActivity.class));
        }
        if(v == plantToWatchButton)
        {
            startActivity(new Intent(getActivity(), PlanToWatchActivity.class));
        }
        if(v == watchingButton)
        {
            startActivity(new Intent(getActivity(), WatchingListActivity.class));
        }
    }
}