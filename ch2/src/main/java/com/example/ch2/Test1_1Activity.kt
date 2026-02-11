package com.example.ch2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch2.databinding.ActivityTest11Binding
import androidx.core.content.edit

class Test1_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest11Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.saveBtn.setOnClickListener {
            // 앱 전역의 데이터이다.
            // private 말고는 다 deprecate되었다.
            val sharedPrefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
            sharedPrefs.edit {
                putString("data1", "hello")
                putInt("data2", 100)
                // 비동기 저장순간.. (동기식으로 사용하려면 commit()을 사용하기)
            }
        }

        binding.getBtn.setOnClickListener {
            val sharedPrefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
            val data1 = sharedPrefs.getString("data1", "none")
            val data2 = sharedPrefs.getInt("data2", 0)
            binding.resultView.text = "$data1 $data2"
        }
    }
}