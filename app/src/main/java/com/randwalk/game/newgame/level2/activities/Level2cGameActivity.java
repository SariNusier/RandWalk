package com.randwalk.game.newgame.level2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.randwalk.game.R;

public class Level2cGameActivity extends Activity {
    int leftTFs = 0, rightTFs = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2c_game);
        Intent i = getIntent();
        leftTFs = i.getIntExtra("left_TFs",0);
        rightTFs = i.getIntExtra("right_TFs",0);
        Log.d("Left:", ""+leftTFs);
        Log.d("Right:", ""+rightTFs);
    }

}
