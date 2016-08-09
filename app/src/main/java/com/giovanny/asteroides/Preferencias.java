package com.giovanny.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by giovannyg on 09/08/2016.
 */
public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
