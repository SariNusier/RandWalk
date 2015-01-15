package com.example.sari.randomwalk;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Sari on 1/15/2015.
 */
public class MoreDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_dialog, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Learn More");
        //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawableResource(R.drawable.shape1);
        return dialog;
    }
}
