package uk.ac.tees.t7099806.mediatracker2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonRegister;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);



        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonSignIn.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v == buttonSignIn)
        {
            logIn();
        }
        if (v == buttonRegister)
        {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }


    }

    private void logIn()
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
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}