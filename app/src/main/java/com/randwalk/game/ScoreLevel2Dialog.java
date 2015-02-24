package com.randwalk.game;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


public class ScoreLevel2Dialog extends DialogFragment {

    /**
     * onCreateView method for ScoreLevel2Dialog
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_level2_dialog, null); //inflates view with score_level2_dialog layout
    }

    /**
     * onCreateDialog method for ScoreLevel2Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape2);    //sets the shape of the window as the level 1 shape
        return dialog;
    }

}