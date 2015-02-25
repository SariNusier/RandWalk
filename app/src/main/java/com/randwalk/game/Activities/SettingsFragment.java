package com.randwalk.game.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.randwalk.game.R;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences); // Load the preferences from an XML resource
        Preference preferenceRestartScore = findPreference("reset_score"); //finds the preference based on the key

        preferenceRestartScore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Reset Score")
                        .setMessage("Are you sure you want to reset your score?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() { //if sure

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("score_1A",0); //restarts score
                                editor.putInt("score_1B",0);
                                editor.commit(); //commits changes
                            }

                        })
                        .setNegativeButton("No", null).show();
                return false;
            }
        });
    }
}
