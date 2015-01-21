package com.example.sari.randomwalk;





import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

/**
 * Created by Sari on 1/20/2015.
 */

public class GameView extends View implements OnTouchListener {
    Paint paint = new Paint();
    float start_X;
    float start_Y;
   // Path path = new Path();
    int ok = 0;
    boolean ok1 = false;
    int  click_counter = 0;
    int random_X, random_Y;
    boolean listenTouch = true;
    float X,Y;
    int counter = 0;
    Canvas canvas;
    Bitmap bitmap;
    Random rand;
    Paint paintBoundries = new Paint();
    DisplayMetrics metrics;
    boolean canvasCheck = false;
    Drawable pirate;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    //CONSTRUCTOR
    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //bitmap = new Bitmap();
        this.setOnTouchListener(this);

         preferences = context.getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);
         editor = preferences.edit();


        rand = new Random();
        //canvas = new Canvas(bitmap);
        paint.setColor(Color.BLUE);
        paintBoundries.setColor(Color.RED);
        paintBoundries.setStrokeWidth(5);
        paint.setStrokeWidth(5);
        //if(Bitmap.Config.ARGB_8888 == null || canvas.getHeight() == 0 || canvas.getWidth() == 0)
        //	Log.d("BITMAP","Is null");
        //else
        metrics = getResources().getDisplayMetrics();
        Log.d("HEIGHT","Height"+metrics.heightPixels);
        Log.d("WIDTH","Height"+metrics.widthPixels);

        bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#DED9D9"));
        Drawable startSurface = this.getResources().getDrawable(R.drawable.start_surface);
        Drawable boat = this.getResources().getDrawable(R.drawable.boat);


        Rect rect1 = new Rect(metrics.widthPixels-200,metrics.heightPixels/2 -100,metrics.widthPixels,metrics.heightPixels/2 +100);
        Log.d("Rect",rect1.toString());
        boat.setBounds(rect1);

        pirate = this.getResources().getDrawable(R.drawable.rsz_pirate);
        startSurface.setBounds(0,0,metrics.widthPixels/6,metrics.heightPixels);


        startSurface.draw(canvas);
        boat.draw(canvas);


    }

    @Override
    public void onDraw(final Canvas canvas) {

        paint.setStyle(Paint.Style.STROKE);
      // paint.setPathEffect(new DashPathEffect())


        if ( start_X<=metrics.widthPixels && start_X >= metrics.widthPixels-200 && start_Y>= metrics.heightPixels/2 -100 && start_Y<=metrics.heightPixels/2 + 100) {
            listenTouch = true;
            Log.d("HOME", "You are home");
            editor.putInt("score", 100 + preferences.getInt("score",0));
            editor.commit();
        }
        else
            if(start_X >= metrics.widthPixels){
                listenTouch = true;

                int score = Math.round((5/Math.abs(start_Y - metrics.heightPixels/2))*1000);
                Log.d("SCORE","Your score is: "+score);
                editor.putInt("score",score + preferences.getInt("score",0));
                editor.commit();
            }
            else
            if (ok1 == true)
            {drawTick();}
            ok1 = true;

 //           drawTick();


        //drawTick();
        canvas.drawBitmap(bitmap, 0, 0, paint);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(listenTouch == true) {

            if (event.getX() <= metrics.widthPixels / 6 && ok<1) {

                ok++;
                listenTouch = false;
                X = event.getX();
                Y = event.getY();
                start_X = X;
                start_Y = Y;

                pirate.setBounds(0, 0, 500, 150);
                pirate.setBounds((int) start_X - 50, (int) start_Y - 66, (int) start_X + 50, (int) start_Y + 66);
                pirate.draw(canvas);
                invalidate();


            }
            if(ok>=1 && ok<=3){
                ok++;
                listenTouch = false;
                start_Y = Y;
                start_X = X;
                invalidate();
            }
        }



        if(ok<1 && event.getX()<=metrics.widthPixels/6)
        {	ok++;
            start_X = event.getX();
            start_Y = event.getY();
            pirate.setBounds(0,0,500,150);
            pirate.setBounds((int)start_X-50,(int)start_Y-66,(int)start_X+50,(int)start_Y+66);
            pirate.draw(canvas);
            invalidate();}

        return true;
    }



    public void drawTick(){

      /*  try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        random_X = rand.nextInt(40);
        random_Y = rand.nextInt(40);
        if(Math.random()<=.5)
            random_Y = random_Y * -1;

        canvas.drawLine(start_X, start_Y,start_X+random_X, start_Y + random_Y,paint);
       start_X = start_X+random_X;
        start_Y = start_Y + random_Y;
        invalidate();


    }


}

