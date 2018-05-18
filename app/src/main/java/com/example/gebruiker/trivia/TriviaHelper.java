package com.example.gebruiker.trivia;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


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

            // reformat both question and answer
            String correctedQuestion = Html.fromHtml(question).toString().replaceAll("\n", "").trim();
            correctedQuestion.replace("\\\\", "");
            String correctAnswer = response.getJSONObject(0).getString("answer");
            String correctedAnswer = Html.fromHtml(correctAnswer).toString().replaceAll("\n", "").trim();
            correctedAnswer = correctedAnswer.replace("\\\\", "");
            correctedAnswer = correctedAnswer.replace("-", " ");


            // check if value is not null, if so assign default of 500
            int value;
            try {
                value = response.getJSONObject(0).getInt("value");
            }
            catch(Exception e){
                value = 500;
            }

            // construct Question object
            Question retrievedQuestion = new Question(correctedQuestion, correctedAnswer, value);

            // notify callback
            activity.gotQuestion(retrievedQuestion);
        } catch (JSONException jse) {
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
