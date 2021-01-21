package uk.ac.tees.t7099806.mediatracker2.ui.Profile;

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

import uk.ac.tees.t7099806.mediatracker2.EditProfileActivity;
import uk.ac.tees.t7099806.mediatracker2.R;

public class ProfileFragment extends Fragment implements  View.OnClickListener{

    private ProfileViewModel profileViewModel;

    private Button buttonEditProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
               // ViewModelProviders.of(this).get(ProfileViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonEditProfile)
        {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }

    }
}