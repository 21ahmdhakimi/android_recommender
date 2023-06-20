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

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditTV, emailEditTV, passEditTV, conPassEditTV;
    private Button signupB;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        init();

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });
    }

    private void init()
    {
        usernameEditTV = findViewById(R.id.usernameEditTV);
        emailEditTV = findViewById(R.id.emailEditTV);
        passEditTV = findViewById(R.id.passEditTV);
        conPassEditTV = findViewById(R.id.conPassEditTV);
        signupB = findViewById(R.id.buttonSignup);
        progressBar = findViewById(R.id.progressBar);

    }

    private void signupUser()
    {
        String username = usernameEditTV.getText().toString().trim();
        String email = emailEditTV.getText().toString().trim();
        String password = passEditTV.getText().toString().trim();
        String confirmPassword = conPassEditTV.getText().toString().trim();

        if(username.isEmpty()){
            usernameEditTV.setError("Username is Required!");
            usernameEditTV.requestFocus();
            return;
        }

        if(email.isEmpty()){
            emailEditTV.setError("Email is Required!");
            emailEditTV.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passEditTV.setError("Password is Required!");
            passEditTV.requestFocus();
            return;
        }

        if(password.length() < 6){
            passEditTV.setError("Must has 6 characters");
            passEditTV.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            conPassEditTV.setError("Password Needs to be Confirmed!!");
            conPassEditTV.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            conPassEditTV.setError("Password Does Not Match!");
            conPassEditTV.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(email, username, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    SignUpActivity.this.finish();
                                }

                                @Override
                                public void onFailure() {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Authentication Failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Signup Failure! Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}