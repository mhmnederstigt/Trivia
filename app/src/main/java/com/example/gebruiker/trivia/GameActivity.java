package com.example.gebruiker.trivia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setLogLevel(Logger.Level.DEBUG);
//
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello, World!");
//
//        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        // retrieve question from API by creating a new request
        TriviaHelper request = new TriviaHelper(this);
        request.getNextQuestion(this);
    }


    @Override
    public void gotQuestion(Question question) {

        // when question is received, display it
        TextView questionDisplay = findViewById(R.id.question);
        questionDisplay.setText(question.getQuestion());

    }

    @Override
    public void gotError(String message) {

        // when question is not loaded successfully, print error
        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
    }
}
