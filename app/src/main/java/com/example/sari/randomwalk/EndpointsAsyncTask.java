package com.example.sari.randomwalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.example.sari.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Created by sari on 26/01/15.
 */
public class EndpointsAsyncTask extends AsyncTask<Pair<Context,Try>,Void, String> {
    private MyApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Pair<Context, Try>... params) {
        if(myApiService == null){

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("https://randwalk-project.appspot.com/_ah/api/");
            myApiService = builder.build();

        }

        context = params[0].first;
        Try trai = params[0].second;
        SharedPreferences preferences = context.getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        try {
            //return myApiService.saveData(name, preferences.getInt("score", 0)).execute().getData();
            return myApiService.saveDataLevel1A(trai.getId(), trai.getScore(), trai.getStartingPoint(),trai.getFinalPointX(),trai.getFinalPointY(),trai.getLength(), trai.getSubLevel()).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result){
        Log.d("THIS IS THE RESULT: ", result);
    }
}
