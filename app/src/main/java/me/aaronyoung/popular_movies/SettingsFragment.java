package me.aaronyoung.popular_movies;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by shangweiyoung on 10/22/16.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
