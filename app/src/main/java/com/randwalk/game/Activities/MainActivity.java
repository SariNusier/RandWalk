package com.randwalk.game.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.randwalk.game.Dialogs.DevelopmentDialog;
import com.randwalk.game.Dialogs.LearnMoreDialog;
import com.randwalk.game.Dialogs.LockedDialog;
import com.randwalk.game.R;
import com.randwalk.game.Dialogs.ScoreLevel1Dialog;
import com.randwalk.game.Dialogs.ScoreLevel2Dialog;
import com.randwalk.game.newgame.level1.a.activities.Level1aGameActivity;


public class MainActivity extends ActionBarActivity {

    /**
     * onCreate method for Main Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getInt("score_1A",0) <= 1000){
            editor.putBoolean("level1BUnlocked",false);
        }
        else
            editor.putBoolean("level1BUnlocked",true);

        editor.commit();


    }

    /**
     * onCreateOptionsMenu method for Main Activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method called when an item from the menu is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idItemSelected = item.getItemId(); //gets the id of the item that was selected

        if(idItemSelected == R.id.action_learn_more)
            showLearnMoreDialog(); //calls the method that shows the dialog with the "about" text
        if(idItemSelected == R.id.action_settings)
            startSettingsActivity(); //calls the method that shows the settings menu (activity)

        return super.onOptionsItemSelected(item);
    }


    /**
     * Method will show the Learn More Dialog
     */
    public void showLearnMoreDialog(){
        FragmentManager manager = getFragmentManager();
        LearnMoreDialog dialog = new LearnMoreDialog();
        dialog.show(manager,"Learn More");
    }

    /**
     * Shows In Development dialog.
     */
    public void showDevelopmentDialog(View v){
        FragmentManager manager = getFragmentManager();
        DevelopmentDialog dialog = new DevelopmentDialog();
        dialog.show(manager,"In Development");
    }

    /**
     * Shows the dialog with the scores for Level 1.
     */
    public void showScoreLevel1Dialog(View v){
        FragmentManager manager = getFragmentManager();
        ScoreLevel1Dialog dialog = new ScoreLevel1Dialog();
        dialog.show(manager,"Level 1 Score");
    }

    /**
     * Shows the dialog with the scores for Level 2.
     */
    public void showScoreLevel2Dialog(View v){
        FragmentManager manager = getFragmentManager();
        ScoreLevel2Dialog dialog = new ScoreLevel2Dialog();
        dialog.show(manager,"Level 2 Score");
    }


    /**
     * Method will start the Settings Activity
     */
    public void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Method that starts Level 1 Activity.
     */
    public void startLevel1Activity(View view){
        Button button = (Button) view;
        if(button.getText().equals("A") ||(button.getText().equals("B") && getSharedPreferences("GAME_DATA", MODE_PRIVATE).getBoolean("level1BUnlocked", false))) {
            Intent intent = new Intent(this, Level1Activity.class);
            intent.putExtra("SUB_LEVEL", button.getText()); //extracts the sublevel from the button that was pressed and sends it to the Level 1 Activity.
            startActivity(intent);
        }
        else
        {
            FragmentManager manager = getFragmentManager();
            LockedDialog dialog = new LockedDialog();
            dialog.show(manager,"Level 1B Locked");
        }
    }

    /**
     * Method that starts Level 2 Activity.
     */
    public void startLevel2Activity(View view){
        Button button = (Button) view;
        Intent intent = new Intent(this, Level2Activity.class);
        intent.putExtra("SUB_LEVEL",button.getText()); //extracts the sublevel from the button that was pressed and sends it to the Level 2 Activity.
        startActivity(intent);
    }

    public void startLevel2BActivity(View view){
        startActivity(new Intent(this,Level2BActivity.class));
    }

    public void startAnimation(View view){
        Intent intent = new Intent(this, Level2Animation.class);
        startActivity(intent);
    }

    public void startLevel1ActivityTest(View view){
        startActivity(new Intent(this, Level1aGameActivity.class));
    }

}