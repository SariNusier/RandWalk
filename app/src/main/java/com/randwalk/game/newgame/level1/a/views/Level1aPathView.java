package com.randwalk.game.newgame.level1.a.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by sari on 17/07/15.
 */
public class Level1aPathView extends View {
    Canvas canvas;
    Bitmap walkBitmap;
    Paint walkPaint;
    DisplayMetrics metrics;

    public Level1aPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        walkPaint = new Paint();
        walkPaint.setColor(Color.BLACK);
        walkPaint.setStrokeWidth(3);
        walkPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        metrics = new DisplayMetrics();
        walkBitmap = Bitmap.createBitmap(metrics.widthPixels,metrics.heightPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(walkBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(walkBitmap,0,0,walkPaint);
    }

    public void drawLine(Point start,Point end){
        canvas.drawLine(start.x,start.y,end.x,end.y,walkPaint);
        invalidate();
    }
}