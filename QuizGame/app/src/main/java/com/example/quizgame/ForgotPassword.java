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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText editTextForgotEmail;
    Button buttonResetPassword;
    ProgressBar resetProgressbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextForgotEmail = findViewById(R.id.editTextForgotEmail);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        resetProgressbar = findViewById(R.id.resetProgressbar);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetProgressbar.setVisibility(View.VISIBLE);
                String userResetEmail = editTextForgotEmail.getText().toString();
                mAuth.sendPasswordResetEmail(userResetEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    resetProgressbar.setVisibility(View.INVISIBLE);
                                    buttonResetPassword.setClickable(false);
                                    Toast.makeText(ForgotPassword.this, "A Reset Link is Successfully Sent to Your Email", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(ForgotPassword.this, "Reset Link Failed to Be sent", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}