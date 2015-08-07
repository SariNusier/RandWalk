package com.randwalk.game.newgame.level2.activities;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.randwalk.game.R;
import com.randwalk.game.newgame.level2.views.TFView;

import java.util.ArrayList;

public class Level2aGameActivity extends Activity {

    View dnaView;
    View startAreaView;
    RelativeLayout mainLayout;
    Animator.AnimatorListener animatorListener;
    int drawIndex;
    ArrayList<TFView> tfViews;

    boolean walking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2a_game);
        dnaView = findViewById(R.id.level2a_dna_view);
        startAreaView = findViewById(R.id.level2a_startarea_view);
        mainLayout = (RelativeLayout) findViewById(R.id.level2a_mainlayout);
        tfViews = new ArrayList<>();

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(tfViews.get(drawIndex).getCoordinates().y<mainLayout.getHeight()
                        && tfViews.get(drawIndex).getCoordinates().x<mainLayout.getWidth()){
                    tfViews.get(drawIndex).step();
                    tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(100).setListener(animatorListener);
                }
                else
                {
                    drawIndex++;
                    tfViews.get(drawIndex).step();
                    tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(100).setListener(animatorListener);
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
            tfViews.add(new TFView(this, new Point(startAreaView.getHeight() / 2, (int) event.getY())));
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
          //  for (drawIndex = 0; drawIndex < tfViews.size()-1; drawIndex++){
                tfViews.get(drawIndex).step();
                tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(100).setListener(animatorListener);
            //    }
        }
    }
}
