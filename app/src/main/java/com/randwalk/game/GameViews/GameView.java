package com.randwalk.game.GameViews;

import java.util.Random;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.randwalk.game.Activities.Level1Activity;
import com.randwalk.game.Other.EndpointsAsyncTask;
import com.randwalk.game.R;
import com.randwalk.game.Other.Try;


public class GameView extends View implements OnTouchListener, SensorEventListener {

    float start_X, start_Y;
    float X,Y;
    float drift_X = 0;
    float drift_Y = 0;
    int random_X, random_Y;
    float startingPoint, finalPointX, finalPointY, length = 0;
    int ok = 0;
    boolean bitmapSaved = false;
    boolean isStarted = false;
    boolean listenTouch = true;
    int saveTry = 0;
    String subLevel;
    Random rand;

    double A; //this is just a constant so that we are able to calculate proper size of the boat
    int xBoat; //half size of the boat
    int sig_Y=15;//this is stdev for normal distribution along Y axes
    int d=3;//this is step along x axis

    Drawable boat;
    Paint paintWalk;
    Canvas canvas;
    public Bitmap playingBitmap; //this bitmap stores the game status during play
    public Bitmap initialBitmap; //this bitmap stores the initial, clean state of the screen
    DisplayMetrics metrics;
    Drawable pirate;
    int currentScore;
    int levelBCounter = 0;

