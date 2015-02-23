package com.example.sari.randomwalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Level2Animation extends Activity {
    AnimationDrawable rocketAnimation;
    int clickCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_animation2);
        DisplayMetrics metrics = new DisplayMetrics();
        final View animation = findViewById(R.id.animation_view);
        final View animationLayout = findViewById(R.id.animation_view_layout);
        //rocketImage.setBackgroundResource(R.drawable.animation);
        clickCounter = 0;
        animation.setRight(metrics.widthPixels);
        animation.setBottom(metrics.heightPixels);
        rocketAnimation = (AnimationDrawable) animation.getBackground();
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        TextView v =(TextView) findViewById(R.id.textView_feather);
        v.setTypeface(tf,Typeface.BOLD);
        rocketAnimation.start();
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2650);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            animation.setVisibility(View.GONE);
                            animationLayout.setVisibility(View.GONE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
    public void startLevel2Activity(View view){
        Intent intent = new Intent(this, Level2Activity.class);
        startActivity(intent);
        this.finish();
    }

    public void onButtonClick(View v){
        View animationView = findViewById(R.id.textView_feather_layout);
        if(clickCounter == 0){
            clickCounter++;
            animationView.setVisibility(View.GONE);
        }
        else
        {
            startLevel2Activity(v);
            clickCounter--;
        }
    }


}
