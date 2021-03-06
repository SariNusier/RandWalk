package com.randomwalk.game.newgame.level2.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.randomwalk.game.R;
import com.randomwalk.game.newgame.level2.views.Level2aPathView;
import com.randomwalk.game.newgame.level2.views.TFView;
import java.util.ArrayList;
import java.util.Random;

public class Level2aGameActivity extends Activity {

    View dnaLeft,dnaMid,dnaRight;
    View startAreaView;
    View rightSideView, leftSideView;
    TextView guideText, endGuideText, textViewIntro;
    Level2aPathView pathView;
    RelativeLayout mainLayout;
    ProgressBar pb;
    Animator.AnimatorListener animatorListener, guideAnimationListener;
    int drawIndex , animationDuration;
    float activeTF = 0, mRnaC = 0;
    ArrayList<TFView> tfViews;
    ArrayList<View> mrnas;
    int[] colors = {R.color.Bk0,R.color.Bk1,R.color.Bk2,R.color.Bk3,R.color.Bk4,R.color.Bk5,R.color.Bk6,R.color.Bk7,R.color.Bk8,R.color.Bk9};
    String subLevel;
    boolean walking = false;
    boolean toRestart = true;
    boolean allowedToSplit = false;
    boolean showCloseToDivision = true;
    int tfSize, finishedCounter;
    Toast toast;
    TextView popIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        Intent i = getIntent();
        subLevel = i.getStringExtra("SUB_LEVEL");
        setContentView(R.layout.activity_level2a_game);
        dnaLeft = findViewById(R.id.level2a_dna_left_view);
        dnaMid = findViewById(R.id.level2a_dna_mid_view);
        dnaRight = findViewById(R.id.level2a_dna_right_view);
        startAreaView = findViewById(R.id.level2a_startarea_view);
        mainLayout = (RelativeLayout) findViewById(R.id.level2a_mainlayout);
        rightSideView = findViewById(R.id.level2a_right_view);
        leftSideView = findViewById(R.id.level2a_left_view);
        pathView = (Level2aPathView) findViewById(R.id.level2a_path_view);
        guideText = (TextView) findViewById(R.id.level2a_guide_textview);
        endGuideText = (TextView) findViewById(R.id.level2a_end_guide_textview);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/goudy.ttf");
        textViewIntro = (TextView) findViewById(R.id.level2a_intro_textview);
        pb = (ProgressBar) findViewById(R.id.level2a_progressbar);
        textViewIntro.setTypeface(typeface);
        endGuideText.setVisibility(View.INVISIBLE);

        LayoutInflater mInflater = getLayoutInflater();
        View mLayout = mInflater.inflate(R.layout.level2_toast, (ViewGroup) findViewById(R.id.level2_toast));
        popIn = (TextView) mLayout.findViewById(R.id.toast_text_2);
        toast = new Toast(getApplicationContext());
        toast.setView(mLayout);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        tfSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        if(subLevel.equals("A"))
            animationDuration = 1;
        else{
            animationDuration = 100;
            endGuideText.setTypeface(null, Typeface.BOLD);
            textViewIntro.setText(getResources().getString(R.string.level2B_text));
        }



        guideText.setAlpha(0);
        tfViews = new ArrayList<>();
        mrnas = new ArrayList<>();

        animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int[] location = new int[2];

                dnaMid.getLocationOnScreen(location);
                float dna_X = location[0];
                float dna_Y = location[1];
                float dna_W = dnaMid.getWidth();
                float dna_H = dnaMid.getHeight();
                float tfView_X = tfViews.get(drawIndex).getX()+tfViews.get(drawIndex).getWidth()/2;
                float tfView_Y = tfViews.get(drawIndex).getY()+tfViews.get(drawIndex).getHeight()/2;

