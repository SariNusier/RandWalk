package com.randwalk.game.newgame.level1.c.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.randwalk.game.Other.EndpointsAsyncTask;
import com.randwalk.game.Other.Try;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.views.Level1aPathView;
import com.randwalk.game.newgame.level1.b.activities.Level1bGameActivity;


import java.util.Random;

public class Level1cGameActivity extends Activity {
    RelativeLayout mainLayout;
    RelativeLayout introLayout;
    View startAreaView;
    View pirateView;
    View boatView;
    Level1aPathView pathView;
    TextView scoreView;
    TextView scorePopUp;
    TextView textViewIntro;
    TextView guideText;
    Button introButton;
    Point placedPiratePos;
    Point currentPiratePos;
    Point prevPiratePos;
    Animator.AnimatorListener animatorListener, scorePopUpAnimListener, highLightAnimListener;
    SensorEventListener sensorEventListener;
    View highLightView;
    float drift_Y;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    boolean piratePlaced, isGuideOn, prefGuide;
    boolean drawing = false;
    boolean fadeIn, stepping = false;

    int sig_Y=15;//this is stdev for normal distribution along Y axes
    int d=3;//this is step along x axis
    long step1 = 0, time;

    float startingPoint, finalPointX, finalPointY, length = 0;
    int random_X,random_Y;
    int walkCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1c_game);
        mainLayout = (RelativeLayout) findViewById(R.id.level1c_mainlayout);
        introLayout = (RelativeLayout) findViewById(R.id.level1c_intro_layout);
        startAreaView = findViewById(R.id.level1c_startarea_view);
        pirateView = findViewById(R.id.level1c_pirate_view);
        boatView = findViewById(R.id.level1c_boat_view);
        highLightView = findViewById(R.id.level1c_highlight_view);
        pathView = (Level1aPathView) findViewById(R.id.level1c_path_view);
        piratePlaced = false;
        placedPiratePos = new Point();
        currentPiratePos = new Point();
        prevPiratePos = new Point();
        scoreView = (TextView) findViewById(R.id.level1c_score_view);
        scorePopUp = (TextView) findViewById(R.id.level1c_score_popup);
        textViewIntro = (TextView) findViewById(R.id.level1c_intro_textview);
        introButton = (Button) findViewById(R.id.level1c_intro_button);
        guideText = (TextView) findViewById(R.id.level1c_guide_textview);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        textViewIntro.setTypeface(typeface);
        scoreView.setTypeface(typeface);
        scorePopUp.setTypeface(typeface);
        guideText.setTypeface(typeface);
        isGuideOn = true;
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float  y;
                y = event.values[1];
                drift_Y = y;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //preferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isGuideOn = preferences.getBoolean("guide",true);
        updateScore();
        scorePopUpAnimListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                scorePopUp.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];

                boatView.getLocationOnScreen(location);
                float boat_X = location[0];
                float boat_Y = location[1];
                float boat_W = boatView.getWidth();
                float boat_H = boatView.getHeight();
                float pirate_X = pirateView.getX()+pirateView.getWidth()/2;
                float pirate_Y = pirateView.getY()+pirateView.getHeight()/2;

                if(boat_X <= pirate_X && boat_Y<=pirate_Y && pirate_X <= boat_X+boat_W && pirate_Y <= boat_Y+boat_H)
                {
                    onTheBoat();
                } else if(pirate_X >= mainLayout.getWidth() &&
                        pirate_Y <= mainLayout.getHeight() &&
                        pirate_Y >=0){
                    closeToBoat();
                }
                else if(pirate_Y >= mainLayout.getHeight() || pirate_Y <= 0){
                    missedTheBoat();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        highLightAnimListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(!fadeIn){
                    highLightView.animate().alpha(0.5f).setDuration(1000).setListener(highLightAnimListener);
                    fadeIn = true;
                }
                else {
                    highLightView.animate().alpha(0).setDuration(1000).setListener(highLightAnimListener);
                    fadeIn = false;
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
                else if(drawing){
                    time = System.currentTimeMillis();
                    if(step1 != 0){
                        pirateStep(time - step1);
                        step1 = 0;
                    } else{
                        step1 = time;
                    }
                }
                return false;
            }
        });


    } //end of onCreate()

    public void placePirate(MotionEvent event) {
        if(isGuideOn) {
            guideText.setText("Tap on the screen as fast as you can to make the pirate walk!");
            highLightView.setVisibility(View.GONE);
        }
        else {guideText.setVisibility(View.GONE);
            highLightView.setVisibility(View.GONE);}

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
            startingPoint = event.getY();
        }
    }

    public void drawPath(){
        Point start = new Point(prevPiratePos.x+pirateView.getWidth()/2,
                prevPiratePos.y+pirateView.getHeight()/2);
        Point end = new Point(currentPiratePos.x+pirateView.getWidth()/2,
                currentPiratePos.y+pirateView.getHeight()/2);
        pathView.drawLine(start,end);
    }

    public void pirateStep(long distance){
        Random rand = new Random();
        if(distance > 200){
            step1 = 0;
        } else {
            prevPiratePos = currentPiratePos;
            random_X = (int) distance/5;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
            random_Y = (int)Math.floor(rand.nextGaussian()*sig_Y); //was 30/60 but I think 20/40 looks better
            currentPiratePos = new Point(currentPiratePos.x+random_X,currentPiratePos.y + random_Y + (int) drift_Y);
            pirateView.animate().x(currentPiratePos.x).y(currentPiratePos.y).setDuration(100).setListener(animatorListener);
            drawPath();
        }
    }

    public void onTheBoat(){
        increaseScore(200);
        walkFinished();
    }

    public void closeToBoat(){
        double h = mainLayout.getHeight();
        double distance;
        if(currentPiratePos.y < h/2)
            distance = boatView.getTop()-currentPiratePos.y;
        else
            distance = currentPiratePos.y - boatView.getBottom();

        increaseScore((int)(100*(1-(2*distance/h))));
        walkFinished();
    }

    public void missedTheBoat(){
        increaseScore(0);
        walkFinished();
    }

    public void updateScore(){
        scoreView.setText("Score: " + preferences.getInt("score_1C", 0));
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
        editor = preferences.edit();
        editor.putInt("score_1C",amount + preferences.getInt("score_1C",0));
        editor.apply();
        updateScore();
        popUpScore(currentPiratePos, amount);
    }

    public void walkFinished(){
        drawing = false;
        finalPointX = pirateView.getX();
        finalPointY = pathView.getY();
        Try save = new Try(this, "0", preferences.getInt("score_1C", 0), startingPoint, finalPointX, finalPointY, length, "C");
        new EndpointsAsyncTask().execute(new Pair<Context, Try>(this, save));
        if(walkCounter >= 2)
            repositionPirate();
        pathView.changeColor();


    }

    /**
     * Allows the player to reposition pirate in the starting area.
     */
    public void repositionPirate(){
        if(isGuideOn){
            guideText.setText("Now try to put the pirate in a different place and see if he reaches the boat.");
            highLightView.setVisibility(View.VISIBLE);
            highLightView.setX(startAreaView.getX());
            highLightView.setY(startAreaView.getY());
            ViewGroup.LayoutParams params = highLightView.getLayoutParams();
            params.height = startAreaView.getHeight();
            params.width = startAreaView.getWidth();
            isGuideOn = false;
        }
        walkCounter = 0;
        piratePlaced = false;
        pathView.restart();
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
    }

    public void popUpScore(Point coordinate, int amount){

        scorePopUp.setX( mainLayout.getWidth() - scorePopUp.getWidth() * 1.5f );
        scorePopUp.setY(coordinate.y);
        scorePopUp.setText("+"+amount);
        scorePopUp.setVisibility(View.VISIBLE);
        scorePopUp.animate().y(coordinate.y - 150).setDuration(500).setListener(scorePopUpAnimListener);
    }

    public void nextIntro(View v){
        textViewIntro.setVisibility(View.GONE);
        introLayout.setBackground(null);

        endIntro(v);
        guideText.setText("Tap on the shaded area to place the pirate.");
        // guideText.setX(startAreaView.getX() + startAreaView.getWidth() + 5);
        //  guideText.setY(mainLayout.getHeight() / 4);
        highLightView.setX(startAreaView.getX());
        highLightView.setY(startAreaView.getY());
        ViewGroup.LayoutParams params = highLightView.getLayoutParams();
        params.height = startAreaView.getHeight();
        params.width = startAreaView.getWidth();
        highLightView.setVisibility(View.VISIBLE);
        highLightView.setAlpha(0.5f);
        fadeIn = false;
        highLightView.animate().alpha(0).setDuration(1000).setListener(highLightAnimListener);


    }

    public void endIntro(View v){
        introLayout.setVisibility(View.GONE);
        pirateView.setVisibility(View.INVISIBLE);
    }

    public void goToLevel1B(){
        editor.putBoolean("level1BUnlocked",true);
        startActivity(new Intent(this, Level1bGameActivity.class));
        finish();
    }

}
