package com.randwalk.game.newgame.level2.views;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.randwalk.game.Activities.Level2BActivity;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level2.activities.Level2aGameActivity;
import com.randwalk.game.newgame.level2.activities.Level2cGameActivity;

import java.util.Random;

/**
 * Created by sari on 17/03/15.
 */
public class Level2cMainLayout extends RelativeLayout implements View.OnTouchListener{

    GestureDetector gd;
    Level2cGameActivity parentActivity;
    Random rand;
    Animator.AnimatorListener animatorListener;

    public Level2cMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(new GestureListener());
        parentActivity = (Level2cGameActivity) context;
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


//    private void startDrawingWalk() {
    //    parentActivity.startDrawingWalk();
    //  }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent event) {
            if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN &&
               parentActivity.finished == 2) {
                parentActivity.showEndText();

            }
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            if(parentActivity.finished == 2){
                parentActivity.endLevel();
            }
             else {
                parentActivity.startWalkTop();
                parentActivity.startWalkBot();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //parentActivity.splitLevel();
            return super.onFling(e1, e2, velocityX, velocityY);
            //return true;
        }

    }
}


