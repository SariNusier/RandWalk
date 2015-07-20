package com.randwalk.game.newgame.level1.b.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.views.Level1aPathView;


import java.util.Random;

public class Level1bGameActivity extends Activity {
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

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    boolean piratePlaced, isGuideOn;
    boolean drawing = false;
    boolean fadeIn;

    float drift_X = 0;
    float drift_Y = 0;

    int sig_Y=15;//this is stdev for normal distribution along Y axes
    int d=3;//this is step along x axis
    int random_X,random_Y;
    int walkCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1b_game);
        mainLayout = (RelativeLayout) findViewById(R.id.level1b_mainlayout);
        introLayout = (RelativeLayout) findViewById(R.id.level1b_intro_layout);
        startAreaView = findViewById(R.id.level1b_startarea_view);
        pirateView = findViewById(R.id.level1b_pirate_view);
        boatView = findViewById(R.id.level1b_boat_view);
        highLightView = findViewById(R.id.level1b_highlight_view);
        pathView = (Level1aPathView) findViewById(R.id.level1b_path_view);
        piratePlaced = false;
        placedPiratePos = new Point();
        currentPiratePos = new Point();
        prevPiratePos = new Point();
        scoreView = (TextView) findViewById(R.id.level1b_score_view);
        scorePopUp = (TextView) findViewById(R.id.level1b_score_popup);
        textViewIntro = (TextView) findViewById(R.id.level1b_intro_textview);
        introButton = (Button) findViewById(R.id.level1b_intro_button);
        guideText = (TextView) findViewById(R.id.level1b_guide_textview);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        textViewIntro.setTypeface(typeface);
        scoreView.setTypeface(typeface);
        scorePopUp.setTypeface(typeface);
        guideText.setTypeface(typeface);
        isGuideOn = true;
        placedPiratePos.x = (int)pirateView.getX();
        placedPiratePos.y = (int)pirateView.getY();
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x, y, z;
                x = event.values[0];
                drift_X = 0;//x*2;
                y = event.values[1];
                drift_Y = y;
                z = event.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        preferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        editor = preferences.edit();
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
                float boat_X = boatView.getX();
                float boat_Y = boatView.getY();
                float boat_W = boatView.getWidth();
                float boat_H = boatView.getHeight();
                float pirate_X = pirateView.getX()+pirateView.getWidth()/2;
                float pirate_Y = pirateView.getY()+pirateView.getHeight()/2;

                if(boat_X <= pirate_X && boat_Y<=pirate_Y && pirate_X <= boat_X+boat_W && pirate_Y <= boat_Y+boat_Y)
                {
                    onTheBoat();
                } else if(pirate_X >= mainLayout.getWidth() &&
                        pirate_Y <= mainLayout.getHeight() &&
                        pirate_Y >=0){
                    closeToBoat();
                }
                else if(pirate_Y >= mainLayout.getHeight() || pirate_Y <= 0){
                    missedTheBoat();
                } else {
                    Rect r = new Rect();
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
                if(!piratePlaced)
                    placePirate(event);
                else if(piratePlaced && walkCounter < 2 && !drawing)
                    positionPirate();
                return false;
            }
        });


    } //end of onCreate()

    public void placePirate(MotionEvent event) {
        if(isGuideOn) {
            guideText.setText("Help the pirate reach the boat by tilting the phone.");
            highLightPirate();
        }
        else {guideText.setVisibility(View.GONE);
            highLightView.setVisibility(View.GONE);}

        if(!piratePlaced) {
            currentPiratePos = new Point(placedPiratePos);
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
        Point start = new Point(prevPiratePos.x+pirateView.getWidth()/2,
                prevPiratePos.y+pirateView.getHeight()/2);
        Point end = new Point(currentPiratePos.x+pirateView.getWidth()/2,
                currentPiratePos.y+pirateView.getHeight()/2);
        pathView.drawLine(start,end);
    }

    public void pirateStep(){
        Random rand = new Random();
        random_X = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
        random_Y = (int)Math.floor(rand.nextGaussian()*sig_Y) ; //was 30/60 but I think 20/40 looks better
        currentPiratePos = new Point(currentPiratePos.x+random_X,currentPiratePos.y + random_Y + (int)drift_Y);
        if(isGuideOn){
            highLightPirate();
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
        popUpScore(currentPiratePos, amount);
    }

    public void walkFinished(){
        if(isGuideOn){
            if(walkCounter == 0)
                guideText.setText("Tap anywhere on the screen to make the pirate walk again from the same position.");
            else guideText.setText("The pirate must try 3 times from the same place before you can change his position.");
            highLightView.setBackground(getResources().getDrawable(R.drawable.level1_highlightshape));
            highLightView.setX(mainLayout.getX());
            highLightView.setY(mainLayout.getY());
            ViewGroup.LayoutParams params = highLightView.getLayoutParams();
            params.height = mainLayout.getHeight();
            params.width = mainLayout.getWidth();
        }
        drawing = false;
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
        if(isGuideOn){
            guideText.setText("The pirate must try 3 times from the same place before you can change his position.");
        }
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
    }

    public void highLightPirate(){
        highLightView.setBackground(getResources().getDrawable(R.drawable.level1_highlightround));
        highLightView.setX(pirateView.getX());
        highLightView.setY(pirateView.getY());
        ViewGroup.LayoutParams params = highLightView.getLayoutParams();
        params.height = pirateView.getHeight();
        params.width = pirateView.getWidth();
    }
}
