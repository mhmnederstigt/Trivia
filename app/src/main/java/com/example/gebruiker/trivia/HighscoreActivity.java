package com.example.gebruiker.trivia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HighscoreActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String username;

    ArrayList<Highscore> highscoreList = new ArrayList<>();
    HighscoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // set up database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setLogLevel(Logger.Level.DEBUG);

        // declare Firebase authorization and database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = database.getReference("trivia");

        // if highscores change, read highscores from the database and update UI
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                highscoreList = new ArrayList<>();
                for (DataSnapshot highs : dataSnapshot.child("highscores").getChildren()) {
                    Highscore h = highs.getValue(Highscore.class);
                    highscoreList.add(h);
                }

                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // failed to read value
                Log.w("Error: ", "Failed to read value.", error.toException());
            }
        });

     }

    @Override
    public void onStart() {
        super.onStart();
        // check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // where to go if menu is clicked
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.play:
            Intent intentGame = new Intent(HighscoreActivity.this, GameActivity.class);
            intentGame.putExtra("username", username);
            startActivityForResult(intentGame, 1);
            return(true);
        case R.id.login:
            Intent intentLogin = new Intent(HighscoreActivity.this, LoginActivity.class);
            startActivityForResult(intentLogin, 2);
            return(true);
        case R.id.create:
            Intent intentCreate = new Intent(HighscoreActivity.this, AccountActivity.class);
            startActivityForResult(intentCreate, 3);
            return(true);
        case R.id.logout:
            logout();
            return true;
        case R.id.exit:
            mAuth.signOut();
            finish();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    // after return to activity, update UI and Firebase
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // after a game has been played
        if (resultCode == RESULT_OK && requestCode == 1) {

            // add new highscore
            if (data.hasExtra("newHighscore")) {
                Highscore newHighscore = (Highscore) data.getExtras().getSerializable("newHighscore");
                Toast toast= Toast.makeText(getApplicationContext(),
                        "Points: " + newHighscore.getScore(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.show();

                // only store highscore if is not null
                if (newHighscore.getScore()>0){
                    mDatabase.child("highscores").push().setValue(newHighscore);
                }

                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            }
        }

        // after pressing login button
        else if (resultCode == RESULT_OK && requestCode == 2) {
            if (data.hasExtra("email")) {

                // login with provided user name and password
                String email = (String) data.getExtras().getSerializable("email");
                String password = (String) data.getExtras().getSerializable("password");
                login(email, password);
            }
        }

        // after pressing create acount button
        else if (resultCode == RESULT_OK && requestCode == 3) {
            if (data.hasExtra("email")) {

                String email = (String) data.getExtras().getSerializable("email");
                String password = (String) data.getExtras().getSerializable("password");
                String username = (String) data.getExtras().getSerializable("name");

                // create user account
                createUser(email, password, username);

                // login with that account
                login(email, password);
            }
        }
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // on success, update UI with the signed-in user's name
                            Toast.makeText(HighscoreActivity.this, "You're logged in now!",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {

                            // if sign in fails, display a message to the user
                            Toast.makeText(HighscoreActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void logout(){
        mAuth.getInstance().signOut();
        updateUI(null);

    }

    public void createUser(final String email, final String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // if signed in successfully, update UI with the signed-in user's information
                            Toast.makeText(HighscoreActivity.this, "Yay! You created an account.",
                                    Toast.LENGTH_SHORT).show();

                            // add name to existing database
                            FirebaseUser user = mAuth.getCurrentUser();
                            mDatabase.child("users").child(user.getUid()).child("username").setValue(name.toString());

                            // and make username accessible for other functions
                            username = name;

                        } else {

                            // if sign in fails, display a message to the user
                            Toast.makeText(HighscoreActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            // user is signed in
            // display username in UI
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    username = dataSnapshot.child("users").child(user.getUid()).child("username").getValue(String.class);
                    String helloMessage = "Hi " + username + "!";
                    TextView greeting = findViewById(R.id.greeting);
                    greeting.setText(helloMessage);
                }


                @Override
                public void onCancelled(DatabaseError error) {
                    // failed to read value
                    Log.w("Error", "Failed to read value.", error.toException());
                }
            });

        } else {
            // no user is signed in
            TextView greeting = findViewById(R.id.greeting);
            greeting.setText(R.string.anonymous_greeting);
            username = getString(R.string.anonymous);
        }

        // sort highscores
        Collections.sort(highscoreList);

        // set adapter
        ListView lv = findViewById(R.id.list);
        adapter = new HighscoreAdapter(this, R.layout.item_highscore, highscoreList);
        lv.setAdapter(adapter);
    }
}
