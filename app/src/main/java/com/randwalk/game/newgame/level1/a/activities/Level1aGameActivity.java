package com.randwalk.game.newgame.level1.a.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.randwalk.game.newgame.*;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.views.Level1aMainLayout;
import com.randwalk.game.newgame.level1.a.views.Level1aPathView;
import com.randwalk.game.newgame.level1.views.PirateView;

import java.util.Random;

public class Level1aGameActivity extends Activity {
    RelativeLayout mainLayout;
    View startAreaView;
    View pirateView;
    View boatView;
    Level1aPathView pathView;
    TextView scoreView;
    Point placedPiratePos;
    Point currentPiratePos;
    Point prevPiratePos;
    Animator.AnimatorListener animatorListener;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    boolean piratePlaced;
    boolean drawing = false;

    int sig_Y=15;//this is stdev for normal distribution along Y axes
    int d=3;//this is step along x axis

    int walkCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1a_game);
        mainLayout = (RelativeLayout) findViewById(R.id.level1a_mainlayout);
        startAreaView = findViewById(R.id.level1a_startarea_view);
        pirateView = findViewById(R.id.level1a_pirate_view);
        boatView = findViewById(R.id.level1a_boat_view);
        pathView = (Level1aPathView) findViewById(R.id.level1a_path_view);
        piratePlaced = false;
        placedPiratePos = new Point();
        currentPiratePos = new Point();
        prevPiratePos = new Point();
        scoreView = (TextView) findViewById(R.id.level1a_score_view);
        preferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        editor = preferences.edit();
        updateScore();
        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Rect boatRect = new Rect();
                Rect pirateRect = new Rect();
                pirateView.getGlobalVisibleRect(pirateRect);
                boatView.getGlobalVisibleRect(boatRect);
                if(boatRect.contains(Math.round(pirateView.getX())+25,Math.round(pirateView.getY())+25)){
                    //in the boat
                    onTheBoat();
                }
                else if(pirateView.getX()+pirateView.getWidth() >= mainLayout.getWidth() &&
                        pirateView.getY()+25 <= mainLayout.getHeight() &&
                        pirateView.getY()-25 >=0){
                    closeToBoat();
                }
                else if(pirateView.getY()+pirateView.getHeight() >= mainLayout.getHeight() || pirateView.getY()-25 <= 0){
                    missedTheBoat();
                }

                else {
                    Rect r = new Rect();
                    Log.d("PIRATE VIEW POSISTION","POSITION: "+pirateRect.centerX()+" "+boatView.getWidth()+" "+boatView.getGlobalVisibleRect(r)+" "+r);
                    prevPiratePos = currentPiratePos;
                    pirateStep();
                    pirateView.animate().x(currentPiratePos.x).y(currentPiratePos.y).setDuration(1).setListener(animatorListener);
                    drawPath();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getX()<startAreaView.getWidth() && !piratePlaced)
                    placePirate(event);
                else if(piratePlaced && walkCounter < 2 && !drawing)
                    positionPirate();
                return false;
            }
        });



    } //end of onCreate()

    public void placePirate(MotionEvent event) {
        if(!piratePlaced) {
            placedPiratePos.y = Math.round(event.getY());
            currentPiratePos = new Point(placedPiratePos);
            pirateView.setX(placedPiratePos.x);
            pirateView.setY(placedPiratePos.y);
            pirateView.setVisibility(View.VISIBLE);
            mainLayout.invalidate();
            pirateView.invalidate();
            piratePlaced = true;
            drawing = true;
            drawWalk();
        }
    }

    public void drawWalk(){
        prevPiratePos = currentPiratePos;
        pirateStep();
        pirateView.animate().x(currentPiratePos.x).y(currentPiratePos.y).setDuration(100).setListener(animatorListener);
        drawPath();
    }

    public void drawPath(){
        Point start = new Point(prevPiratePos.x+pirateView.getWidth(),prevPiratePos.y);
        Point end = new Point(currentPiratePos.x+pirateView.getWidth(),currentPiratePos.y);
        pathView.drawLine(start,end);
    }

    public void pirateStep(){
        int random_X,random_Y;
        Random rand = new Random();
        random_X = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
        random_Y = (int)Math.floor(rand.nextGaussian()*sig_Y); //was 30/60 but I think 20/40 looks better
        currentPiratePos = new Point(currentPiratePos.x+random_X,currentPiratePos.y + random_Y);
    }

    public void onTheBoat(){
        increaseScore(100);
        walkFinished();
    }

    public void closeToBoat(){
        increaseScore(50);
        walkFinished();
    }

    public void missedTheBoat(){
        increaseScore(0);
        walkFinished();
    }

    public void updateScore(){
        scoreView.setText("Score: " + preferences.getInt("score_1A", 0));
        final int oldColor = scoreView.getCurrentTextColor();
        new Thread(){
            @Override
            public void run() {
                super.run();

                try {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            scoreView.setTextColor(Color.GREEN);
                        }
                    });
                    sleep(500);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            scoreView.setTextColor(oldColor);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public void increaseScore(int amount){
        editor.putInt("score_1A",amount + preferences.getInt("score_1A",0));
        editor.commit();
        updateScore();
    }

    public void walkFinished(){
        drawing = false;
        if(walkCounter >= 2)
            repostionPirate();
    }

    /**
     * Allows the player to reposition pirate in the starting area.
     */
    public void repostionPirate(){
        walkCounter = 0;
        piratePlaced = false;
    }

    /**
     * Restarts pirate from the same position as the last try.
     */
    public void positionPirate(){
        drawing = true;
        walkCounter++;
        currentPiratePos = new Point(placedPiratePos);
        pirateView.setX(placedPiratePos.x);
        pirateView.setY(placedPiratePos.y);
        pirateView.setVisibility(View.VISIBLE);
        mainLayout.invalidate();
        pirateView.invalidate();
        piratePlaced = true;
        drawWalk();
    }
}
