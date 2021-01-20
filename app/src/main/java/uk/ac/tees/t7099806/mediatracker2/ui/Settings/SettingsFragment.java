package uk.ac.tees.t7099806.mediatracker2.ui.Settings;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import uk.ac.tees.t7099806.mediatracker2.LoginActivity;
import uk.ac.tees.t7099806.mediatracker2.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private SettingsViewModel mViewModel;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private Button buttonLogOut;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        buttonLogOut = view.findViewById(R.id.logoutButton);
        buttonLogOut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogOut)
        {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }

}