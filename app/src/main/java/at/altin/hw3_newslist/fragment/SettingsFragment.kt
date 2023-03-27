package at.altin.hw3_newslist.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.altin.hw3_newslist.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}