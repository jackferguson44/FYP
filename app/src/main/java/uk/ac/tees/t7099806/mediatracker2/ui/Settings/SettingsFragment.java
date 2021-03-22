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

import com.google.firebase.auth.FirebaseAuth;

import uk.ac.tees.t7099806.mediatracker2.LoginActivity;
import uk.ac.tees.t7099806.mediatracker2.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {



    private FirebaseAuth firebaseAuth;
    private Button buttonLogOut;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        buttonLogOut = view.findViewById(R.id.logoutButton);
        buttonLogOut.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogOut)
        {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }

}