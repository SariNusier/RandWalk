package com.randwalk.game.newgame.level2.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.randwalk.game.R;
import com.randwalk.game.newgame.level2.views.Level2cMainLayout;
import com.randwalk.game.newgame.level2.views.TFView;

import java.util.ArrayList;

public class Level2cGameActivity extends Activity {
    int leftTFs = 0, rightTFs = 0;

    Level2cMainLayout mainLayoutTop, mainLayoutBot;
    ArrayList<TFView> tfViewsTop, tfViewsBot;
    View startAreaViewTop, startAreaViewBot;
    int w, h;
    int tfSize;

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
        tfViewsBot = new ArrayList<>();
        tfViewsTop = new ArrayList<>();
         w = getWindowManager().getDefaultDisplay().getWidth();

        h = getWindowManager().getDefaultDisplay().getHeight();
        placeTFs();


    }

    public void startWalk(){

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
}
