package com.example.gebruiker.trivia;

import java.io.Serializable;

public class Highscore implements Serializable {
    private String name;
    private int score;

    // empty constructor required for firebase
    public Highscore(){}

//    // regular constructor
//    public Highscore(String name, int score) {
//       this.name = name;
//       this.score = score;
//    }

    // setters and getters
    public void setName(String name) {
        this.name = name;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
}
