package com.example.sari.randomwalk;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast toast;
        switch (item.getItemId()) {
            case R.id.action_about:
                toast = Toast.makeText(getApplicationContext(),"About",Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.action_help:
                Toast.makeText(getApplicationContext(),"Help",Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        //noinspection SimplifiableIfStatement

        }
    }

    public void showScore1(View v){
        FragmentManager manager = getFragmentManager();
        ScoreDialog1 dialog1 = new ScoreDialog1();
        dialog1.show(manager,"MyDialog");
    }
    public void showScore2(View v){

    }

}
