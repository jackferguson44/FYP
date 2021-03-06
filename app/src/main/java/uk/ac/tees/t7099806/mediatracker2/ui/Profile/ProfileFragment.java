package uk.ac.tees.t7099806.mediatracker2.ui.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import uk.ac.tees.t7099806.mediatracker2.EditProfileActivity;
import uk.ac.tees.t7099806.mediatracker2.LoginActivity;
import uk.ac.tees.t7099806.mediatracker2.R;
import uk.ac.tees.t7099806.mediatracker2.UserInformation;

public class ProfileFragment extends Fragment implements  View.OnClickListener{



    private TextView user_name, user_join_date, booksReadText, booksScoreText, showsWatchedText, timeWatchedText, showsScoredText, averageScoreText, navUserName;
    private Button buttonEditProfile;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference ref;
    private String userId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("users");
        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();



        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                showData(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        user_name = view.findViewById(R.id.user_name);
        user_join_date = view.findViewById(R.id.user_join_date);
        booksReadText = view.findViewById(R.id.booksReadText);
        booksScoreText = view.findViewById(R.id.booksScoreText);
        showsWatchedText = view.findViewById(R.id.showsWatchedText);
        timeWatchedText = view.findViewById(R.id.timeWatchedText);
        showsScoredText= view.findViewById(R.id.showsScoredText);
        averageScoreText = view.findViewById(R.id.averageScoreText);



        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

    private void showData(DataSnapshot dataSnapshot)
    {

        user_name.setText(dataSnapshot.child(userId).getValue(UserInformation.class).getUserName());
        user_join_date.setText("Date Joined: " + dataSnapshot.child(userId).getValue(UserInformation.class).getDate());
        booksReadText.setText(dataSnapshot.child(userId).child("booksRead").getValue().toString());

        float bockScore = Float.parseFloat(dataSnapshot.child(userId).child("bookScore").getValue().toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String bockScoreString = decimalFormat.format(bockScore);
        booksScoreText.setText(bockScoreString);
        checkZero(bockScoreString, booksScoreText);


        showsWatchedText.setText(dataSnapshot.child(userId).child("showsWatched").getValue().toString());

        float showScore = Float.parseFloat(dataSnapshot.child(userId).child("showScore").getValue().toString());
        String showScoreString = decimalFormat.format(showScore);
        showsScoredText.setText(showScoreString);
        checkZero(showScoreString, showsScoredText);


        float averageScore = Float.parseFloat(dataSnapshot.child(userId).child("totalScore").getValue().toString());
        String averageScoreString = decimalFormat.format(averageScore);
        checkZero(averageScoreString, averageScoreText);


    }


    private void checkZero(String scoreT, TextView scoreTV)
    {
        if(scoreT.equals(".00"))
        {
            scoreTV.setText("0");
        }
        else
        {
            scoreTV.setText(scoreT);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonEditProfile)
        {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }

    }
}