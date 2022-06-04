package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    EditText signUpEmail, signUpPassword;
    Button signUpButton;
    ProgressBar progressBar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpEmail = findViewById(R.id.editTextEmailSigup);
        signUpPassword = findViewById(R.id.editTextSignupPassword);
        signUpButton = findViewById(R.id.buttonSignup);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setClickable(false);
                String userEmail = signUpEmail.getText().toString();
                String userPassword = signUpPassword.getText().toString();
                signUpFirebase(userEmail, userPassword);
                finish();
            }
        });
    }

    public void signUpFirebase(String userEmail, String userPassword){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignUpActivity.this, "Your account is successfully created",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SignUpActivity.this, "There is a problem",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}