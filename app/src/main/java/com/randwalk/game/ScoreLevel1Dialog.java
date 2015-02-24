package com.randwalk.game;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.randwalk.game.R;

public class ScoreLevel1Dialog extends DialogFragment {

    TextView textView;

    /**
     * onCreateView method for ScoreLevel1Dialog
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.score_level1_dialog, null);   //inflates view with score_level1_dialog layout
        textView = (TextView) view.findViewById(R.id.scoreLevel1TextView);
        SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);  //gets score preferences
       // textView.setText(textView.getText()+" "+preferences.getInt("score",0));    //gets score and shows it into Level 1's score textview
        textView.setText("Your score for level 1A: "+preferences.getInt("score_1A",0)+"\n"+"Your score for level 1B: "+preferences.getInt("score_1B",0));
        return view;
    }

    /**
     * onCreateDialog method for ScoreLevel1Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape1); //sets the shape of the window as the level 1 shape
        return dialog;
    }

}
