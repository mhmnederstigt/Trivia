package com.example.gebruiker.trivia;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Highscore implements Comparable, Serializable {
    private String name;
    private int score;

    // empty constructor required for firebase
    public Highscore(){}

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


    @Override
    public int compareTo(@NonNull Object obj) {
        int compareScore =((Highscore) obj).getScore();
        return compareScore-this.score;
    }

}
