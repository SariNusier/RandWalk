package com.randomwalk.game.Activities;

import android.animation.Animator;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.randomwalk.game.GameViews.Level2bWalkView;
import com.randomwalk.game.GameViews.TFView;
import com.randomwalk.game.R;

import java.util.ArrayList;
import java.util.Random;

public class Level2BActivity extends ActionBarActivity {
    View dnaView, startingView;
    Level2bWalkView walkView;
    ViewGroup mainLayout;
    ArrayList<TFView> TFViews;
    Random rand;
    Animator.AnimatorListener animatorListener;
    DisplayMetrics displayMetrics;
    int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_b);
        dnaView = findViewById(R.id.level2b_dna_view);
        startingView = findViewById(R.id.level2b_startarea_view);
        walkView = (Level2bWalkView) findViewById(R.id.level2b_walk_view);
        mainLayout = (ViewGroup) findViewById(R.id.level2B_mainlayout);
        TFViews = new ArrayList<>();
        rand = new Random();
        displayMetrics = new DisplayMetrics();
        animatorListener = new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(counter < 50)
                for(TFView t : TFViews) {
                    t.takeOneStep();
                    walkView.drawLine(t);
                    t.animate().x(t.getCoordinates().x).y(t.getCoordinates().y).setDuration(100).setListener(animatorListener);

                }
                counter++;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        };

    }


    public void placeTFView(MotionEvent event) {

        TFViews.add(new TFView(this, new Point(Math.round(event.getX()), startingView.getHeight() / 2)));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        params.leftMargin = Math.round(event.getX());
        params.topMargin = startingView.getHeight() / 2;
        mainLayout.addView(TFViews.get(TFViews.size() - 1), params);
        //mainLayout.invalidate();
    }


    public void startDrawingWalk() {
        for(TFView t : TFViews) {
            t.takeOneStep();
            walkView.drawLine(t);
            t.animate().x(t.getCoordinates().x).y(t.getCoordinates().y).setDuration(100).setListener(animatorListener);

        }
        counter++;
    }

    public ArrayList<TFView> getTFViews() {
        return TFViews;
    }
}



