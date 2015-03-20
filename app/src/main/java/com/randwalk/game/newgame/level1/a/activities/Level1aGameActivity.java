package com.randwalk.game.newgame.level1.a.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.randwalk.game.newgame.*;
import com.randwalk.game.R;
import com.randwalk.game.newgame.level1.a.views.Level1aMainLayout;
import com.randwalk.game.newgame.level1.views.PirateView;

public class Level1aGameActivity extends ActionBarActivity {
    Level1aMainLayout mainLayout;
    View startAreaView;
    View pirateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1a_game);
        mainLayout = (Level1aMainLayout) findViewById(R.id.level1a_mainlayout);
        startAreaView = findViewById(R.id.level1a_startarea_view);
        pirateView = findViewById(R.id.level1a_pirate_view);
    }

    public void placePirate(MotionEvent event) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pirateView.getLayoutParams();

        params.topMargin = Math.round(event.getY());
        params.leftMargin = (startAreaView.getWidth()-pirateView.getWidth())/2;
        pirateView.setLayoutParams(params);
        pirateView.setVisibility(View.VISIBLE);
    }
}
