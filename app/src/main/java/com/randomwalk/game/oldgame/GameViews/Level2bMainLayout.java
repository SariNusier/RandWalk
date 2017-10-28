package com.randomwalk.game.GameViews;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.randomwalk.game.Activities.Level2BActivity;
import com.randomwalk.game.R;

import java.util.Random;

/**
 * Created by sari on 17/03/15.
 */
public class Level2bMainLayout extends RelativeLayout implements View.OnTouchListener{

    GestureDetector gd;
    Level2BActivity parentActivity;
    Random rand;
    Thread animation;
    int drawing = 0;
    Animator.AnimatorListener animatorListener;

    public Level2bMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(new GestureListener());
        parentActivity = (Level2BActivity) context;
        rand = new Random();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


    private void startDrawingWalk() {
        parentActivity.startDrawingWalk();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent event) {
            if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                if (event.getY()<=findViewById(R.id.level2b_startarea_view).getBottom()) {
                    parentActivity.placeTFView(event);
                }

            }
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            startDrawingWalk();
            return true;
        }

    }
}


