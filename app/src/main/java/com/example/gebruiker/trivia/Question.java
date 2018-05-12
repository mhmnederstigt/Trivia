package com.example.gebruiker.trivia;

public class Question {

    private String question;
    private String answers[];
    private String correctAnswer;

    // constructor
    public Question(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }


    public String getQuestion(){
        return this.question;
    }

    public void setQuestion() {

    }
}



