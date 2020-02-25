package com.maksimovamaris.chess.preferences;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.maksimovamaris.chess.R;

public class PreferencesFragment extends PreferenceFragmentCompat {
    private static final String ARG_ROOT_SCREEN = "root";

    public static PreferencesFragment newInstance(String rootScreen) {
        Bundle args = new Bundle();
        args.putString(ARG_ROOT_SCREEN, rootScreen);
        PreferencesFragment fragment = new PreferencesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, getArguments().getString(ARG_ROOT_SCREEN));
        Preference colorPref = findPreference(getString(R.string.color_pref_key));
        if (colorPref != null)
            colorPref.setSummaryProvider(new Preference.SummaryProvider() {
                @Override
                public CharSequence provideSummary(Preference preference) {
                    if ("0"
                            .equals(((ListPreference) preference).getValue())) {

                        return getResources().getStringArray(R.array.board_themes)[0];
                    }
                    else
                    if ("1"
                            .equals(((ListPreference) preference).getValue())) {
                        return getResources().getStringArray(R.array.board_themes)[1];
                    }
                    else
                    return getResources().getStringArray(R.array.board_themes)[2];
                }
            });
    }
}
