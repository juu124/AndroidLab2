package com.example.ch2

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MainSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.setting_main, rootKey)
        // 위 코드만으로 설정 화면이 출력된다. 설정 내용이 자동으로 sharedPreferences에 저장된다.
        // 아래 코드는 필요하다면 추가해라

        // 설정 객체 획득, key로 획득한다.
        // 해당 key로 식별되는 preference를 가져왔다.
        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")

        // summary 동적 지정
        // 유저 설정 값을 그대도 summary에 출력한다.
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        idPreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "설정이 되지 않았습니다."
                } else {
                    "설정된 ID는 $text 입니다."
                }
            }


        idPreference?.setOnPreferenceChangeListener { preference, newValue ->
            Log.d("jay", "preference value : $newValue")
            true
        }
    }
}