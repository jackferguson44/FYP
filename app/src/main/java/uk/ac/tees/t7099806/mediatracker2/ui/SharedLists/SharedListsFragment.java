package uk.ac.tees.t7099806.mediatracker2.ui.SharedLists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import uk.ac.tees.t7099806.mediatracker2.R;

public class SharedListsFragment extends Fragment {

    private SharedListsViewModel mViewModel;

    public static SharedListsFragment newInstance() {
        return new SharedListsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shared_lists, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SharedListsViewModel.class);
        // TODO: Use the ViewModel
    }

}