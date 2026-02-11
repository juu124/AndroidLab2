package com.example.ch2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.ch2.databinding.ActivityTest12Binding
import kotlinx.coroutines.launch

class Test1_2Activity : AppCompatActivity() {
    val dataStore: DataStore<Preferences> by preferencesDataStore("my_prefs")

    // 데이터의 키로 사용할 변수 선언
    // 타입 안정성을 위해서.. 변수로 키를 지정하게 한다.
    val DATA1 = stringPreferencesKey("data1")
    val DATA2 = intPreferencesKey("data2")

    // coroutine(kotlin 언어에서 제공하는 비동기 기법)의 suspend 함수로 선언한다.
    // java로 개발하면 사용하지 못한다.
    private suspend fun save() {
        dataStore.edit { preferences ->
//            preferences[DATA1] = 10     // error - SharePreferences는 해당 타입 안정성을 확인해주지 않는다.
            preferences[DATA1] = "world"
            preferences[DATA2] = 200
        }
    }

    private suspend fun get() {
        dataStore.data.collect {
            val data1 = it[DATA1] ?: ""
            val data2 = it[DATA2] ?: 0
            Log.d("jay", "$data1 $data2")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest12Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.saveBtn.setOnClickListener {
            // datastore 는 코루틴에 의해 비동기적으로 처리되어야한다.
            // 비동기 흐름을 만들어 업무를 진행한다. 정도만 이해하기
            // launch가 실행되고 메인 스레드가 실행된다. launch에서 비동기로 save함수를 호출한다.
            lifecycleScope.launch {
                save()
            }
        }
        binding.getBtn.setOnClickListener {
            lifecycleScope.launch {
                get()
            }
        }
    }
}