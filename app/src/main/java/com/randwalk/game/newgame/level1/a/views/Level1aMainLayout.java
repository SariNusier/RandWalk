package com.randwalk.game.newgame.level1.a.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.randwalk.game.Activities.Level2BActivity;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.activities.Level1aGameActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sari on 17/03/15.
 */
public class Level1aMainLayout extends RelativeLayout{

    GestureDetector gd;
    Level1aGameActivity parentActivity;
    Random rand;
    Thread animation;
    int drawing = 0;
    Animator.AnimatorListener animatorListener;

    public Level1aMainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parentActivity = (Level1aGameActivity) context;
        rand = new Random();
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                parentActivity.placePirate(event);
                return false;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



}


