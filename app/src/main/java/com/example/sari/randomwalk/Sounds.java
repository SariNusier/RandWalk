package com.example.sari.randomwalk;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Pair;

/**
 * Created by sari on 05/02/15.
 */
public class Sounds extends AsyncTask<Pair<Context,Integer>, Void, Void> {

    @Override
    protected Void doInBackground(Pair... params) {
        MediaPlayer player = MediaPlayer.create((Context)params[0].first, (Integer)params[0].second);
        player.setLooping(false); // Set looping
        player.setVolume(100,100);
        player.start();

        return null;
    }

}