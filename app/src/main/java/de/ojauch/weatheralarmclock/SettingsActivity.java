package de.ojauch.weatheralarmclock;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Activity to display the application settings
 * @author Oskar Jauch
 */
public class SettingsActivity extends PreferenceActivity {

    public static final String KEY_PREF_CITY = "pref_city";
    public static final String KEY_PREF_TIME_SHIFT = "pref_time_shift";
    public static final String KEY_PREF_HOURS = "pref_hours";
    public static final String KEY_PREF_MINUTES = "pref_minutes";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
