package com.randwalk.game.newgame.level2.views;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.randwalk.game.R;

import java.util.Random;

public class TFView extends View {
    Point coordinates;
    Point prevCoordinates;
    int sig_Y=25;//this is stdev for normal distribution along Y axes
    int d=5;//this is step along x axis
    int random_X,random_Y;
    boolean bounced = false;
    public boolean finished = false;

    public TFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable background = this.getResources().getDrawable(R.drawable.cell1);
        this.setBackground(background);
        d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, d, getResources().getDisplayMetrics());
    }

    public TFView(Context context, Point coordinates){
        super(context);
        this.coordinates = coordinates;
        //prevCoordinates = coordinates;
        Drawable background = this.getResources().getDrawable(R.drawable.cell1);
        this.setBackground(background);
        d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, d, getResources().getDisplayMetrics());
    }

    public Point getCoordinates(){
        return coordinates;
    }
    public Point getPrevCoordinates(){
        return prevCoordinates;
    }

    public void setCoordinates(Point coordinates){
        this.coordinates = coordinates;
    }

    public void step(View leftSide, View rightSide) {
        if (!bounced){
            Random rand = new Random();
            int[] location= new int[2];
            int bound;
            prevCoordinates = new Point(coordinates.x, coordinates.y);
            Log.d("INSIDE TFView", "THIS IS THE PREV" + prevCoordinates);
            random_Y = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
            random_X = (int) Math.floor(rand.nextGaussian() * sig_Y); //was 30/60 but I think 20/40 looks better
            rightSide.getLocationOnScreen(location);
            if(coordinates.x + random_X >= location[0]){
                //bounce to the left
                bound = location[0];
                coordinates.x = bound;
                coordinates.y = coordinates.y + random_Y/2;
                bounced = true;
            }
            else {
                leftSide.getLocationOnScreen(location);
                if(coordinates.x +random_X <= location[0]+leftSide.getWidth()){
                    bound = location[0]+leftSide.getWidth();
                    coordinates.x = bound;
                    coordinates.y = coordinates.y +random_Y/2;
                    bounced = true;
                } else {
                    coordinates.x = coordinates.x + random_X;
                    coordinates.y = coordinates.y + random_Y;
                    Log.d("INSIDE TFView", "THIS IS AFTER GENERATION" + coordinates + " " + prevCoordinates);
                }
            }
        } else {
            coordinates.x = prevCoordinates.x;
            coordinates.y = coordinates.y + random_Y/2;
            bounced = false;
        }
    }

    public void step(View leftSide, View rightSide, Level2aPathView pathView) {
        if (!bounced){
            Random rand = new Random();
            int[] location= new int[2];
            int bound;
            prevCoordinates = new Point(coordinates.x, coordinates.y);
            Log.d("INSIDE TFView", "THIS IS THE PREV" + prevCoordinates);
            random_Y = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
            random_X = (int) Math.floor(rand.nextGaussian() * sig_Y); //was 30/60 but I think 20/40 looks better
            rightSide.getLocationOnScreen(location);
            if(coordinates.x + random_X >= location[0]){
                //bounce to the left
                bound = location[0];
                coordinates.x = bound;
                coordinates.y = coordinates.y + random_Y/2;
                bounced = true;
            }
            else {
                leftSide.getLocationOnScreen(location);
                if(coordinates.x +random_X <= location[0]+leftSide.getWidth()){
                    bound = location[0]+leftSide.getWidth();
                    coordinates.x = bound;
                    coordinates.y = coordinates.y +random_Y/2;
                    bounced = true;
                } else {
                    coordinates.x = coordinates.x + random_X;
                    coordinates.y = coordinates.y + random_Y;
                    Log.d("INSIDE TFView", "THIS IS AFTER GENERATION" + coordinates + " " + prevCoordinates);
                }
            }
        } else {
            coordinates.x = prevCoordinates.x;
            coordinates.y = coordinates.y + random_Y/2;
            bounced = false;
        }

            animate().x(coordinates.x).y(coordinates.y).setDuration(1);
            pathView.drawLine(getPrevCoordinates(), getCoordinates());
        if(!finished)
            step(leftSide,rightSide,pathView);

    }



}
