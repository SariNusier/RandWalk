package com.example.sari.randomwalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;
import java.util.Random;

//BEWARE OF THIS CLASS. IT WAS WRITTEN BY A SHIT PROGRAMMER SO DO NOT ENTER. IF YOU WANT TO IMPLEMENT SOMETHING LIKE THIS YOU WOULD BE BETTER OFF CREATING EVERYTHING YOURSELF.
public class GameView2 extends View implements OnTouchListener {


    boolean isPlacing = true;
    int cellCount = 0;
    int cellToDraw = 0;
    float numTF = 0.0f;
    boolean bitmapSaved = false;
    boolean isStarted = false;
    boolean listenTouch = true;
    String subLevel;
    Random rand;
    ArrayList<Point> points;
    ArrayList<Rect> mRNA;
    Paint paintWalk;
    Canvas canvas;
    Bitmap playingBitmap; //this bitmap stores the game status during play
    Bitmap initialBitmap; //this bitmap stores the initial, clean state of the screen
    DisplayMetrics metrics;
    Drawable cellPicture;
    Drawable cellPicture2;
    Drawable cellPicture3;


    Level2Activity parentActivity;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //Sounds sounds = new Sounds();
    //<-----CONSTRUCTOR------>
    public GameView2(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        points = new ArrayList<>();
        mRNA = new ArrayList<>();

        /*
        <===== INITIALISATIONS =====>
         */
        paintWalk = new Paint(); //construct paint for drawing path/walk
        rand = new Random();
        parentActivity =(Level2Activity) context; //casts the context as a Level1Activity to use updateScore()
       // subLevel = parentActivity.getSubLevel(); // gets selected sublevel
        preferences = context.getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);//get preferences GAME_DATA
        editor = preferences.edit(); //sets the editor as editor of preferences declared above
        metrics = getResources().getDisplayMetrics(); //gets the metrics of the screen
        playingBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        initialBitmap = Bitmap.createBitmap(metrics.widthPixels, metrics.heightPixels, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(playingBitmap); //canvas draw into a bitmap
        canvas.drawColor(Color.parseColor("#c2d5f0")); //sets the color of the background
        cellPicture = this.getResources().getDrawable(R.drawable.cell1); // the pirate arrrr!
        cellPicture2 = this.getResources().getDrawable(R.drawable.cell2);
        cellPicture3 = this.getResources().getDrawable(R.drawable.cell3);
        Drawable dnaImage = this.getResources().getDrawable(R.drawable.dna);
        dnaImage.setBounds(0,(int)Math.round(metrics.heightPixels/1.4),metrics.widthPixels,metrics.heightPixels);
        dnaImage.draw(canvas);
        Drawable leftSide = this.getResources().getDrawable(R.drawable.left_side_2);
        Drawable rightSide = this.getResources().getDrawable(R.drawable.right_side_2);
        leftSide.setBounds(0,metrics.heightPixels/12,metrics.widthPixels/4,(int)Math.round(metrics.heightPixels/1.4));
        rightSide.setBounds(metrics.widthPixels/4*3,metrics.heightPixels/12,metrics.widthPixels,(int)Math.round(metrics.heightPixels/1.4));
        rightSide.draw(canvas);
        leftSide.draw(canvas);
        Drawable startSurface = this.getResources().getDrawable(R.drawable.start_surface_2);//area where the player starts
        startSurface.setBounds(0,0,metrics.widthPixels,metrics.heightPixels/12);
        startSurface.draw(canvas);

        subLevel = parentActivity.getSubLevel();
        paintWalk.setColor(getResources().getColor(R.color.BlueLine)); //set the color of the walk
        paintWalk.setStrokeWidth(3); //sets the width of the walk
        paintWalk.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0)); //sets the dash effect of the walk
        paintWalk.setStyle(Paint.Style.STROKE);


    }
        // 2
    @Override
    public void onDraw(final Canvas canvas) {
        for(Point p : points) {
            if(p.equals(points.get(cellToDraw)))
            if (p.y <= metrics.heightPixels && p.y >= metrics.heightPixels/24*22 && p.x >= metrics.widthPixels/8*3 && p.x <= metrics.widthPixels/8*5) {
                cellPicture2.setBounds(p.x - 25,p.y - 25,p.x + 25,p.y+ 25);Log.d("PIRATE DRAWN","PIRATE DRAWN"+points.size() );
                cellPicture2.draw(this.canvas);
                cellToDraw++;
                numTF++;
                float probability = numTF/(numTF +3);
                Log.d("CellToDRAW","Cell index: "+probability );
                if(Math.random()<=probability){
                    int randPosition;

                    if(p.x <= metrics.widthPixels/2) {
                        randPosition = 41 + rand.nextInt(metrics.widthPixels / 4 - 41);
                    }
                    else
                        randPosition = (metrics.widthPixels/4*3) + rand.nextInt(metrics.widthPixels/4-41);

                    cellPicture3.setBounds(randPosition - 41,metrics.heightPixels/24*22 - 28,randPosition + 41,metrics.heightPixels/24*22 + 28);
                    cellPicture3.draw(this.canvas);

                }
                if(cellToDraw == cellCount) {
                    listenTouch = true;
                }
            } else if (p.y > metrics.heightPixels/24*22) {
                cellToDraw++;
                Log.d("CellToDRAW","Cell index: "+cellToDraw);
                cellPicture.setBounds(p.x - 25,p.y - 25,p.x + 25,p.y+ 25);
                cellPicture.draw(this.canvas);
                if(cellToDraw == cellCount){
                    listenTouch = true;Log.d("PIRATE DRAWN!!","PIRATE DRAWN"+points.size());}
            }
             else if (isStarted == true && cellCount == 10 && points.get(cellToDraw).equals(p)) {
                drawTick(p);
            }
            isStarted = true;
        }

        canvas.drawBitmap(playingBitmap, 0, 0, paintWalk);
        if(bitmapSaved == false) {
            initialBitmap = Bitmap.createBitmap(playingBitmap, 0, 0, playingBitmap.getWidth(), playingBitmap.getHeight(), null, true);
            bitmapSaved = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(listenTouch == true && MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {

            if (event.getY() <= metrics.heightPixels / 12 && event.getX() > metrics.widthPixels/4 && event.getX() < metrics.widthPixels/4*3 && isPlacing) {
                onCellPlaced(event);
            }
            else if(cellCount == 10) {
                onStartAgain();
            }
        }
        Log.d("ON TOUCH EVENT","ON TOUCH EVENT"+event.toString());
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void drawTick(Point p){
        int random_X, random_Y;
        random_Y = rand.nextInt(31);
        random_X = rand.nextInt(61)-30;
        if (p.x + random_X <= metrics.widthPixels/4){  // || p.x >= metrics.widthPixels/4*3) {
            canvas.drawLine(p.x,p.y,metrics.widthPixels/4,p.y+random_Y/2,paintWalk);
            canvas.drawLine(metrics.widthPixels/4,p.y+random_Y/2,p.x,p.y+random_Y,paintWalk);
            p.y = p.y + random_Y;
        } else if(p.x + random_X >= metrics.widthPixels/4*3){
            canvas.drawLine(p.x,p.y,metrics.widthPixels/4*3,p.y+random_Y/2,paintWalk);
            canvas.drawLine(metrics.widthPixels/4*3,p.y+random_Y/2,p.x,p.y+random_Y,paintWalk);
            p.y = p.y + random_Y;
        } else {
            canvas.drawLine(p.x, p.y, p.x + random_X, p.y + random_Y, paintWalk);
            p.x = p.x + random_X;
            p.y = p.y + random_Y;
        }
        invalidate();
    }


    public void onBegin(){

    }
    public void onCellPlaced(MotionEvent event){

        paintWalk.setColor(getResources().getColor(R.color.BlueLine));
        cellCount++;
        if(cellCount == 10) {
            isPlacing = false;
            listenTouch = false;
        }

        points.add(new Point((int)event.getX(),metrics.widthPixels/12));
        cellPicture.setBounds((int)event.getX() - 25, metrics.heightPixels/24 - 25,(int)event.getX() + 25, metrics.heightPixels/24 + 25);

        Log.d("event X","Event x:"+(int)event.getX()+" "+metrics.heightPixels/12);
        cellPicture.draw(canvas);

        invalidate();

    }

    /**
     * Restart everything
     */
    public void onStartAgain(){
        isPlacing = true;
        cellCount = 0;
        cellToDraw = 0;
        numTF = 0.0f;
        points = new ArrayList<>();
        canvas.drawBitmap(initialBitmap,0,0,paintWalk);
        invalidate();
    }

    public void onWalkStarted(){

    }

    public void onWalkFinished(){

    }

}

