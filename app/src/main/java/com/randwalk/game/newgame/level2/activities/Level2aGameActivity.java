package com.randwalk.game.newgame.level2.activities;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.randwalk.game.R;
import com.randwalk.game.newgame.level2.views.Level2aPathView;
import com.randwalk.game.newgame.level2.views.TFView;

import java.util.ArrayList;

public class Level2aGameActivity extends Activity {

    View dnaLeft,dnaMid,dnaRight;
    View startAreaView;
    View rightSideView, leftSideView;
    Level2aPathView pathView;
    RelativeLayout mainLayout;
    Animator.AnimatorListener animatorListener;
    int drawIndex;
    ArrayList<TFView> tfViews;

    boolean walking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2a_game);
        dnaLeft = findViewById(R.id.level2a_dna_left_view);
        dnaMid = findViewById(R.id.level2a_dna_mid_view);
        dnaRight = findViewById(R.id.level2a_dna_right_view);
        startAreaView = findViewById(R.id.level2a_startarea_view);
        mainLayout = (RelativeLayout) findViewById(R.id.level2a_mainlayout);
        rightSideView = findViewById(R.id.level2a_right_view);
        leftSideView = findViewById(R.id.level2a_left_view);
        pathView = (Level2aPathView) findViewById(R.id.level2a_path_view);
        tfViews = new ArrayList<>();

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];

                dnaMid.getLocationOnScreen(location);
                float dna_X = location[0];
                float dna_Y = location[1];
                float dna_W = dnaMid.getWidth();
                float dna_H = dnaMid.getHeight();
                float tfView_X = tfViews.get(drawIndex).getX()+tfViews.get(drawIndex).getWidth()/2;
                float tfView_Y = tfViews.get(drawIndex).getY()+tfViews.get(drawIndex).getHeight()/2;

                if(tfView_Y >= dna_Y){
                    if(tfView_X >= dna_X && tfView_X <= dna_W+dna_X)
                        insideActiveRegion();
                    else
                        outsideActiveRegion();
                    if(drawIndex == tfViews.size()-1)
                        finishWalk();
                    else{
                        drawIndex++;
                        tfViews.get(drawIndex).step(leftSideView,rightSideView);
                        tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(1).setListener(animatorListener);
                        pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(),tfViews.get(drawIndex).getCoordinates());
                    }

                } else
                {
                    tfViews.get(drawIndex).step(leftSideView,rightSideView);
                    tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(1).setListener(animatorListener);
                    pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(),tfViews.get(drawIndex).getCoordinates());
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

    }

    public void placeTF(MotionEvent event){
        if(event.getY() <= startAreaView.getHeight() && tfViews.size()<10 && !walking) {
            tfViews.add(new TFView(this, new Point((int) event.getX(), startAreaView.getHeight() / 2)));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
            params.leftMargin = Math.round(event.getX());
            params.topMargin = startAreaView.getHeight() / 2;
            mainLayout.addView(tfViews.get(tfViews.size() - 1), params);
        }
    }

    public void startWalk(){
        if(tfViews.size()>=5 && !walking) {
            Toast.makeText(this, "WALKING", Toast.LENGTH_LONG).show();
            walking = true;
            drawIndex = 0;
            tfViews.get(drawIndex).step(leftSideView,rightSideView);
            tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(1).setListener(animatorListener);
            pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(),tfViews.get(drawIndex).getCoordinates());

        }
    }

    public void insideActiveRegion(){
        Toast.makeText(this,"In :)",Toast.LENGTH_SHORT).show();
    }
    public void outsideActiveRegion(){Toast.makeText(this,"Out :(",Toast.LENGTH_SHORT).show();}
    public void finishWalk(){Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();}
}
