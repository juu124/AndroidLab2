package com.example.ch2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch2.databinding.ActivityTest3Binding
import java.io.File

class Test3Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.internalBtn.setOnClickListener {
            // fildDir : 내장 메모리
            val file = File(filesDir, "test.txt")
            val writer = file.writer()
            writer.write("helloworld - internal")
            writer.flush()

            val reader = file.reader().buffered()
            reader.forEachLine {
                binding.resultView.text = it
            }

        }

        binding.externalBtn.setOnClickListener {
            // getExternalFilesDir : 외장 메모리
            val file = File(getExternalFilesDir(null), "test.txt")
            val writer = file.writer()
            writer.write("helloworld - external")
            writer.flush()

            val reader = file.reader().buffered()
            reader.forEachLine {
                binding.resultView.text = it
            }

        }
    }
}