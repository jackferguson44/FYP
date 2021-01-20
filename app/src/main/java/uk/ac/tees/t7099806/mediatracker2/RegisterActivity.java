package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonSignIn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }


        buttonRegister = findViewById(R.id.buttonRegister);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    @Override
    public void onClick(View v)
    {
        if(v == buttonRegister)
        {
            register();
        }
        if (v == buttonSignIn)
        {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    private void register()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            // empty email
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            // stop
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            //empty password
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            // stop
            return;
        }

        //if validations pass
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.sendEmailVerification();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
                else
                {
                    String fail = "Register Account Failed" + task.getException();
                    Toast.makeText(RegisterActivity.this, fail, Toast.LENGTH_SHORT).show();
                }
            }

        });

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}