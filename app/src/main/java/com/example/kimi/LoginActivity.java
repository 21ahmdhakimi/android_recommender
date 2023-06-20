package com.example.kimi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailET, passET;
    private Button loginB;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        init();

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void init()
    {
        loginB = findViewById(R.id.buttonLogin);
        emailET = findViewById(R.id.emailEditTV);
        passET = findViewById(R.id.passEditTV);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loginUser()
    {
        String email = emailET.getText().toString().trim();
        String password = passET.getText().toString().trim();

        if(email.isEmpty()){
            emailET.setError("Email is Required!");
            emailET.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passET.setError("Password is Required!");
            passET.requestFocus();
            return;
        }

        if(password.length() < 6){
            passET.setError("Must have 6 characters");
            passET.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            finish();
                            startActivity(intent);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication Failure. Please check your credentials !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}