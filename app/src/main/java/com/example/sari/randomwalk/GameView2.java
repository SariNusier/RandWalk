package com.example.sari.randomwalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Random;

//BEWARE OF THIS CLASS. IT WAS WRITTEN BY A SHIT PROGRAMMER SO DO NOT ENTER. IF YOU WANT TO IMPLEMENT SOMETHING LIKE THIS YOU WOULD BE BETTER OFF CREATING EVERYTHING YOURSELF.
public class GameView2 extends View implements OnTouchListener {

    float start_X, start_Y;
    float X,Y;
    float drift_X = 0;
    float drift_Y = 0;
    int random_X, random_Y;
    int ok = 0;
    int cellCount = 0;
    int cellsFinished = 0;
    boolean bitmapSaved = false;
    boolean isStarted = false;
    boolean listenTouch = true;
    String subLevel;
    Random rand;
    ArrayList<Point> points;
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
        points = new ArrayList<>();


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
        paintWalk.setColor(getResources().getColor(R.color.BlueLine)); //set the color of the walk
        paintWalk.setStrokeWidth(3); //sets the width of the walk
        paintWalk.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0)); //sets the dash effect of the walk
        paintWalk.setStyle(Paint.Style.STROKE);



    }

    @Override
    public void onDraw(final Canvas canvas) {
        ArrayList<Integer> pointsToDelete = new ArrayList<>();
        for(Point p : points) {


            if (p.y <= metrics.heightPixels && p.y >= metrics.heightPixels/16*15 && p.x >= metrics.widthPixels/8*3 && p.x <= metrics.widthPixels/8*5) {
                cellsFinished++;
                pointsToDelete.add(points.indexOf(p));
                if(cellsFinished == cellCount)
                    listenTouch = true;
            } else if (p.y > metrics.heightPixels - 100) {

                cellsFinished++;
                pointsToDelete.add(points.indexOf(p));
                if(cellsFinished == cellCount)
                    listenTouch = true;
            }
            else if (p.x <= 0 || p.x >= metrics.widthPixels) {
                Log.d("OUT OF BOUNDS", "OUT OF BOUNDS!");
                listenTouch = true;
            } else if (isStarted == true && cellCount == 5) {
                drawTick(p);
            }
            isStarted = true;

        }
        for(Integer i : pointsToDelete){
            points.remove(i);
        }
        canvas.drawBitmap(playingBitmap, 0, 0, paintWalk);
        if(bitmapSaved == false) {
            initialBitmap = Bitmap.createBitmap(playingBitmap, 0, 0, playingBitmap.getWidth(), playingBitmap.getHeight(), null, true);
            bitmapSaved = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(listenTouch == true) {

            if (event.getY() <= metrics.heightPixels / 12 && ok < 1 && event.getX() > metrics.widthPixels/4 && event.getX() < metrics.widthPixels/4*3 && MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                paintWalk.setColor(getResources().getColor(R.color.BlueLine));
                cellCount++;
                if(cellCount == 5) {
                    ok++;
                    listenTouch = false;
                }
                X = event.getX();
                Y = event.getY();
                start_Y = metrics.widthPixels/24;
                start_X = X;
                points.add(new Point((int)start_X,metrics.widthPixels/24));
                pirate.setBounds((int)start_X - 66,metrics.heightPixels/24 - 50,(int)start_X + 66,metrics.widthPixels/24 + 50);
                pirate.draw(canvas);

                invalidate();


            }
            else if(cellCount == 5) {
                ok = 0;
                cellCount = 0;
                cellsFinished = 0;
                points = new ArrayList<>();
                canvas.drawBitmap(initialBitmap,0,0,paintWalk); //restart everything
                invalidate();
            }
        }
        Log.d("ON TOUCH EVENT","ON TOUCH EVENT"+event.toString());
        return true;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("ON TOUCH","ON TOUCH");
        return false;
    }



    public void drawTick(Point p){

        random_Y = rand.nextInt(31);
        random_X = rand.nextInt(61)-30;
        canvas.drawLine(p.x, p.y,p.x+random_X, p.y + random_Y + drift_Y, paintWalk);
        p.x = p.x + random_X + (int)drift_X;
        p.y = p.y + random_Y + (int)drift_Y;
        invalidate();


    }

}

