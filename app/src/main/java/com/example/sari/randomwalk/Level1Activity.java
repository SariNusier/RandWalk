package com.example.sari.randomwalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Level1Activity extends ActionBarActivity {

    static TextView textView, textViewIntro;
    static SharedPreferences preferences;
    static String subLevel;
    int clickCounter;

    /**
     * onCreate method for Level1Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        subLevel = intent.getStringExtra("SUB_LEVEL"); //gets the sub-level selected from main activity
        setContentView(R.layout.activity_level1); //sets layout activity_level1
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        setTitle("Level 1" + subLevel); //sets appropriate title based on sub-level
        getSupportActionBar().setCustomView(R.layout.actionbar_custom);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        preferences = getSharedPreferences("GAME_DATA", MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.scoreText);
        textViewIntro = (TextView) findViewById(R.id.textView_intro_level1A);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        textViewIntro.setTypeface(typeface);
        clickCounter = 0;
        updateScore();
        if(subLevel.equals("B"))
        {

        }
    }

    /**
     * onCreateOptionsMenu for Level1Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_level1, menu);    // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    /**
     * Handles action bar item clicks.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method hides the guide page and reveals the game view behind it.
     */
    public void nextPage(View view){

        View layout = findViewById(R.id.level1_guide_layout);
        View scrollView = findViewById(R.id.level1_guide_scrollView);


        if(clickCounter == 0){
            scrollView.setVisibility(View.GONE);
            layout.setBackgroundColor(Color.WHITE);
            clickCounter++;
        }
        else
            if(clickCounter == 1){
                layout.setVisibility(View.GONE);
            }


    }

    /**
     * Refreshes the score's text view, displaying most recent score.
     */
    public static void updateScore(){
        textView.setText("Score: " + preferences.getInt("score", 0));
    }

    public static String getSubLevel(){
        return subLevel;
    }

}
