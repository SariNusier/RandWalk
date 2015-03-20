package com.randwalk.game.newgame.level1.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.randwalk.game.R;

/**
 * Created by sari on 20/03/15.
 */
public class PirateView extends View {

    public PirateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable background = this.getResources().getDrawable(R.drawable.rsz_pirate);
        this.setBackground(background);
    }

    public PirateView(Context context) {
        super(context);
        Drawable background = this.getResources().getDrawable(R.drawable.rsz_pirate);
        this.setBackground(background);
    }
}
