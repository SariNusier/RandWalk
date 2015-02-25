package com.randwalk.game.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.randwalk.game.GameViews.GameView;
import com.randwalk.game.R;

import java.util.Random;

public class Level1Activity extends ActionBarActivity {

    static TextView textView, textViewIntro;
    static ImageView imageView;
    static SharedPreferences preferences;
    static String subLevel;
    int clickCounter;
    boolean levelEndend = false;
    String[] outText = {"Sink me!","Oups, pirate went too far, the crew returned him to the pub.",
            "Arrrgh!!!","That Clap of Thunder killed me!"};
    String[] missText = {"Almost there!","Splash! \"Grrr, I'll swim to the boat!\"","The crew dragged you home!","Mermaids rescued you!"};
    String[] homeText = {"This choice seems to work - I must try again!","Yay - let me help my friends as well!","Lucky you! Chances of coming home were not that high!"};
    String[] restartText = {"Maybe it's the time to change the bar?","Walk the plank if you'd like to visit bar again!","Ahoy! Try again!"};
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
        updateScore("nothing");

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
    public void updateScore(String whatHappened){

        int rand;
        if(whatHappened.equals("o")){
            Random r = new Random();
            rand = r.nextInt(3);

            textView.setText("Score: " + preferences.getInt(String.format("score_1%s",subLevel), 0)+" - "+outText[rand]);
        } else
        if(whatHappened.equals("m")){
            Random r = new Random();
            rand = r.nextInt(3);
            textView.setText("Score: " + preferences.getInt(String.format("score_1%s",subLevel), 0)+" - "+missText[rand]);
        } else
        if(whatHappened.equals("h")){
            Random r = new Random();
            rand = r.nextInt(2);
            textView.setText("Score: " + preferences.getInt(String.format("score_1%s",subLevel), 0)+" - "+homeText[rand]);
        } else
        if(whatHappened.equals("r")){
            Random r = new Random();
            rand = r.nextInt(2);
            textView.setText("Score: " + preferences.getInt(String.format("score_1%s",subLevel), 0)+" - "+restartText[rand]);
        }else
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
        View v2 = findViewById(R.id.level1_layout_final);
        v.initialBitmap.recycle();
        v.playingBitmap.recycle();
        v.destroyDrawingCache();
        System.gc();
        if(subLevel.equals("B"))
            v2.setBackground(getResources().getDrawable(R.drawable.level1b_final));
        v1.setVisibility(View.GONE);
    }
    public void goNextLevel(View view){
        if(subLevel.equals("A")) {
            Intent intent = new Intent(this, Level1Activity.class);
            intent.putExtra("SUB_LEVEL", "B");
            startActivity(intent);
        }
        finish();
    }
}
