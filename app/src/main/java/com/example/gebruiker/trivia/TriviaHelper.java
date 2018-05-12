package com.example.gebruiker.trivia;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.api.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import javax.security.auth.callback.Callback;

public class TriviaHelper implements com.android.volley.Response.Listener<JSONArray>, com.android.volley.Response.ErrorListener {

    public Context context;
    public Callback activity;

    public interface Callback {
        void gotQuestion(Question question);
        void gotError(String message);
    }

    // constructor
    public TriviaHelper(Context context) {
        this.context = context;
    }



    @Override
    public void onResponse(JSONArray response) {

        // convert incoming json to question type
        try {
            String question = response.getJSONObject(0).getString("question");
            String correctAnswer = response.getJSONObject(0).getString("answer");
            Question retrievedQuestion = new Question(question, correctAnswer);

            activity.gotQuestion(retrievedQuestion);
        }
        catch(JSONException jse){
            jse.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        // pass error on to activity if request is unsuccessful
        String message = error.getMessage();
        activity.gotError(message);
    }

    public void getNextQuestion(Callback activity) {

        // add new JSON request to the queue, notify activity if successful
        this.activity = activity;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://jservice.io/api/random", this, this);
        queue.add(jsonArrayRequest);
    }



}
