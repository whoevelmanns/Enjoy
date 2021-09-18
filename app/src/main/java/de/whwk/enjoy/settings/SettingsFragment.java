package de.whwk.enjoy.settings;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import de.whwk.enjoy.BuildConfig;
import de.whwk.enjoy.R;

public class SettingsFragment extends PreferenceFragmentCompat {
  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
    Preference p = findPreference("key_version");
    if (p != null) {
      p.setSummary(BuildConfig.VERSION_NAME);
    }
  }
}
