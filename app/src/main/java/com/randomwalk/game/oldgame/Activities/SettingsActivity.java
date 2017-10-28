package com.randomwalk.game.Activities;

import android.app.Activity;
import android.os.Bundle;


public class SettingsActivity extends Activity {

    /**
     * onCreate for SettingsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }



}
