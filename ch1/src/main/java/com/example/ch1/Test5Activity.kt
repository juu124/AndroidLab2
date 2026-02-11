package com.example.ch1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch1.databinding.ActivityTest5Binding
import com.google.android.material.snackbar.Snackbar

class Test5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fab.setOnClickListener {
            when (binding.fab.isExtended) {
                true -> {
                    Snackbar.make(binding.main, "메세지를 확인하시겠습니까?", Snackbar.LENGTH_SHORT)
                        .setAction("실행 취소") {
                            // 이벤트
                            Toast.makeText(this, "메세지 확인", Toast.LENGTH_SHORT).show()
                        }.show()
                    binding.fab.shrink()
                }

                false -> {
                    binding.fab.extend()
                }
            }
        }

    }
}