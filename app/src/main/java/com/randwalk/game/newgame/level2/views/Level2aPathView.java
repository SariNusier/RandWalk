package com.randwalk.game.newgame.level2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.randwalk.game.R;


public class Level2aPathView extends View {
    Canvas canvas;
    Bitmap walkBitmap;
    Paint walkPaint;
    WindowManager wm;
    Display display;
    int[] colors;
    int strokeWidth = 1;
    int indexColor = 0;
    public Level2aPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        walkPaint = new Paint();
        colors = new int[3];
        colors[0] = getResources().getColor(R.color.GreenLine);
        colors[1] = getResources().getColor(R.color.YellowLine);
        //colors[2] = getResources().getColor(R.color.BlueLine);
        colors[2] = getResources().getColor(R.color.RedLine);
        walkPaint.setColor(getColorToUse());
        strokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, strokeWidth, getResources().getDisplayMetrics());
        walkPaint.setStrokeWidth(3);
        walkPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
        walkBitmap = Bitmap.createBitmap(display.getWidth(),display.getHeight(), Bitmap.Config.ARGB_8888);
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

    public void restart(){
        walkBitmap.recycle();
        walkBitmap = Bitmap.createBitmap(display.getWidth(),display.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(walkBitmap);
        invalidate();
    }

    public int getColorToUse(){
        if(indexColor == 3)
            indexColor = 0;

        return colors[indexColor++];
    }

    public void changeColor(){
        walkPaint.setColor(getColorToUse());
    }
}