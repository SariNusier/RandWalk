package com.example.sari.randomwalk;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class Level2Animation extends ActionBarActivity {
    AnimationDrawable rocketAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_animation2);

        ImageView rocketImage = (ImageView) findViewById(R.id.animation_view);
        rocketImage.setBackgroundResource(R.drawable.animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();
    }



}
