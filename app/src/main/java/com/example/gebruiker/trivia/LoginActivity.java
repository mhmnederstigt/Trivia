package com.example.gebruiker.trivia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new ClickLoginListener());
    }

    public class ClickLoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            // retrieve info from form
            EditText emailField = findViewById(R.id.email);
            EditText passwordField = findViewById(R.id.password);
            email = emailField.getText().toString();
            password = passwordField.getText().toString();

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

            if (mat.matches() && password.length()>5) {
                // // send email and password back to HighscoreActivity
                tryLogin();
            }
        }
    }

    public void tryLogin(){

        // prepare login info in intent
        Intent intent = new Intent();
        intent.putExtra("email", email);
        intent.putExtra("password", password);

        // return login info to HighscoreActivity
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
