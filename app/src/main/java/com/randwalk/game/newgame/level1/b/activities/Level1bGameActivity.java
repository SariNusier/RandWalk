package com.randwalk.game.newgame.level1.b.activities;

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
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.randwalk.game.Other.EndpointsAsyncTask;
import com.randwalk.game.Other.Try;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.views.Level1aPathView;
import com.randwalk.game.newgame.level1.c.activities.Level1cGameActivity;


import java.util.Random;

public class Level1bGameActivity extends Activity {

    final int MAXIMUM_TRANSITION_SCORE = 300;
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
    TextView popIn;
    Toast toast;
    Button introButton;
    Point placedPiratePos;
    Point currentPiratePos;
    Point prevPiratePos;
    Animator.AnimatorListener animatorListener, scorePopUpAnimListener, highLightAnimListener;
    SensorEventListener sensorEventListener;

    String[] outText = {"Sink me!","Oups, pirate went too far, the crew returned him to the pub.",
            "Arrrgh!!!","That Clap of Thunder killed me!"};
    String[] missText = {"Almost there!","Splash! \"Grrr, I'll swim to the boat!\"","The crew dragged you home!","Mermaids rescued you!"};
    String[] homeText = {"This choice seems to work - I must try again!","Yay - let me help my friends as well!","Lucky you! Chances of coming home were not that high!"};

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    boolean piratePlaced, isGuideOn;
    boolean drawing = false;
    boolean fadeIn;

    float drift_X = 0;
    float drift_Y = 0;

    float startingPoint, finalPointX, finalPointY, length = 0;
    int sig_Y=15;//this is stdev for normal distribution along Y axes
    int d=3;//this is step along x axis
    int random_X,random_Y;
    int walkCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.activity_level1b_game);
        d = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, d, getResources().getDisplayMetrics());
        mainLayout = (RelativeLayout) findViewById(R.id.level1b_mainlayout);
        introLayout = (RelativeLayout) findViewById(R.id.level1b_intro_layout);
        startAreaView = findViewById(R.id.level1b_startarea_view);
        pirateView = findViewById(R.id.level1b_pirate_view);
        boatView = findViewById(R.id.level1b_boat_view);
        //highLightView = findViewById(R.id.level1b_highlight_view);
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

        LayoutInflater mInflater = getLayoutInflater();
        View mLayout = mInflater.inflate(R.layout.level1_toast,
                                         (ViewGroup) findViewById(R.id.level1_toast));
        popIn = (TextView) mLayout.findViewById(R.id.toast_text);
        toast = new Toast(getApplicationContext());
        toast.setView(mLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, d*3);

        placedPiratePos.x = (int)pirateView.getX();
        placedPiratePos.y = (int)pirateView.getY();
        startingPoint = placedPiratePos.y;
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float  y;
                drift_X = 0;//x*2;
                y = event.values[1];
                drift_Y = y;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer,
                                       SensorManager.SENSOR_DELAY_NORMAL);

       // preferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
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

                if(boat_X <= pirate_X && boat_Y<=pirate_Y && pirate_X <= boat_X+boat_W &&
                   pirate_Y <= boat_Y+boat_H)
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
                    prevPiratePos = currentPiratePos;
                    pirateStep();
                    pirateView.animate().x(currentPiratePos.x).y(currentPiratePos.y).setDuration(1)
                              .setListener(animatorListener);
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
                if(!piratePlaced)
                    placePirate();
                else if(piratePlaced && walkCounter < 2 && !drawing)
                    positionPirate();
                return false;
            }
        });


    } //end of onCreate()

    public void placePirate() {
        if(isGuideOn) {
            guideText.setText("Help the pirate reach the boat by tilting the phone.");
        }
        else {guideText.setVisibility(View.GONE);
           // highLightView.setVisibility(View.GONE);
           }

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
        pirateView.animate().x(currentPiratePos.x).y(currentPiratePos.y).setDuration(100)
                  .setListener(animatorListener);
        drawPath();
    }

    public void drawPath(){
        Point start = new Point(prevPiratePos.x+pirateView.getWidth()/2,
                prevPiratePos.y+pirateView.getHeight()/2);
        Point end = new Point(currentPiratePos.x+pirateView.getWidth()/2,
                currentPiratePos.y+pirateView.getHeight()/2);
        pathView.drawLine(start, end);
    }

    public void pirateStep(){
        Random rand = new Random();
        random_X = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
        random_Y = (int)Math.floor(rand.nextGaussian()*sig_Y) ; //was 30/60 but I think 20/40 looks better
        currentPiratePos = new Point(currentPiratePos.x+random_X,currentPiratePos.y +
                                     random_Y + (int)drift_Y);
    }

    public void onTheBoat(){
        increaseScore(200);
        Random r = new Random();
        popIn.setText(homeText[r.nextInt(homeText.length - 1)]);
        toast.show();
        walkFinished();
    }

    public void closeToBoat(){
        double h = mainLayout.getHeight();
        double distance;
        distance = currentPiratePos.y - boatView.getTop();

        increaseScore((int)(100*(1-(distance/h))));
        Random r = new Random();
        popIn.setText(missText[r.nextInt(missText.length - 1)]);
        toast.show();
        walkFinished();
    }

    public void missedTheBoat(){
        increaseScore(0);
        Random r = new Random();
        popIn.setText(outText[r.nextInt(outText.length - 1)]);
        toast.show();
        walkFinished();
    }

    public void updateScore(){
        scoreView.setText("Score: " + preferences.getInt("score_1B", 0));
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
        editor.putInt("score_1B",amount + preferences.getInt("score_1B",0));
        editor.apply();
        updateScore();
        popUpScore(currentPiratePos, amount);
        if(!preferences.getBoolean("level1CUnlocked",false) &&
            preferences.getInt("score_1B",0)>=MAXIMUM_TRANSITION_SCORE)
            goToLevel1C();
    }

    public void walkFinished(){
        if(isGuideOn){
            if(walkCounter == 0)
                guideText.setText("Tap anywhere on the screen to make the pirate walk again from the same position.");
        }
        drawing = false;
        finalPointX = pirateView.getX();
        finalPointY = pathView.getY();
        Try save = new Try(this, "0", preferences.getInt("score_1A", 0),
                           startingPoint, finalPointX, finalPointY, length, "B");
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
        drawWalk();
    }

    public void popUpScore(Point coordinate, int amount){

        scorePopUp.setX(mainLayout.getWidth() - scorePopUp.getWidth() * 1.5f);
        scorePopUp.setY(coordinate.y);
        scorePopUp.setText("+" + amount);
        scorePopUp.setVisibility(View.VISIBLE);
        scorePopUp.animate().y(coordinate.y - 150).setDuration(500)
                  .setListener(scorePopUpAnimListener);
    }

    public void nextIntro(View v){
        textViewIntro.setVisibility(View.GONE);
        introLayout.setBackground(null);

        endIntro(v);
        guideText.setText("Tap anywhere on the screen to start walking.");
        fadeIn = false;


    }

    public void endIntro(View v){
        introLayout.setVisibility(View.GONE);
    }

    public void goToLevel1C(){
        editor.putBoolean("level1CUnlocked",true);
        editor.commit();
        startActivity(new Intent(this, Level1cGameActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.level1b_mainlayout));
        System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
