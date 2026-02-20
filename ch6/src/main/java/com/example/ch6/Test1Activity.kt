package com.example.ch6

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch6.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val cursor = contentResolver.query(
                Uri.parse("content://com.example.ch6_outer.provider"),
                null, null, null, null
            )
            cursor?.run {
                Log.d("jay", "total rows : ${count}")
                Log.d("jay", "columns : ${columnNames.joinToString(",")}")   // 컬럼명을 알고 싶을때 columnNames 사용. 여러 문자를 단일 문자열로
                while (moveToNext()) {
                    // id컬럼의 index를 구하고 그 index 위치의 column data를 뽑아달라
                    // 만약 id 컬럼이 없으면 exception 발생 시켜라
                    val id = getString(getColumnIndexOrThrow("id"))
                    val data = getString(getColumnIndexOrThrow("data"))
                    Log.d("jay", "$id - $data")
                }
            }
        }
    }
}