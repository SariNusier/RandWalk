package com.example.sari.randomwalk;

import android.content.Context;

/**
 * Created by sari on 30/01/15.
 */
public class Try {

    private Context context;
    private String id;
    private int score;
    private float startingPoint, finalPointY, finalPointX, length;


    public Try(Context context,String id, int score, float startingPoint,float finalPointX, float finalPointY, float length){

        this.context = context;
        this.id = id;
        this.score = score;
        this.startingPoint = startingPoint;
        this.finalPointX = finalPointX;
        this.finalPointY = finalPointY;
        this.length = length;

    }

    public int getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public float getStartingPoint() {
        return startingPoint;
    }

    public float getFinalPointY() {
        return finalPointY;
    }

    public float getFinalPointX() {
        return finalPointX;
    }

    public float getLength() {
        return length;
    }

    public Context getContext() {
        return context;
    }
}