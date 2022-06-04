package com.example.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizActivity extends AppCompatActivity {

    TextView textViewTime, textViewCorrect, textViewWrong, textViewQuestion,
             textViewA, textViewB, textViewC, textViewD;

    Button buttonNext, buttonFinish;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");
    DatabaseReference databaseReference2 = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    String question, answerA, answerB, answerC, answerD, correctAnswer, userAnswer;
    int questionCount;
    int questionNumber = 1;
    int correctAnswerCount = 0;
    int wrongAnswerCount = 0;
    boolean clicked = false;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 25000;
    long timeLeft = TOTAL_TIME;
    boolean timerContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewTime = findViewById(R.id.textViewTime);
        textViewCorrect = findViewById(R.id.textViewCorrect);
        textViewWrong = findViewById(R.id.textViewWrong);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewA = findViewById(R.id.textViewA);
        textViewB = findViewById(R.id.textViewB);
        textViewC = findViewById(R.id.textViewC);
        textViewD = findViewById(R.id.textViewD);

        buttonNext = findViewById(R.id.buttonNext);
        buttonFinish = findViewById(R.id.buttonFinish);

        game();


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                game();

            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendScore();
                Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textViewA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    userAnswer = "a";
                    if (userAnswer.equals(correctAnswer)) {
                        textViewA.setBackgroundColor(Color.GREEN);
                        correctAnswerCount++;
                        textViewCorrect.setText(""+correctAnswerCount);
                    } else {
                        textViewA.setBackgroundColor(Color.RED);
                        wrongAnswerCount++;
                        textViewWrong.setText(""+wrongAnswerCount);
                        findAnswer();
                    }
                    textViewA.setClickable(false);
                    clicked = true;
                }
                pauseTimer();
            }
        });

        textViewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    userAnswer = "b";
                    if (userAnswer.equals(correctAnswer)) {
                        textViewB.setBackgroundColor(Color.GREEN);
                        correctAnswerCount++;
                        textViewCorrect.setText(""+correctAnswerCount);
                    } else {
                        textViewB.setBackgroundColor(Color.RED);
                        wrongAnswerCount++;
                        textViewWrong.setText(""+wrongAnswerCount);
                        findAnswer();
                    }
                    textViewB.setClickable(true);
                    clicked = true;
                }
                pauseTimer();
            }
        });

        textViewC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    userAnswer = "c";
                    if (userAnswer.equals(correctAnswer)) {
                        textViewC.setBackgroundColor(Color.GREEN);
                        correctAnswerCount++;
                        textViewCorrect.setText(""+correctAnswerCount);
                    } else {
                        textViewC.setBackgroundColor(Color.RED);
                        wrongAnswerCount++;
                        textViewWrong.setText(""+wrongAnswerCount);
                        findAnswer();
                    }
                    textViewC.setClickable(true);
                    clicked = true;
                }
                pauseTimer();
            }
        });

        textViewD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    userAnswer = "d";
                    if (userAnswer.equals(correctAnswer)) {
                        textViewD.setBackgroundColor(Color.GREEN);
                        correctAnswerCount++;
                        textViewCorrect.setText(""+correctAnswerCount);
                    } else {
                        textViewD.setBackgroundColor(Color.RED);
                        wrongAnswerCount++;
                        textViewWrong.setText(""+wrongAnswerCount);
                        findAnswer();
                    }
                    textViewD.setClickable(true);
                    clicked = true;
                }
                pauseTimer();
            }

        });

    }



    public void game(){
        startTimer();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                questionCount = (int) snapshot.getChildrenCount();
                DataSnapshot questionNumberSnapShot = snapshot.child(String.valueOf(questionNumber));

                question = questionNumberSnapShot.child("q").getValue().toString();
                textViewQuestion.setText(question);

                answerA = questionNumberSnapShot.child("a").getValue().toString();
                textViewA.setText(answerA);

                answerB = questionNumberSnapShot.child("b").getValue().toString();
                textViewB.setText(answerB);

                answerC = questionNumberSnapShot.child("c").getValue().toString();
                textViewC.setText(answerC);

                textViewA.setBackgroundColor(Color.WHITE);
                textViewB.setBackgroundColor(Color.WHITE);
                textViewC.setBackgroundColor(Color.WHITE);
                textViewD.setBackgroundColor(Color.WHITE);

                textViewA.setClickable(true);
                textViewB.setClickable(true);
                textViewC.setClickable(true);
                textViewD.setClickable(true);

                clicked = false;

                answerD = questionNumberSnapShot.child("d").getValue().toString();
                textViewD.setText(answerD);

                correctAnswer = questionNumberSnapShot.child("Answer").getValue().toString();



                if (questionNumber < questionCount){
                    questionNumber++;
                }else{
                    Toast.makeText(QuizActivity.this, "You answered all questions", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void findAnswer(){
        if (correctAnswer.equals("a")){
            textViewA.setBackgroundColor(Color.GREEN);
        }
        if (correctAnswer.equals("b")){
            textViewB.setBackgroundColor(Color.GREEN);
        }
        if (correctAnswer.equals("c")){
            textViewC.setBackgroundColor(Color.GREEN);
        }
        if (correctAnswer.equals("d")){
            textViewD.setBackgroundColor(Color.GREEN);
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                //updateCountDownText():
                int seconds = (int)(timeLeft/1000)%60;
                textViewTime.setText(""+ seconds);
            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                textViewQuestion.setText("Sorry, Time is Up!");
            }
        }.start();

        timerContinue = true;
    }

    public void pauseTimer(){
        countDownTimer.cancel();
        timerContinue = false;
    }

    public void resetTimer(){
        timeLeft = TOTAL_TIME;
    }

    public void sendScore(){
        String userId = user.getUid();
        databaseReference2.child("Scores").child(userId).child("CorrectAnswers").setValue(""+correctAnswerCount);
        databaseReference2.child("Scores").child(userId).child("WrongAnswers").setValue(""+wrongAnswerCount);
    }
}