    Level1Activity parentActivity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //Sounds sounds = new Sounds();
    //<-----CONSTRUCTOR------>
    public GameView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);



        /*
        <===== INITIALISATIONS =====>
         */
        paintWalk = new Paint(); //construct paint for drawing path/walk
        rand = new Random();
        parentActivity =(Level1Activity) context; //casts the context as a Level1Activity to use updateScore()
       // subLevel = parentActivity.getSubLevel(); // gets selected sublevel

        preferences = context.getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);//get preferences GAME_DATA
        editor = preferences.edit(); //sets the editor as editor of preferences declared above
        metrics = getResources().getDisplayMetrics(); //gets the metrics of the screen
        playingBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        initialBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(playingBitmap); //canvas draw into a bitmap
        canvas.drawColor(Color.parseColor("#DED9D9")); //sets the color of the background
        pirate = this.getResources().getDrawable(R.drawable.rsz_pirate); // the pirate arrrr!
        Drawable startSurface = this.getResources().getDrawable(R.drawable.start_surface);//area where the player starts
        startSurface.setBounds(0,0,metrics.widthPixels/6,metrics.heightPixels);
        startSurface.draw(canvas);
        boat = this.getResources().getDrawable(R.drawable.boat); // just boat
        subLevel = parentActivity.getSubLevel();
        currentScore = preferences.getInt(String.format("score_1%s",subLevel),0);
        Rect boundsBoatA;
        Rect boundsBoatB;
        A=sig_Y*Math.sqrt(2)*0.272/Math.sqrt(d);
        xBoat=(int)Math.floor(0.25*(-Math.pow(A,2)+Math.sqrt(Math.pow(A,4)+3.6*metrics.widthPixels*Math.pow(A,2))));

        boundsBoatA = new Rect(metrics.widthPixels-2*xBoat,metrics.heightPixels/2 -xBoat,metrics.widthPixels,metrics.heightPixels/2 +xBoat);
        boundsBoatB = new Rect(metrics.widthPixels-100,metrics.heightPixels/12*9-50,metrics.widthPixels,metrics.heightPixels/12*9+50);
        if(subLevel.equals("A"))
            boat.setBounds(boundsBoatA);
        else {
            boat.setBounds(boundsBoatB);
            pirate.setBounds(metrics.widthPixels/12 - 50, 4, metrics.widthPixels/12 + 50, 136); //draws pirate here
            pirate.draw(canvas);
        }
        boat.draw(canvas);


        if(!subLevel.equals("A")) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        paintWalk.setColor(getResources().getColor(R.color.BlueLine)); //set the color of the walk
        paintWalk.setStrokeWidth(3); //sets the width of the walk
        paintWalk.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0)); //sets the dash effect of the walk
        paintWalk.setStyle(Paint.Style.STROKE);



    }

    @Override
    public void onDraw(final Canvas canvas) {

        if(boat.getBounds().contains((int)start_X,(int)start_Y)){
            listenTouch = true;
            editor.putInt(String.format("score_1%s",subLevel), 100 + preferences.getInt(String.format("score_1%s",subLevel),0));
            editor.commit();
            parentActivity.updateScore("h");
            if(levelBCounter ==  10 || ok == 4){
                parentActivity.updateScore("r");
            }
            saveTry++;
        }
        else
            if(start_X >= metrics.widthPixels){
                listenTouch = true;
                int score;
                if (start_Y==boat.getBounds().centerY())
                    score = 1000;
                else
                    score= Math.round((5/Math.abs(start_Y - boat.getBounds().centerY()))*1000);
                editor.putInt(String.format("score_1%s",subLevel),score + preferences.getInt(String.format("score_1%s",subLevel),0));
                editor.commit();
                parentActivity.updateScore("m");

                if(levelBCounter == 10 || ok == 4){
                    parentActivity.updateScore("r");
                }
                saveTry++;
            }
            else
                if(start_Y <=0 || start_Y >=metrics.heightPixels){
                    if(isStarted)
                        parentActivity.updateScore("o");

                    if(levelBCounter == 10 || ok == 4){
                        parentActivity.updateScore("r");
                    }
                    listenTouch = true;
                    saveTry++;
                }
                else
                    if (isStarted == true) {
                        drawTick();
                        //sounds.execute(new Pair<Context, Integer>(this.getContext(),R.raw.background_sound));
                    }

        if(saveTry == 2 && length !=0) {
            finalPointX = start_X;
            finalPointY = start_Y;
            Try save = new Try(getContext(), "0", preferences.getInt(String.format("score_1%s",subLevel), 0), startingPoint, finalPointX, finalPointY, length, subLevel);
            new EndpointsAsyncTask().execute(new Pair<Context, Try>(getContext(), save));
            length = 0;
            saveTry = 1;
        }
        isStarted = true;
        canvas.drawBitmap(playingBitmap, 0, 0, paintWalk);
        if(bitmapSaved == false) {
            initialBitmap = Bitmap.createBitmap(playingBitmap, 0, 0, playingBitmap.getWidth(), playingBitmap.getHeight(), null, true);
            bitmapSaved = true;
        }
        if(preferences.getInt("score_1A",0)<1000){
            editor.putBoolean("level1BUnblocked",false);
        }
        else
        {
            editor.putBoolean("level1BUnblocked",true);
        }
        if(preferences.getInt(String.format("score_1%s",subLevel),0) - currentScore >= 1000)
            if(subLevel.equals("A")){

                currentScore = preferences.getInt(String.format("score_1%s",subLevel),0);
                editor.putBoolean("level1BUnlocked",true);
                editor.commit();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parentActivity.endLevel();

            }
            else
            {
                currentScore = preferences.getInt(String.format("score_1%s",subLevel),0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parentActivity.endLevel();

            }
        else if(!preferences.getBoolean("level1BUnblocked",false) && preferences.getInt("score_1A",0) >= 1000 && subLevel.equals("A")){
            editor.putBoolean("level1BUnlocked",true);
            editor.commit();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parentActivity.endLevel();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(listenTouch == true) {

            if (event.getX() <= metrics.widthPixels / 6 && ok < 1) {
                paintWalk.setColor(getResources().getColor(R.color.BlueLine));
                ok++;
                listenTouch = false;
                if (subLevel.equals("A")){
                    X = event.getX();
                    Y = event.getY();
                }
                else
                {
                    X = 0;
                    Y = 70;
                }
                start_X = metrics.widthPixels/12;
                start_Y = Y;

                pirate.setBounds(metrics.widthPixels / 12 - 50, (int) start_Y - 66, metrics.widthPixels / 12 + 50, (int) start_Y + 66); //draws pirate here
                pirate.draw(canvas);
                startingPoint = Y;
                invalidate();


            }
            else if(subLevel.equals("B")){
                if(levelBCounter <10){
                    start_X = metrics.widthPixels/12;
                    start_Y = 70;
                    listenTouch = false;
                    levelBCounter++;
                    if(levelBCounter == 10) {

                    }
                    invalidate();
                }
                else{
                    canvas.drawBitmap(initialBitmap,0,0,paintWalk);
                    listenTouch = true;
                    levelBCounter = 0;
                    invalidate();
                }

            }
            else
            if (ok >= 1 && ok < 4) {
                switch (ok){
                    case 1:paintWalk.setColor(getResources().getColor(R.color.GreenLine));break;
                    case 2:paintWalk.setColor(getResources().getColor(R.color.RedLine));break;
                    case 3:paintWalk.setColor(getResources().getColor(R.color.YellowLine));break;
                }
                ok++;
                listenTouch = false;
                start_Y = Y;
                start_X = metrics.widthPixels/12;
                if(ok ==4){
                   parentActivity.updateScore("r");
                }
                invalidate();
            }
            else {
                if(ok == 4) {
                }
                ok = 0;
                canvas.drawBitmap(initialBitmap,0,0,paintWalk);

            }
        }
        return true;

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        float x, y, z;
        x = event.values[0];
        drift_X = 0;//x*2;
        y = event.values[1];
        drift_Y = y;
        z = event.values[2];

    }

    public void drawTick(){

        if(subLevel.equals("A")) {
            random_X = d;//(int)Math.abs(Math.floor(rand.nextGaussian()*20)); //generates two random numbers for X and Y
            random_Y = (int)Math.floor(rand.nextGaussian()*sig_Y); //was 30/60 but I think 20/40 looks better
        }
        else
        {
            random_X = d;
            random_Y = (int)Math.floor(rand.nextGaussian()*15);
        }

        canvas.drawLine(start_X, start_Y,start_X+random_X, start_Y + random_Y + drift_Y, paintWalk);
        length += Math.sqrt(Math.pow(random_X+drift_X,2)+Math.pow(random_Y+drift_Y,2));
        start_X = start_X+random_X + drift_X;
        start_Y = start_Y + random_Y + drift_Y;
        invalidate();


    }

}

