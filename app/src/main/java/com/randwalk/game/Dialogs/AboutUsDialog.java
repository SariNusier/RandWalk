package com.randwalk.game.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randwalk.game.R;


public class AboutUsDialog extends DialogFragment {

    /**
     * onCreateView method for LearnMoreDialog
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_us_dialog, null); //inflates view with learn_more_dialog layout
    }

    /**
     * onCreateDialog method for LearnMoreDialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("About Us");
        return dialog;
    }

}
