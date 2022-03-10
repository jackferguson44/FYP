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

    public DatabaseReference databaseReference, ref;

    private EditText editTextUsername, editTextPhone;
    private Button buttonSave;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String userId;



    private boolean accountExists, userNameExists;
    private String joinDate;
//
//    private String userNameI;
    private String userNameOriginal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        ref = databaseReference.child("users");

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);

        //userNameI = editTextUsername.getText().toString().trim();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        buttonSave.setOnClickListener(this);

        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //
            }
        };






    }



    private void saveInformation()
    {

        String userName = editTextUsername.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String pattern = "dd-MM-yyyy";
        //date joined is wrong
        String date = new SimpleDateFormat(pattern).format(new Date());


        if(userNameExists == true)
        {
            UserInformation userInformation = new UserInformation(userName, phone, joinDate);
            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
        }
        else
        {
            UserInformation userInformation = new UserInformation(userName, phone, date);
            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
        }

//        if(userNameExists == false)
//        {
//            UserInformation userInformation = new UserInformation(userName, phone, date);
//            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
//            databaseReference.child("userNames").child(userName).setValue(userName);
        //}
//        else
//        {
//            UserInformation userInformation = new UserInformation(userName, phone, joinDate);
//            databaseReference.child("users").child(user.getUid()).setValue(userInformation);
          //  databaseReference.child("userNames").child(userName).setValue(userName);
//        }
        Toast.makeText(this, "Details Saved", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public void onClick(View v) {
        if(v == buttonSave)
        {
            saveInformation();

//            userNameI = editTextUsername.getText().toString().trim();
//            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//            ref.child("userNames").child(userNameI).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    System.out.println("userNameOriginal outside if = " + userNameOriginal);
//
//                    //if user name isn't changed no crash, crash if user name changed because of null object reference
//
//
//                    if(dataSnapshot.getValue().toString().trim().equals(userNameOriginal))
//                    {
//                        System.out.println("userNameOriginal= " + userNameOriginal);
//                       //System.out.println(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getUserName());
//                        userNameExists = false;
//                        saveInformation();
//                    }
//                    else if(dataSnapshot.exists())
//                    {
//                        userNameExists = true;
//                        System.out.println("usernameI = " + userNameI);
//                        System.out.println("true");
//                        //System.out.println("datasnapshot == " + dataSnapshot.getValue().toString());
//                        saveInformation();
//                    }
//                    else
//                    {
//                        userNameExists = false;
//                        System.out.println("usernameI = " + userNameI);
//                        System.out.println("false");
//                        saveInformation();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });



        }

    }


    private void check()
    {

    }


    private void showData(DataSnapshot dataSnapshot)
    {

//        joinDate = (dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getDate());
//        userNameOriginal = (dataSnapshot.child(userId).getValue(UserInformation.class).getUserName());


        if(dataSnapshot.child("users").child(userId).exists())
        {
            editTextUsername.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getUserName());
            editTextPhone.setText(dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getPhone());
            joinDate = (dataSnapshot.child("users").child(userId).getValue(UserInformation.class).getDate());
            editTextUsername.setText(userNameOriginal);
            editTextPhone.setText(dataSnapshot.child(userId).getValue(UserInformation.class).getPhone());
            userNameExists = true;
            System.out.println("exists bruh");

        }
        else
        {
            System.out.println("does no exists bruh");
            userNameExists = false;
        }

//
//
//        if(dataSnapshot.child("userNames").child(userNameI).exists())
//        {
//            userNameExists = true;
//            System.out.println("user name exists");
//        }
//        else
//        {
//            userNameExists = false;
//            System.out.println("user name does not exists");
//        }
    }







    @Override
    public void onBackPressed()
    {
        user.delete();
        startActivity(new Intent(this, RegisterActivity.class));
        super.onBackPressed();
    }
}