                if(tfView_Y >= dna_Y + dna_H/2){
                    if(tfView_X >= dna_X && tfView_X <= dna_W+dna_X)
                        insideActiveRegion();
                    else
                        outsideActiveRegion();
                    if(drawIndex == tfViews.size()-1)
                        finishWalk();
                    else{
                        if(subLevel.equals("A"))
                            drawIndex++;
                        tfViews.get(drawIndex).step(leftSideView,rightSideView);
                        tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(animationDuration).setListener(animatorListener);
                        pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(),tfViews.get(drawIndex).getCoordinates());
                        incrementDrawIndex();
                    }

                } else
                {
                    tfViews.get(drawIndex).step(leftSideView,rightSideView);
                    tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(animationDuration).setListener(animatorListener);
                    pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(),tfViews.get(drawIndex).getCoordinates());
                    incrementDrawIndex();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        guideAnimationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                guideText.animate().alpha(0).setDuration(2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };




    }

    public void placeTF(MotionEvent event){
        if(toRestart){
            restart();
            toRestart = false;
        }
        if(event.getY() <= startAreaView.getHeight() && event.getX() > leftSideView.getWidth() && event.getX() < rightSideView.getX()
                && tfViews.size()<8 && !walking) {
            tfViews.add(new TFView(this, new Point((int) event.getX(), startAreaView.getHeight() / 2)));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize, tfSize);
            params.leftMargin = Math.round(event.getX());
            params.topMargin = startAreaView.getHeight() / 2;
            mainLayout.addView(tfViews.get(tfViews.size() - 1), params);
        }
    }

    public void startWalk(){
        if(tfViews.size()>=5 && !walking) {
            walking = true;
            drawIndex = 0;
            tfViews.get(drawIndex).step(leftSideView,rightSideView);
            tfViews.get(drawIndex).animate().x(tfViews.get(drawIndex).getCoordinates().x).y(tfViews.get(drawIndex).getCoordinates().y).setDuration(animationDuration).setListener(animatorListener);
            pathView.drawLine(tfViews.get(drawIndex).getPrevCoordinates(), tfViews.get(drawIndex).getCoordinates());
            if(subLevel.equals("B")) {
                incrementDrawIndex();
                findViewById(R.id.level2_line_view).setVisibility(View.VISIBLE);
                pb.setVisibility(View.VISIBLE);
                showEndGuide("When the time is over, swipe on the dashed line to divide the cell.");
                new CountDownTimer(10000, 100) {

                    public void onTick(long millisUntilFinished) {
                        pb.setProgress(100 - (int)(millisUntilFinished/100));
                        if(millisUntilFinished<5000){
                            if(showCloseToDivision){
                            showEndGuide("The cell is almost ready to divide...");
                            showCloseToDivision = false;
                            }
                        }
                    }

                    public void onFinish() {
                        showEndGuide("GO!");
                        allowedToSplit = true;
                        showCloseToDivision = true;
                    }
                }.start();
            }
        }
    }

    public void insideActiveRegion(){

        activeTF++;
        float probability = activeTF/(activeTF +3);
        if(Math.random()<=probability){
            if(subLevel.equals("A")){
                popIn.setText("Wow... creation of mRNA!");
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tfSize+tfSize/2,tfSize);
            Random rand = new Random();
            mRnaC++;
            mrnas.add(new View(this));
            mrnas.get(mrnas.size()-1).setBackground(this.getResources().getDrawable(R.drawable.cell3));
            if(tfViews.get(drawIndex).getCoordinates().x <= mainLayout.getWidth()/2){
                params.leftMargin = rand.nextInt(dnaLeft.getWidth()-(tfSize+tfSize/2)-1) + 1;
                params.topMargin = rand.nextInt(mainLayout.getHeight() - tfSize - (mainLayout.getHeight() - dnaLeft.getHeight())) + (mainLayout.getHeight() - dnaLeft.getHeight());
            }else {
                params.leftMargin = rand.nextInt(mainLayout.getWidth() - (tfSize+tfSize/2) - (mainLayout.getWidth() - dnaRight.getWidth())) + (mainLayout.getWidth() - dnaRight.getWidth());
                params.topMargin = rand.nextInt(mainLayout.getHeight() - tfSize - (mainLayout.getHeight() - dnaLeft.getHeight())) + (mainLayout.getHeight() - dnaLeft.getHeight());
            }

            mainLayout.addView(mrnas.get(mrnas.size()-1), params);
        } else if (subLevel.equals("A")){
            popIn.setText("Yuppi! One TF has just bound the promoter region!");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
        tfViews.get(drawIndex).setBackground(this.getResources().getDrawable(R.drawable.cell2));
        mainLayout.setBackgroundColor(getResources().getColor(colors[mrnas.size()]));
    }
    public void outsideActiveRegion(){
        if(subLevel.equals("A")) {
            popIn.setText("Oh, TF didn't hit the promoter region...");
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void finishWalk(){
        try{
        Thread.sleep(1000);
        }
        catch (Exception ignored){
        }
        toRestart = true;
        mainLayout.setBackgroundColor(getResources().getColor(colors[mrnas.size()]));
        showEndGuideFixed(getResources().getString(R.string.level2a_endText));

        pathView.restart();

    }

    public void restart(){
        for(TFView v:tfViews)
            mainLayout.removeView(v);
        for(View v:mrnas)
            mainLayout.removeView(v);
        walking = false;
        activeTF = 0;
        mRnaC = 0;
        tfViews.clear();
        mrnas.clear();
        if(subLevel.equals("A"))
            endGuideText.setVisibility(View.INVISIBLE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.level2_game_background));
    }

    public void showMithocondria(View v){
        guideText.setText("Mithocondria");
        showGuide(v);
    }
    public void showLysosomes(View v){
        guideText.setText("Lysosomes");
        showGuide(v);
    }
    public void showVacuole(View v){
        guideText.setText("Vacuole");
        showGuide(v);
    }
    public void showEndopl(View v){
        guideText.setText("Endoplasmic reticulum");
        showGuide(v);
    }
    public void showPerixosome(View v){
        guideText.setText("Perixosome");
        showGuide(v);
    }
    public void showGolgi(View v){
        guideText.setText("Golgi apparatus");
        showGuide(v);
    }
    public void showDNA(View v){
        guideText.setText("DNA");
        showGuide(v);
    }
    public void showDnaPromoter(View v){
        guideText.setText("DNA promoter region");
        showGuide(v);
    }

    public void showNucleous(View v){
        guideText.setText("Nucleous");
        int[] locationV = new int[2];
        v.getLocationOnScreen(locationV);

        guideText.setX(mainLayout.getWidth()/2 - guideText.getWidth()/2);
        guideText.setY(locationV[1] - guideText.getHeight());
        guideText.animate().alpha(1).setDuration(1000).setListener(guideAnimationListener);
    }

    public void showGuide(View v){
        int[] locationV = new int[2];
        v.getLocationOnScreen(locationV);

        guideText.setX(mainLayout.getWidth()/2 - v.getWidth()/2);
        guideText.setY(locationV[1] + 10);
        guideText.animate().alpha(1).setDuration(2000).setListener(guideAnimationListener);
    }

    public void showEndGuide(String text){
        int[] location = new int[2];
        pb.getLocationInWindow(location);
        toast.setGravity(Gravity.TOP,0 , location[1]);
        endGuideText.setVisibility(View.INVISIBLE);
        popIn.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
    public void showEndGuideFixed(String text){
        endGuideText.setText(text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(endGuideText.getLayoutParams());
        params.leftMargin = leftSideView.getWidth();
        params.rightMargin = rightSideView.getWidth();
        params.topMargin = startAreaView.getHeight();
        endGuideText.setLayoutParams(params);
        endGuideText.setVisibility(View.VISIBLE);
    }

    public void nextIntro(View v){
        findViewById(R.id.level2a_intro_layout).setVisibility(View.GONE);
        showEndGuide("Tap to position 5-10 transcription factors in the shaded region and double tap to start their random walk.");
    }

    public void incrementDrawIndex() {
        if (subLevel.equals("B")){
            if (drawIndex == tfViews.size() - 1) {
                drawIndex = 0;
            } else drawIndex++;
            if(tfViews.get(drawIndex).finished){
                incrementDrawIndex();
            }
        }

    }

    public void splitLevel(){
        if(subLevel.equals("B") && walking && allowedToSplit){
            int leftSideTFs = 0;
            int rightSideTFs = 0;
            Intent intent = new Intent(this,Level2cGameActivity.class);
            for(TFView v: tfViews){
                if(v.getCoordinates().x<=mainLayout.getWidth()/2)
                    leftSideTFs++;
                else
                    rightSideTFs++;
            }
            intent.putExtra("left_TFs",leftSideTFs);
            intent.putExtra("right_TFs",rightSideTFs);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.level2a_mainlayout));
    }


    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
