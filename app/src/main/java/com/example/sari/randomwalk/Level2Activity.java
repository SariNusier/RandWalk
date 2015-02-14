package com.example.sari.randomwalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


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
