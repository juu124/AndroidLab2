package com.example.ch2

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class BSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        // b설정 화면이 나올 준비 끝
        setPreferencesFromResource(R.xml.setting_b, rootKey)
    }

}