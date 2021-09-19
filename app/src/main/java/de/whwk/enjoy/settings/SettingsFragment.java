package de.whwk.enjoy.settings;

import android.os.Bundle;
import android.view.Menu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import de.whwk.enjoy.BuildConfig;
import de.whwk.enjoy.R;

public class SettingsFragment extends PreferenceFragmentCompat {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onPrepareOptionsMenu(@NonNull Menu menu) {
    menu.clear();
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.settings, rootKey);
    Preference p = findPreference("key_version");
    if (p != null) {
      p.setSummary(BuildConfig.VERSION_NAME);
    }
  }
}
