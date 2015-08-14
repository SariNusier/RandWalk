package com.randwalk.game.newgame.level2.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.randwalk.game.R;
import com.randwalk.game.newgame.level2.views.Level2aPathView;
import com.randwalk.game.newgame.level2.views.Level2cMainLayout;
import com.randwalk.game.newgame.level2.views.TFView;

import java.util.ArrayList;
import java.util.Random;

public class Level2cGameActivity extends Activity {
    int leftTFs = 0, rightTFs = 0;

    Level2cMainLayout mainLayoutTop, mainLayoutBot;
    ArrayList<TFView> tfViewsTop, tfViewsBot;
    View startAreaViewTop, startAreaViewBot, leftSideViewTop, rightSideViewTop, leftSideViewBot, rightSideViewBot,
    dnaMidTop, dnaMidBot, dnaLeftTop, dnaLeftBot, dnaRightTop, dnaRightBot;
    Level2aPathView pathViewTop, pathViewBot;
    Animator.AnimatorListener animatorListenerTop, animatorListenerBot;
    int w, h;
    int[] colors = {R.color.Bk0,R.color.Bk1,R.color.Bk2,R.color.Bk3,R.color.Bk4,R.color.Bk5,R.color.Bk6,R.color.Bk7,R.color.Bk8,R.color.Bk9};
    int tfSize, drawIndexTop = 0, drawIndexBot = 0;
    int finishCounterTop = 0, finishCounterBot = 0;
    boolean walking = false;
    ArrayList<View> mrnasTop, mrnasBot;
    float activeTFTop = 0, activeTFBot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2c_game);
        Intent i = getIntent();
        leftTFs = i.getIntExtra("left_TFs",0);
        rightTFs = i.getIntExtra("right_TFs",0);
        Log.d("Left:",""+leftTFs);
        tfSize =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        mainLayoutBot =(Level2cMainLayout) findViewById(R.id.level2c_mainlayout_bottom);
        mainLayoutTop =(Level2cMainLayout) findViewById(R.id.level2c_mainlayout_top);
        startAreaViewBot = findViewById(R.id.level2c_startarea_view_bottom);
        startAreaViewTop = findViewById(R.id.level2c_startarea_view_top);
        leftSideViewBot = findViewById(R.id.level2c_left_view_bottom);
        leftSideViewTop = findViewById(R.id.level2c_left_view_top);
        rightSideViewBot = findViewById(R.id.level2c_right_view_bottom);
        rightSideViewTop = findViewById(R.id.level2c_right_view_top);
        pathViewBot =(Level2aPathView) findViewById(R.id.level2c_path_view_bottom);
        pathViewTop =(Level2aPathView) findViewById(R.id.level2c_path_view_top);
        dnaMidBot = findViewById(R.id.level2c_dna_mid_view_bottom);
        dnaMidTop = findViewById(R.id.level2c_dna_mid_view_top);
        dnaLeftTop = findViewById(R.id.level2c_dna_left_view_top);
        dnaLeftBot = findViewById(R.id.level2c_dna_left_view_bottom);
        dnaRightTop = findViewById(R.id.level2c_dna_right_view_top);
        dnaRightBot = findViewById(R.id.level2c_dna_right_view_bottom);
        tfViewsBot = new ArrayList<>();
        tfViewsTop = new ArrayList<>();
        mrnasTop = new ArrayList<>();
        mrnasBot = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("level2CUnlocked",true);
        editor.commit();
         w = getWindowManager().getDefaultDisplay().getWidth();

        h = getWindowManager().getDefaultDisplay().getHeight();
        placeTFs();
        animatorListenerTop = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];

                dnaMidTop.getLocationOnScreen(location);
                float dna_X = location[0];
                float dna_Y = location[1];
                float dna_W = dnaMidTop.getWidth();
                float dna_H = dnaMidTop.getHeight();
                float tfView_X = tfViewsTop.get(drawIndexTop).getX()+tfViewsTop.get(drawIndexTop).getWidth()/2;
                float tfView_Y = tfViewsTop.get(drawIndexTop).getY()+tfViewsTop.get(drawIndexTop).getHeight();

                if(tfView_Y >= dna_Y){
                    if(tfView_X >= dna_X && tfView_X <= dna_W+dna_X)
                        insideActiveRegionTop();
                    else
                        outsideActiveRegionTop();
                    if(finishCounterTop == tfViewsTop.size())
                        finishWalkTop();
                    else{
                        tfViewsTop.get(drawIndexTop).step(leftSideViewTop,rightSideViewTop);
                        tfViewsTop.get(drawIndexTop).animate().x(tfViewsTop.get(drawIndexTop)
                                .getCoordinates().x).y(tfViewsTop.get(drawIndexTop).getCoordinates().y)
                                .setDuration(100).setListener(animatorListenerTop);
                        pathViewTop.drawLine(tfViewsTop.get(drawIndexTop).getPrevCoordinates(), tfViewsTop
                                .get(drawIndexTop).getCoordinates());
                        incrementDrawIndexTop();
                    }

                } else
                {
                    tfViewsTop.get(drawIndexTop).step(leftSideViewTop,rightSideViewTop);
                    tfViewsTop.get(drawIndexTop).animate().x(tfViewsTop.get(drawIndexTop).getCoordinates().x).y(tfViewsTop.get(drawIndexTop).getCoordinates().y).setDuration(100).setListener(animatorListenerTop);
                    pathViewTop.drawLine(tfViewsTop.get(drawIndexTop).getPrevCoordinates(),tfViewsTop.get(drawIndexTop).getCoordinates());
                    incrementDrawIndexTop();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };



        animatorListenerBot = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];

                dnaMidTop.getLocationOnScreen(location);
                float dna_X = location[0];
                float dna_Y = location[1];
                float dna_W = dnaMidTop.getWidth();
                float dna_H = dnaMidTop.getHeight();
                float tfView_X = tfViewsBot.get(drawIndexBot).getX()+tfViewsBot.get(drawIndexBot).getWidth()/2;
                float tfView_Y = tfViewsBot.get(drawIndexBot).getY()+tfViewsBot.get(drawIndexBot).getHeight();

                if(tfView_Y >= dna_Y){
                    if(tfView_X >= dna_X && tfView_X <= dna_W+dna_X)
                        insideActiveRegionBot();
                    else
                        outsideActiveRegionBot();
                    if(finishCounterBot == tfViewsBot.size())
                        finishWalkBot();
                    else{
                        tfViewsBot.get(drawIndexBot).step(leftSideViewBot,rightSideViewBot);
                        tfViewsBot.get(drawIndexBot).animate().x(tfViewsBot.get(drawIndexBot)
                                .getCoordinates().x).y(tfViewsBot.get(drawIndexBot).getCoordinates().y)
                                .setDuration(100).setListener(animatorListenerBot);
                        pathViewBot.drawLine(tfViewsBot.get(drawIndexBot).getPrevCoordinates(),tfViewsBot
                                .get(drawIndexBot).getCoordinates());
                        incrementDrawIndexBot();
                    }

                } else
                {
                    tfViewsBot.get(drawIndexBot).step(leftSideViewBot,rightSideViewBot);
                    tfViewsBot.get(drawIndexBot).animate().x(tfViewsBot.get(drawIndexBot).getCoordinates().x).y(tfViewsBot.get(drawIndexBot).getCoordinates().y).setDuration(100).setListener(animatorListenerBot);
                    pathViewBot.drawLine(tfViewsBot.get(drawIndexBot).getPrevCoordinates(),tfViewsBot.get(drawIndexBot).getCoordinates());
                    incrementDrawIndexBot();
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

    public void startWalkTop(){
        if(!walking && leftTFs > 0) {
            drawIndexTop = 0;
            tfViewsTop.get(drawIndexTop).step(leftSideViewTop,rightSideViewTop);
            tfViewsTop.get(drawIndexTop).animate().x(tfViewsTop.get(drawIndexTop).getCoordinates().x).y(tfViewsTop.get(drawIndexTop).getCoordinates().y).setDuration(100).setListener(animatorListenerTop);
            pathViewTop.drawLine(tfViewsTop.get(drawIndexTop).getPrevCoordinates(), tfViewsTop.get(drawIndexTop).getCoordinates());
            incrementDrawIndexTop();
            }
        }

    public void startWalkBot(){
        if(!walking && rightTFs > 0) {
            walking = true;
            drawIndexBot = 0;
            tfViewsBot.get(drawIndexBot).step(leftSideViewBot,rightSideViewBot);
            tfViewsBot.get(drawIndexBot).animate().x(tfViewsBot.get(drawIndexBot).getCoordinates().x).y(tfViewsBot.get(drawIndexBot).getCoordinates().y).setDuration(100).setListener(animatorListenerBot);
            pathViewBot.drawLine(tfViewsBot.get(drawIndexBot).getPrevCoordinates(), tfViewsBot.get(drawIndexBot).getCoordinates());
            incrementDrawIndexBot();
        }
    }

    public void finishWalkTop(){
        mainLayoutTop.setBackgroundColor(getResources().getColor(colors[mrnasTop.size()]));
    }

    public void finishWalkBot(){
        mainLayoutBot.setBackgroundColor(getResources().getColor(colors[mrnasBot.size()]));
    }


    public void placeTFs(){
        if(leftTFs % 2 == 0){
            for(int i = 0 ; i<leftTFs; i++){
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 + tfSize/4;
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                } else if(i == 1){
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 - (tfSize + tfSize/4);
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    if(i % 2 == 0)
                        params.leftMargin = tfViewsTop.get(i-2).getCoordinates().x + (tfSize + tfSize/2);
                    else
                        params.leftMargin = tfViewsTop.get(i-2).getCoordinates().x - (tfSize + tfSize/2);
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                }
                mainLayoutTop.invalidate();
            }
        } else {
            for(int i = 0 ; i<leftTFs; i++){
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 - tfSize/2;
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                } else if(i == 1){
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin =tfViewsTop.get(0).getCoordinates().x + tfSize + tfSize/2;
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    if(i % 2 == 0)
                        params.leftMargin = tfViewsTop.get(i-2).getCoordinates().x - (tfSize + tfSize/2);
                    else
                        params.leftMargin = tfViewsTop.get(i-2).getCoordinates().x + (tfSize + tfSize/2);
                    params.topMargin = startAreaViewTop.getHeight() / 2 + tfSize/2;
                    tfViewsTop.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutTop.addView(tfViewsTop.get(tfViewsTop.size() - 1), params);
                }
                mainLayoutTop.invalidate();
            }
        }
        if(rightTFs % 2 == 0){
            for(int i = 0 ; i<rightTFs; i++){
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 + tfSize/4;
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                } else if(i == 1){
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 - (tfSize + tfSize/4);
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    if(i % 2 == 0)
                        params.leftMargin = tfViewsBot.get(i-2).getCoordinates().x + (tfSize + tfSize/2);
                    else
                        params.leftMargin = tfViewsBot.get(i-2).getCoordinates().x - (tfSize + tfSize/2);
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                }
                mainLayoutTop.invalidate();
            }
        } else {
            for(int i = 0 ; i<rightTFs; i++){
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin = w/2 - tfSize/2;
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                } else if(i == 1){
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    params.leftMargin =tfViewsBot.get(0).getCoordinates().x + tfSize + tfSize/2;
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
                    if(i % 2 == 0)
                        params.leftMargin = tfViewsBot.get(i-2).getCoordinates().x - (tfSize + tfSize/2);
                    else
                        params.leftMargin = tfViewsBot.get(i-2).getCoordinates().x + (tfSize + tfSize/2);
                    params.topMargin = startAreaViewBot.getHeight() / 2 + tfSize/2;
                    tfViewsBot.add(new TFView(this, new Point(params.leftMargin,params.topMargin)));
                    mainLayoutBot.addView(tfViewsBot.get(tfViewsBot.size() - 1), params);
                }
                mainLayoutBot.invalidate();
            }
        }
        //startWalkTop();
        //startWalkBot();
    }

    public void insideActiveRegionTop(){
        tfViewsTop.get(drawIndexTop).finished = true;
        finishCounterTop++;
        activeTFTop++;
        float probability = activeTFTop/(activeTFTop +3);
        if(Math.random()<=probability){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize+tfSize/2,tfSize);
            Random rand = new Random();
            mrnasTop.add(new View(this));
            mrnasTop.get(mrnasTop.size()-1).setBackground(this.getResources().getDrawable(R.drawable.cell3));
            if(tfViewsTop.get(drawIndexTop).getCoordinates().x <= mainLayoutTop.getWidth()/2){
                params.leftMargin = rand.nextInt(dnaLeftTop.getWidth()-(tfSize+tfSize/2)-1) + 1;
                params.topMargin = rand.nextInt(mainLayoutTop.getHeight()-(tfSize) - (mainLayoutTop.getHeight() - dnaLeftTop.getHeight())) + (mainLayoutTop.getHeight() - dnaLeftTop.getHeight());
            }else {
                params.leftMargin = rand.nextInt(mainLayoutTop.getWidth()-(tfSize+tfSize/2) - (mainLayoutTop.getWidth() - dnaRightTop.getWidth())) + (mainLayoutTop.getWidth() - dnaRightTop.getWidth());
                params.topMargin = rand.nextInt(mainLayoutTop.getHeight()-(tfSize) - (mainLayoutTop.getHeight() - dnaLeftTop.getHeight())) + (mainLayoutTop.getHeight() - dnaLeftTop.getHeight());
            }

            mainLayoutTop.addView(mrnasTop.get(mrnasTop.size()-1), params);
        }
        tfViewsTop.get(drawIndexTop).setBackground(this.getResources().getDrawable(R.drawable.cell2));
    }

    public void insideActiveRegionBot(){
        tfViewsBot.get(drawIndexBot).finished = true;
        finishCounterBot++;
        activeTFBot++;
        float probability = activeTFBot/(activeTFBot +3);
        if(Math.random()<=probability){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize/2 + tfSize,tfSize);
            Random rand = new Random();
            mrnasBot.add(new View(this));
            mrnasBot.get(mrnasBot.size()-1).setBackground(this.getResources().getDrawable(R.drawable.cell3));
            if(tfViewsBot.get(drawIndexBot).getCoordinates().x <= mainLayoutBot.getWidth()/2){
                params.leftMargin = rand.nextInt(dnaLeftBot.getWidth()-(tfSize+tfSize/2)-1) + 1;
                params.topMargin = rand.nextInt(mainLayoutBot.getHeight() - (tfSize) - (mainLayoutBot.getHeight() - dnaLeftBot.getHeight())) + (mainLayoutBot.getHeight() - dnaLeftBot.getHeight());
            }else {
                params.leftMargin = rand.nextInt(mainLayoutBot.getWidth() - (tfSize+tfSize/2) - (mainLayoutBot.getWidth() - dnaRightBot.getWidth())) + (mainLayoutBot.getWidth() - dnaRightBot.getWidth());
                params.topMargin = rand.nextInt(mainLayoutBot.getHeight() - (tfSize) - (mainLayoutBot.getHeight() - dnaLeftBot.getHeight())) + (mainLayoutBot.getHeight() - dnaLeftBot.getHeight());
            }

            mainLayoutBot.addView(mrnasBot.get(mrnasBot.size()-1), params);
        }
        tfViewsBot.get(drawIndexBot).setBackground(this.getResources().getDrawable(R.drawable.cell2));
    }

    public void outsideActiveRegionTop(){
        tfViewsTop.get(drawIndexTop).finished = true;
        finishCounterTop++;
    }
    public void outsideActiveRegionBot(){
        tfViewsBot.get(drawIndexBot).finished = true;
        finishCounterBot++;
    }

    public void showMithocondria(View v){
       // guideText.setText("Mithocondria");
       // showGuide(v);
    }
    public void showLysosomes(View v){
    //    guideText.setText("Lysosomes");
      //  showGuide(v);
    }
    public void showVacuole(View v){
        //guideText.setText("Vacuole");
        //showGuide(v);
    }
    public void showEndopl(View v){
      //  guideText.setText("Endoplasmic reticulum");
    //    showGuide(v);
    }
    public void showPerixosome(View v){
        //guideText.setText("Perixosome");
       // showGuide(v);
    }
    public void showGolgi(View v){
       // guideText.setText("Golgi apparatus");
       // showGuide(v);
    }

    public void incrementDrawIndexTop() {
            if (drawIndexTop == tfViewsTop.size() - 1) {
                drawIndexTop = 0;
            } else drawIndexTop++;
            if(tfViewsTop.get(drawIndexTop).finished){
                incrementDrawIndexTop();
            }
    }

    public void incrementDrawIndexBot() {

        if (drawIndexBot == tfViewsBot.size() - 1) {
            drawIndexBot = 0;
        } else drawIndexBot++;
        if(tfViewsBot.get(drawIndexBot).finished){
            incrementDrawIndexBot();
        }
    }

    public void nextIntro(View v){
        findViewById(R.id.level2c_intro_layout).setVisibility(View.GONE);
    }

}
