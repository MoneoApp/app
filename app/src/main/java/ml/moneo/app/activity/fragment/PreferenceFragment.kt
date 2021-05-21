package ml.moneo.app.activity.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ml.moneo.app.R

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}