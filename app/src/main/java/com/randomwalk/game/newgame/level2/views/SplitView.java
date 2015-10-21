package com.randomwalk.game.newgame.level2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class SplitView extends View implements View.OnTouchListener{
    GestureDetector gd;
    public SplitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(new GestureListener());

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Toast.makeText(getContext(),"FLING!",Toast.LENGTH_SHORT).show();

            return super.onFling(e1, e2, velocityX, velocityY);
            //return true;
        }
    }
}
