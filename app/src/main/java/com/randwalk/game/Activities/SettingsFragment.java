package com.randwalk.game.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.randwalk.game.R;


public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences); // Load the preferences from an XML resource

        Preference preferenceRestartScore = findPreference("reset_score"); //finds the preference based on the key
        Preference preferenceRestartScore1A = findPreference("reset_score_1A");
        Preference preferenceRestartScore1B = findPreference("reset_score_1B");
        Preference preferenceRestartScore1C = findPreference("reset_score_1C");

        preferenceRestartScore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Reset Score")
                        .setMessage("Are you sure you want to reset your score for the entire level 1?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() { //if sure

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("score_1A",0);
                                editor.putInt("score_1B",0);
                                editor.putInt("score_1C",0);
                                editor.putBoolean("level1BUnlocked",false);
                                editor.putBoolean("level1CUnlocked",false);
                                editor.commit(); //commits changes
                            }

                        })
                        .setNegativeButton("No", null).show();
                return false;
            }
        });

        preferenceRestartScore1A.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Reset Score")
                        .setMessage("Are you sure you want to reset your score for level 1A?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() { //if sure

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("score_1A",0);
                                editor.commit(); //commits changes
                            }

                        })
                        .setNegativeButton("No", null).show();
                return false;
            }
        });

        preferenceRestartScore1B.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Reset Score")
                        .setMessage("Are you sure you want to reset your score for level 1B?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() { //if sure

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("score_1B",0);
                                editor.commit(); //commits changes
                            }

                        })
                        .setNegativeButton("No", null).show();
                return false;
            }
        });

        preferenceRestartScore1C.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Reset Score")
                        .setMessage("Are you sure you want to reset your score for level 1C?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() { //if sure

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SharedPreferences preferences = getActivity().getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("score_1C",0);
                                editor.commit(); //commits changes
                            }

                        })
                        .setNegativeButton("No", null).show();
                return false;
            }
        });
    }
}
