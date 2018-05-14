package com.example.gebruiker.trivia;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements TriviaHelper.Callback {
    TriviaHelper request;
    TriviaHelper.Callback callback = this;
    int questionCount;
    int scoreCount;
    int scoreOfcurrentQuestion;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        user = "Milou";

        questionCount = 0;
        scoreCount = 0;

        // retrieve question from API by creating a new request
        request = new TriviaHelper(this);
        request.getNextQuestion(this);

        // set listener to next button
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new nextListener());
    }

    /// when button is pressed
    public class nextListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // check if answer is correct, update score
            scoreCount += scoreOfcurrentQuestion;

            // display next question if maximum isn't reached yet
            if (questionCount < 10) {
                request.getNextQuestion(callback);
                questionCount += 1;
            }
            else {
                finish();
            }
        }
    }

    @Override
    public void gotQuestion(Question question) {

        // when question is received, display it
        TextView questionDisplay = findViewById(R.id.question);
        questionDisplay.setText(question.getQuestion());

        // display options for answers
        RadioButton option1 = findViewById(R.id.option1);
        option1.setText(question.getCorrectAnswer());
        RadioButton option2 = findViewById(R.id.option2);
        option2.setText(question.getCorrectAnswer());
        RadioButton option3 = findViewById(R.id.option3);
        option3.setText(question.getCorrectAnswer());
        RadioButton option4 = findViewById(R.id.option4);
        option4.setText(question.getCorrectAnswer());

        scoreOfcurrentQuestion = question.getValue();
        Log.d("hi", String.valueOf(scoreOfcurrentQuestion));
    }

    @Override
    public void gotError(String message) {

        // when question is not loaded successfully, print error
        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    // pass on score when game is over, or user presses back button
    public void finish() {
        int score = scoreCount;
        Highscore newHighscore = new Highscore(user, score);

        // prepare
        Intent intent = new Intent();
        intent.putExtra("newHighscore", newHighscore);

        // activity finished ok, return score
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
