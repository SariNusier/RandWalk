package com.example.sari.randomwalk;

import android.app.FragmentManager;
import android.content.Intent;
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
        if(id == R.id.action_about)
           showAboutDialog();
        if(id == R.id.action_settings)
            showSettings();
        return super.onOptionsItemSelected(item);
    }

    public void showSettings(){

    }
    public void showAboutDialog(){
        FragmentManager manager = getFragmentManager();
        MoreDialog dialog = new MoreDialog();
        dialog.show(manager,"Learn More");
    }

    public void showScore1(View v){
        FragmentManager manager = getFragmentManager();
        ScoreDialog1 dialog1 = new ScoreDialog1();
        dialog1.show(manager,"MyDialog");
    }
    public void showScore2(View v){
        FragmentManager manager = getFragmentManager();
        ScoreDialog2 dialog2 = new ScoreDialog2();
        dialog2.show(manager,"MyDialog");
    }

    public void startLevel1(View view){
       Button buttonClicked = (Button) view;
        Intent intent = new Intent(this, Level1Activity.class);
        intent.putExtra("SUB_LEVEL", buttonClicked.getText());
        startActivity(intent);
    }

    public void startLevel2(View view){
        Button buttonClicked = (Button) view;
        Intent intent = new Intent(this, Level2Activity.class);
        intent.putExtra("SUB_LEVEL",buttonClicked.getText());
        startActivity(intent);
    }

}
