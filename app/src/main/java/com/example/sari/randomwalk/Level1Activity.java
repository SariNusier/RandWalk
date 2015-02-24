package com.example.sari.randomwalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Level1Activity extends ActionBarActivity {

    static TextView textView, textViewIntro;
    static ImageView imageView;
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
       // imageView = (ImageView) findViewById(R.id.level1a_tutorial_imageView);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        textViewIntro.setTypeface(typeface);
        clickCounter = 0;
        updateScore();

        if(subLevel.equals("B"))
        {
            // Drawable d = getResources().getDrawable(R.drawable.level1b_tutorial);
//            imageView.setBackground(d);
            textViewIntro.setText(R.string.intro_level1B);
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

    @Override
    protected void onPause(){
        super.onPause();
        //sounds.cancel(true);
    }

    /**
     * This method hides the guide page and reveals the game view behind it.
     */
    public void nextPage(View view){

        View layout = findViewById(R.id.level1_guide_layout);
        View scrollView = findViewById(R.id.level1_guide_scrollView);


        if(clickCounter == 0){
            scrollView.setVisibility(View.GONE);
            //layout.setBackgroundColor(Color.WHITE);
            if(subLevel.equals("A"))
                layout.setBackground(getResources().getDrawable(R.drawable.level1a_tutorial));
            else
                layout.setBackground(getResources().getDrawable(R.drawable.level1b_tutorial));
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
    public void updateScore(){
        textView.setText("Score: " + preferences.getInt(String.format("score_1%s",subLevel), 0));
        final int oldColor = textView.getCurrentTextColor();

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            textView.setTextColor(Color.GREEN);
                        }
                    });
                    sleep(500);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            textView.setTextColor(oldColor);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static String getSubLevel(){
        return subLevel;
    }

    @Override
    protected void onStop() {
        super.onStop();
        GameView v = (GameView)this.findViewById(R.id.gameView1);
        v.initialBitmap.recycle();
        v.playingBitmap.recycle();
        v.destroyDrawingCache();
        System.gc();

    }
    public void endLevel(){
        GameView v = (GameView)this.findViewById(R.id.gameView1);
        View v1 = findViewById(R.id.level1_mainlayout);
        v.initialBitmap.recycle();
        v.playingBitmap.recycle();
        v.destroyDrawingCache();
        System.gc();
        v1.setVisibility(View.GONE);
    }
    public void goNextLevel(View view){

        Intent intent = new Intent(this, Level1Activity.class);
        intent.putExtra("SUB_LEVEL","B");
        startActivity(intent);
        finish();
    }
}
