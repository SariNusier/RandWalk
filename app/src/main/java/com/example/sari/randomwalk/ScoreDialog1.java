package com.example.sari.randomwalk;

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

/**
 * Created by Sari on 1/15/2015.
 */
public class ScoreDialog1 extends DialogFragment {
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.score_dialog_1, null);
        TextView textView =(TextView) view.findViewById(R.id.scoreTextView1);

        SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        textView.setText(textView.getText()+" "+preferences.getInt("score",0));


        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape1);
        return dialog;
    }
}
