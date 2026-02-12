package com.example.ch2

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class ASettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        // setting_a 화면이 나올 준비 끝
        setPreferencesFromResource(R.xml.setting_a, rootKey)
    }

}