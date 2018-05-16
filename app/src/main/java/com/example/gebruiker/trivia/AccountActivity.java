package com.example.gebruiker.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountActivity extends AppCompatActivity {
    String email;
    String password;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // set listener to submit button
        Button create = findViewById(R.id.create);
        create.setOnClickListener(new CreateAccountListener());
    }

    public class CreateAccountListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // retrieve info from form
            EditText emailField = findViewById(R.id.email);
            EditText passwordField = findViewById(R.id.password);
            EditText nameField = findViewById(R.id.name);
            email = emailField.getText().toString();
            password = passwordField.getText().toString();
            name = nameField.getText().toString();

            // verify email - https://stackoverflow.com/questions/18463848/how-to-tell-if-a-random-string-is-an-email-address-or-something-else
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher mat = pattern.matcher(email);

            if (!mat.matches()) {
                emailField.setHint("enter a valid email adress");
                emailField.setText("");
            }

            // verify password (and email)
            if (password.length()<6) {
                passwordField.setHint("hint: 6 characters");
                passwordField.setText("");
            }

            if (name == null) {
                nameField.setHint("provide a username");
                nameField.setText("");
            }

            if (mat.matches() && password.length()>5 && name != null) {
                // send account info back to HighscoreActivity
                tryCreate();
            }
        }
    }

    public void tryCreate(){

        // prepare login info in intent
        Intent intent = new Intent();
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("name", name);

        // return login info to HighscoreActivity
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
