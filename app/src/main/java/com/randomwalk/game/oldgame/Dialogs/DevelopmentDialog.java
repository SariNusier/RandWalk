package com.randomwalk.game.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.randomwalk.game.R;


public class DevelopmentDialog extends DialogFragment {

    /**
     * onCreateView method for the Development Dialog
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.under_development, null); //inflates Dialog with under_development layout
    }

    /**
     * onCreateDialog method for Development Dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape1); //sets background the shape of level 1.
        return dialog;
    }

}
