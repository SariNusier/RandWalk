package com.randomwalk.game.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.randomwalk.game.R;


public class Level2Activity extends Activity {

    static String subLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);
        Intent intent = getIntent();
        setTitle("Level 2" + intent.getStringExtra("SUB_LEVEL"));
    }



    public void updateScore(){

    }

    public static String getSubLevel(){
        return "A";
    }

}
