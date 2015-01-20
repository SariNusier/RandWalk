package com.example.sari.randomwalk;





import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    Path path = new Path();
    int ok = 0;
    int random;
    int counter = 0;
    Canvas canvas;
    Bitmap bitmap;
    Random rand;
    Paint paintBoundries = new Paint();
    DisplayMetrics metrics;
    boolean canvasCheck = false;

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        //bitmap = new Bitmap();
        this.setOnTouchListener(this);

        rand = new Random();
        //canvas = new Canvas(bitmap);
        paint.setColor(Color.WHITE);
        paintBoundries.setColor(Color.YELLOW);
        paintBoundries.setStrokeWidth(3);
        paint.setStrokeWidth(5);
        //if(Bitmap.Config.ARGB_8888 == null || canvas.getHeight() == 0 || canvas.getWidth() == 0)
        //	Log.d("BITMAP","Is null");
        //else
        metrics = getResources().getDisplayMetrics();
        Log.d("HEIGHT","Height"+metrics.heightPixels);
        Log.d("WIDTH","Height"+metrics.widthPixels);
        bitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);


    }

    @Override
    public void onDraw(final Canvas canvas) {
        //canvas.drawLine(50,50, 10, 1000, paint);
        //canvas.drawLine(20, 0, 0, 20, paint);
        paint.setStyle(Paint.Style.STROKE);
        this.canvas.drawLine(metrics.widthPixels/6,0,metrics.widthPixels/6,metrics.heightPixels,paintBoundries);
        this.canvas.drawLine(metrics.widthPixels, metrics.heightPixels/2-100,
                metrics.widthPixels-20, metrics.heightPixels/2-100, paint);

        this.canvas.drawLine(metrics.widthPixels, metrics.heightPixels/2+100,
                metrics.widthPixels-20, metrics.heightPixels/2+100, paint);

        this.canvas.drawLine(metrics.widthPixels-20, metrics.heightPixels/2-100,
                metrics.widthPixels-20, metrics.heightPixels/2+100, paint);
        if (ok == 1 ) {//&& start_X <=500 && start_Y<=500
            if(start_X + 25 == metrics.widthPixels)
                if(start_Y >= metrics.heightPixels/2-100+25 && start_Y <= metrics.heightPixels/2+100+25)
                    Toast.makeText(getContext(), "YOU ARE HOME!", Toast.LENGTH_SHORT);
                else
                    Toast.makeText(getContext(), "Almost There"+ Math.abs(start_Y - metrics.heightPixels/2), Toast.LENGTH_SHORT);

            drawTick();
            //Log.d("COUNTER","COUNTER + "+counter);
            counter++;


        }
        //drawTick();
        canvas.drawBitmap(bitmap, 0, 0, paint);



    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(ok<1 && event.getX()<=metrics.widthPixels/6)
        {	ok++;
            start_X = event.getX();
            start_Y = event.getY();invalidate();}

        return true;
    }



    public void drawTick(){

        int distance = 25;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        random = rand.nextInt(7);

        switch (random) {
            case 0: {
                canvas.drawLine(start_X, start_Y, start_X, start_Y - distance,
                        paint);

                start_Y = start_Y - distance;
                break;
            }
            case 1: {

                canvas.drawLine(start_X, start_Y, start_X, start_Y + distance,
                        paint);
                start_Y = start_Y + distance;
                break;
            }
//		case 2: {
//
//
//			canvas.drawLine(start_X, start_Y, start_X - distance, start_Y,
//					paint);
//			start_X = start_X - distance;
//		}
            case 3: {


                canvas.drawLine(start_X, start_Y, start_X + distance, start_Y,
                        paint);
                start_X = start_X + distance;
                break;
            }

//		case 4: {
//			canvas.drawLine(start_X, start_Y, start_X - distance, start_Y - distance,
//					paint);
//
//			start_Y = start_Y - distance;
//			start_X = start_X - distance;
//			break;
//		}
            case 5: {

                canvas.drawLine(start_X, start_Y, start_X + distance, start_Y + distance,
                        paint);
                start_Y = start_Y + distance;
                start_X = start_X + distance;
                break;
            }
//		case 6: {
//
//
//			canvas.drawLine(start_X, start_Y, start_X - distance, start_Y + distance,
//					paint);
//			start_X = start_X - distance;
//			start_Y = start_Y + distance;
//		}
            case 7: {


                canvas.drawLine(start_X, start_Y, start_X + distance, start_Y - distance,
                        paint);
                start_X = start_X + distance;
                start_Y = start_Y - distance;
                break;
            }

        }

        invalidate();


    }


}

