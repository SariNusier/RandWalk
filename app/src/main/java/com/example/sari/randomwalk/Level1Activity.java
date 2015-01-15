package com.example.sari.randomwalk;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class Level1Activity extends ActionBarActivity {
    private int clickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);
        Intent intent = getIntent();
        setTitle(getTitle()+" "+intent.getStringExtra("SUB_LEVEL"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_level1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextPage(View view){

        TextView textView = (TextView) findViewById(R.id.textView_intro_level1A);
        View layout = (View) findViewById(R.id.guide_layout);
        if(clickCount == 0) {
            textView.setText(R.string.guide_level1A);
            clickCount++;
        } else {
            layout.setVisibility(View.GONE);
            clickCount = 0;
        }
    }
}
