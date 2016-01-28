package de.ojauch.weatheralarmclock;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * @author Oskar Jauch
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
