package com.randomwalk.game.GameViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by sari on 17/03/15.
 */
public class Level2bWalkView extends View {
    Canvas canvas;
    Bitmap walkBitmap;
    Paint walkPaint;
    DisplayMetrics metrics;
    Point a;
    Point b;

    public Level2bWalkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        walkPaint = new Paint();
        walkPaint.setColor(Color.BLACK);
        walkPaint.setStrokeWidth(3);
        walkPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        metrics = new DisplayMetrics();
        walkBitmap = Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(walkBitmap);
        a = new Point(0,0);
        b = new Point(0,0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawLine(a.x,a.y,b.x,b.y,walkPaint);
        canvas.drawBitmap(walkBitmap,0,0,walkPaint);
    }

    public void drawLine(TFView t){
        //canvas.drawLine(500,500,700,700,walkPaint);
        Log.d("DRAWING LINE","DRAWING LINE: "+a+" "+b);
        canvas.drawLine(t.getPrevCoordinates().x,t.getPrevCoordinates().y,t.getCoordinates().x,t.getCoordinates().y,walkPaint);
        this.a = a;
        this.b = b;
        invalidate();
    }
}
