package com.randomwalk.game.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.randomwalk.game.Dialogs.AboutUsDialog;
import com.randomwalk.game.Dialogs.DevelopmentDialog;
import com.randomwalk.game.Dialogs.LearnMoreDialog;
import com.randomwalk.game.Dialogs.LockedDialog;
import com.randomwalk.game.Dialogs.LockedDialog2C;
import com.randomwalk.game.Dialogs.LockedDialogC;
import com.randomwalk.game.R;
import com.randomwalk.game.Dialogs.ScoreLevel1Dialog;
import com.randomwalk.game.Dialogs.ScoreLevel2Dialog;
import com.randomwalk.game.newgame.level1.a.activities.Level1aGameActivity;
import com.randomwalk.game.newgame.level1.b.activities.Level1bGameActivity;
import com.randomwalk.game.newgame.level1.c.activities.Level1cGameActivity;
import com.randomwalk.game.newgame.level2.activities.Level2aGameActivity;
import com.randomwalk.game.newgame.level2.activities.Level2cGameActivity;

import java.util.Random;


public class MainActivity extends ActionBarActivity {
    SharedPreferences preferences;
    /**
     * onCreate method for Main Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getInt("score_1A",0) <= 10){
            editor.putBoolean("level1BUnlocked",false);
        }
        else
            editor.putBoolean("level1BUnlocked",true);

        if(preferences.getInt("score_1B",0) <=10){
            editor.putBoolean("level1CUnlocked",false);
        } else
            editor.putBoolean("level1CUnlocked",true);

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
        if(idItemSelected == R.id.action_about_us)
            showAboutUsDialog();
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

    public void showAboutUsDialog(){
        FragmentManager manager = getFragmentManager();
        AboutUsDialog dialog = new AboutUsDialog();
        dialog.show(manager,"About Us");
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

    public void startLevel1aActivityTest(View view){
        startActivity(new Intent(this, Level1aGameActivity.class));
    }

    public void startLevel1bActivityTest(View view){
        if(preferences.getBoolean("level1BUnlocked",false))
        {
            startActivity(new Intent(this, Level1bGameActivity.class));
        } else {
            FragmentManager manager = getFragmentManager();
            LockedDialog dialog = new LockedDialog();
            dialog.show(manager, "Level 1B Locked");
        }
    }

    public void startLevel2aActivityTest(View view){
        Button b = (Button) view;
        Intent intent = new Intent(this, Level2aGameActivity.class);
        intent.putExtra("SUB_LEVEL",b.getText());
        startActivity(intent);
    }

    public void startLevel2cActivity(View view){
        if(preferences.getBoolean("level2CUnlocked",false)){
            Intent intent = new Intent(this, Level2cGameActivity.class);
            Random r = new Random();
            intent.putExtra("left_TFs",r.nextInt(4)+1);
            intent.putExtra("right_TFs",r.nextInt(4)+1);
            startActivity(intent);
        }
        else
        {
            FragmentManager manager = getFragmentManager();
            LockedDialog2C dialog = new LockedDialog2C();
            dialog.show(manager, "Level 2C Locked");
        }
    }

    public void startLevel1cActivity(View view){
        if(preferences.getBoolean("level1CUnlocked",false))
       {
            startActivity(new Intent(this, Level1cGameActivity.class));
        } else {
           FragmentManager manager = getFragmentManager();
            LockedDialogC dialog = new LockedDialogC();
            dialog.show(manager, "Level 1C Locked");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }
}
