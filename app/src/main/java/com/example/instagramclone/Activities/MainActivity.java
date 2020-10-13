package com.example.instagramclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private EditText email;
    private EditText pass;
    private Button loginButton;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        createButton = findViewById(R.id.createAcc);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Not signed in", Toast.LENGTH_SHORT).show();
                }
            }
        };

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString())
                        && !TextUtils.isEmpty(pass.getText().toString())) {

                    String emailString = email.getText().toString();
                    String passString = pass.getText().toString();

                    login(emailString, passString);
                } else {

                }
            }
        });
    }

    private void login(String emailString, String passString) {
        auth.signInWithEmailAndPassword(emailString, passString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "signed in", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MainActivity.this, PostListActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            auth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}