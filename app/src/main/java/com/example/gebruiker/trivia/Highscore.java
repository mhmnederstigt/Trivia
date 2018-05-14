package com.example.gebruiker.trivia;

import java.io.Serializable;

public class Highscore implements Serializable{
    String name;
    int score;

    public Highscore(){}

    public Highscore(String name, int score) {
       this.name = name;
       this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
