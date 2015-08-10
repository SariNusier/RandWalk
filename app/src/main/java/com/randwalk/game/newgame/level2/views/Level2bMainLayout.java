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

import java.util.Random;

/**
 * Created by sari on 17/03/15.
 */
public class Level2bMainLayout extends RelativeLayout implements View.OnTouchListener{

    GestureDetector gd;
    Level2aGameActivity parentActivity;
    Random rand;
    Animator.AnimatorListener animatorListener;

    public Level2bMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(new GestureListener());
        parentActivity = (Level2aGameActivity) context;
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
            if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                //if (event.getY()<=findViewById(R.id.level2b_startarea_view).getHeight()) {
                    parentActivity.placeTF(event);
                //}

            }
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent event) {
            parentActivity.startWalk();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Toast.makeText(getContext(),"FLING",Toast.LENGTH_SHORT).show();
            parentActivity.splitLevel();
            return super.onFling(e1, e2, velocityX, velocityY);
            //return true;
        }

    }
}


