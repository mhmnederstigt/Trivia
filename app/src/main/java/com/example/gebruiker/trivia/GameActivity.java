package com.example.gebruiker.trivia;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String username;
    Question curQuestion;
    EditText answerField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // create handles for UI display
        answerField = findViewById(R.id.answer);
        TextView greetingDisplay = findViewById(R.id.greeting);

        // set listener to next button
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new submitAnswerListener());

        // show encouraging message if user is logged in
        Intent intent = getIntent();
        username = (String) intent.getSerializableExtra("username");

        if (!username.equals("anonymous")) {
            greetingDisplay.setText("You can do it, " + username + "!");
        }

        // initialize game starting values
        questionCount = 1;
        scoreCount = 0;

        // retrieve question from API by creating a new request
        request = new TriviaHelper(this);
        request.getNextQuestion(this);

    }

    /// when answer is submitted
    public class submitAnswerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // check if answer is correct
            String givenAnswer = answerField.getText().toString().toLowerCase();
            givenAnswer.replace("-", " ");

            if (givenAnswer.equals(curQuestion.getCorrectAnswer().toLowerCase())) {
                // assign points to score
                scoreCount += curQuestion.getValue();

                // inform user
                Toast toast= Toast.makeText(getApplicationContext(),
                        "That's correct!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.show();
            }
            else {
                Toast toast= Toast.makeText(getApplicationContext(),
                        "Too bad, that's not right", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.show();
            }

            // display next question if maximum is not reached yet
            if (questionCount < 10) {
                request.getNextQuestion(callback);
                questionCount += 1;
                TextView count = findViewById(R.id.counter);
                count.setText("Question: " + questionCount);
            }
            else {
                finish();
            }
        }
    }

    @Override
    public void gotQuestion(Question question) {

        curQuestion = question;

        // when question is received, update UI
        TextView questionDisplay = findViewById(R.id.question);
        questionDisplay.setText(question.getQuestion());
        answerField.setText("");

        // for testing purposes log correct answer
        Log.d("Correct answer:" ,curQuestion.getCorrectAnswer());
    }

    @Override
    public void gotError(String message) {

        // when question is not loaded successfully, print error
        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

        // try again
        // retrieve question from API by creating a new request
        request = new TriviaHelper(this);
        request.getNextQuestion(this);
    }

    @Override
    // pass on score when game is over, or user presses back button
    public void finish() {

        // create new highscore
        Highscore newHighscore = new Highscore();
        newHighscore.setName(username);
        newHighscore.setScore(scoreCount);

        // put highscore in intent
        Intent intent = new Intent();
        intent.putExtra("newHighscore", newHighscore);

        // return
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
