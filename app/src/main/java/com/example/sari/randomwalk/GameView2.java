package com.example.sari.randomwalk;

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
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.Random;


public class GameView2 extends View implements OnTouchListener, SensorEventListener {

    float start_X, start_Y;
    float X,Y;
    float drift_X = 0;
    float drift_Y = 0;
    int random_X, random_Y;
    int ok = 0;
    int cellCount = 0;
    boolean bitmapSaved = false;
    boolean isStarted = false;
    boolean listenTouch = true;
    String subLevel;
    Random rand;

    Paint paintWalk;
    Canvas canvas;
    Bitmap playingBitmap; //this bitmap stores the game status during play
    Bitmap initialBitmap; //this bitmap stores the initial, clean state of the screen
    DisplayMetrics metrics;
    Drawable pirate;

    Level2Activity parentActivity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //Sounds sounds = new Sounds();
    //<-----CONSTRUCTOR------>
    public GameView2(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);



        /*
        <===== INITIALISATIONS =====>
         */
        paintWalk = new Paint(); //construct paint for drawing path/walk
        rand = new Random();
        parentActivity =(Level2Activity) context; //casts the context as a Level1Activity to use updateScore()
       // subLevel = parentActivity.getSubLevel(); // gets selected sublevel
        preferences = context.getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);//get preferences GAME_DATA
        editor = preferences.edit(); //sets the editor as editor of preferences declared above
        metrics = getResources().getDisplayMetrics(); //gets the metrics of the screen
        playingBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        initialBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(playingBitmap); //canvas draw into a bitmap
        canvas.drawColor(Color.parseColor("#c2d5f0")); //sets the color of the background
        pirate = this.getResources().getDrawable(R.drawable.rsz_pirate); // the pirate arrrr!
        Drawable startSurface = this.getResources().getDrawable(R.drawable.start_surface_2);//area where the player starts
        startSurface.setBounds(0,0,metrics.widthPixels,metrics.heightPixels/12);
        startSurface.draw(canvas);

        Drawable dnaImage = this.getResources().getDrawable(R.drawable.dna);

        dnaImage.setBounds(0,(int)Math.round(metrics.heightPixels/1.4),metrics.widthPixels,metrics.heightPixels);
        dnaImage.draw(canvas);
        subLevel = parentActivity.getSubLevel();


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

        if ( start_X<=metrics.widthPixels && start_X >= metrics.widthPixels-100 && start_Y>= metrics.heightPixels/2 -50 && start_Y<=metrics.heightPixels/2 + 50) {
            listenTouch = true;
            Log.d("HOME", "You are home");
            editor.putInt(String.format("score_1%s", subLevel), 100 + preferences.getInt(String.format("score_1%s",subLevel),0));
            editor.commit();
            parentActivity.updateScore();
        }
        else
            if(start_X >= metrics.widthPixels){
                listenTouch = true;
                int score = Math.round((5/Math.abs(start_Y - metrics.heightPixels/2))*1000);
                Log.d("SCORE","Your score is: "+score);
                editor.putInt(String.format("score_1%s", subLevel), score + preferences.getInt(String.format("score_1%s", subLevel), 0));

                editor.commit();
                parentActivity.updateScore();
            }
            else
                if(start_Y <=0 || start_Y >=metrics.heightPixels){
                    Log.d("OUT OF BOUNDS","OUT OF BOUNDS!");
                    listenTouch = true;
                }
                else
                    if (isStarted == true) {
                        drawTick();
                        //sounds.execute(new Pair<Context, Integer>(this.getContext(),R.raw.background_sound));
                    }
        isStarted = true;


        canvas.drawBitmap(playingBitmap, 0, 0, paintWalk);
        if(bitmapSaved == false) {
            initialBitmap = Bitmap.createBitmap(playingBitmap, 0, 0, playingBitmap.getWidth(), playingBitmap.getHeight(), null, true);
            bitmapSaved = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        
        if(listenTouch == true) {

            if (event.getY() <= metrics.heightPixels / 12 && ok < 1) {
                paintWalk.setColor(getResources().getColor(R.color.BlueLine));
                cellCount++;
                if(cellCount == 5)
                    ok++;
                listenTouch = false;
                X = event.getX();
                Y = event.getY();
                start_Y = metrics.widthPixels/24;
                start_X = X;
                pirate.setBounds((int)start_X - 66,metrics.heightPixels/24 - 50,(int)start_X + 66,metrics.widthPixels/24 + 50);
                Log.d("BOUNDS"," "+metrics.widthPixels/12);
                pirate.draw(canvas);
                invalidate();


            }
            else {
                ok = 0;
                cellCount = 0;
                canvas.drawBitmap(initialBitmap,0,0,paintWalk); //restart everything.

            }
        }
        return true;

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }
    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Many sensors return 3 values, one for each axis.
        float x, y, z;
        x = event.values[0];
        drift_X = 0;//x*2;
        y = event.values[1];
        drift_Y = y*2;
        z = event.values[2];

    }

    public void drawTick(){
        /*
       try {
            Thread.sleep();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        if(subLevel.equals("A")) {
            random_X = rand.nextInt(31); //generates two random numbers for X and Y
            random_Y = rand.nextInt(61)-30;
        }
        else
        {
            random_X = rand.nextInt(21);
            random_Y = rand.nextInt(41)-20;
        }

        canvas.drawLine(start_X, start_Y,start_X+random_X, start_Y + random_Y + drift_Y, paintWalk);
        start_X = start_X+random_X + drift_X;
        start_Y = start_Y + random_Y + drift_Y;
        invalidate();


    }

}

