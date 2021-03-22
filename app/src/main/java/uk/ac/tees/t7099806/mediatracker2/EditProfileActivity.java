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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public DatabaseReference databaseReference;

    private EditText editTextUsername, editTextPhone;
    private Button buttonSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String userId;

    private boolean accountExists;
    private String joinDate;


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




    }



    private void saveInformation()
    {
        String userName = editTextUsername.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String pattern = "dd-MM-yyyy";
        String date = new SimpleDateFormat(pattern).format(new Date());

        if(accountExists == false)
        {
            UserInformation userInformation = new UserInformation(userName, phone, date);
            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
        }
        else
        {
            UserInformation userInformation = new UserInformation(userName, phone, joinDate);
            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
        }
        Toast.makeText(this, "Details Saved", Toast.LENGTH_SHORT).show();
    }


    private void showData(DataSnapshot dataSnapshot)
    {
        if(dataSnapshot.child("users").child(userId).exists())
        {
            editTextUsername.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getUserName());
            editTextPhone.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getPhone());
            joinDate = (dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getDate());
            accountExists = true;
        }
        else
        {
            accountExists = false;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonSave)
        {
            saveInformation();
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}