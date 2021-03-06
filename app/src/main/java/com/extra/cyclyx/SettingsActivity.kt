package com.extra.cyclyx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.extra.cyclyx.ui.profil.EditProfileFragment
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(settingActionBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settingsFrame, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            val aboutUs: Preference? = findPreference("about_us")
            aboutUs?.setOnPreferenceClickListener {


                val fragmentAboutUs: Fragment = AboutUsFragment()

                fragmentManager?.beginTransaction()
                    ?.replace(R.id.settingsFrame, fragmentAboutUs)
                    ?.addToBackStack(null)
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()
                true
            }

            var editProfile: Preference? = findPreference("edit_profile")
            editProfile?.setOnPreferenceClickListener {

                val editProfileFragment: Fragment =
                    EditProfileFragment()

                fragmentManager?.beginTransaction()
                    ?.replace(R.id.settingsFrame, editProfileFragment)
                    ?.addToBackStack(null)
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.commit()

                true
            }
        }
    }
}