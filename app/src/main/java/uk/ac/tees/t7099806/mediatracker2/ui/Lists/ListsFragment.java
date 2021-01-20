package uk.ac.tees.t7099806.mediatracker2.ui.Lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import uk.ac.tees.t7099806.mediatracker2.R;

public class ListsFragment extends Fragment {

    private ListsViewModel listsViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        listsViewModel = ViewModelProviders.of(this).get(ListsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lists, container, false);
        final TextView textView = root.findViewById(R.id.text_lists);
        listsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

   

}