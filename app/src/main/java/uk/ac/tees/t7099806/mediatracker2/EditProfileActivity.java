package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public DatabaseReference databaseReference;

    private EditText editTextUsername, editTextPhone;
    private Button buttonSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);

        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //
            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }



    private void saveInformation()
    {
        String userName = editTextUsername.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        UserInformation userInformation = new UserInformation(userName, phone);


        databaseReference.child("users").child(user.getUid()).push().setValue(userInformation);
        Toast.makeText(this, "Details Saved", Toast.LENGTH_SHORT).show();
    }

    private void showData(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.child("users").child(userId).exists())
        {
            editTextUsername.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getUserName());
            editTextPhone.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSave)
        {
            saveInformation();
            //startActivity(new Intent(this, MainActivity.class));
        }

    }
}