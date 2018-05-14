package com.example.gebruiker.trivia;

public class Question {

    private String question;
    private String answers[];
    private String correctAnswer;
    private int value;

    // constructor
    public Question(String question, String correctAnswer, int value) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.value = value;
    }


    public String getQuestion(){
        return this.question;
    }

    public void setQuestion() {

    }

    public String getCorrectAnswer(){
        return this.correctAnswer;
    }

    public int getValue() {return this.value;}

}



