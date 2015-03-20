package com.randwalk.game.GameViews;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.randwalk.game.R;

import java.util.Random;

/**
 * Created by sari on 17/03/15.
 */
public class TFView extends View {
    Point coordinates;
    Point prevCoordinates;
    public TFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable background = this.getResources().getDrawable(R.drawable.cell1);
        this.setBackground(background);
    }

    public TFView(Context context, Point coordinates){
        super(context);
        this.coordinates = coordinates;
        //prevCoordinates = coordinates;
        Drawable background = this.getResources().getDrawable(R.drawable.cell1);
        this.setBackground(background);
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

    public void takeOneStep(){
        Random rand = new Random();
        prevCoordinates = new Point(coordinates.x,coordinates.y);
        Log.d("INSIDE TFView","THIS IS THE PREV"+prevCoordinates);
        coordinates.y = coordinates.y + rand.nextInt(20);
        coordinates.x = coordinates.x + rand.nextInt(61)-30;
        Log.d("INSIDE TFView","THIS IS AFTER GENERATION"+coordinates+" "+prevCoordinates);
    }


}
