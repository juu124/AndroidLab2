package com.example.ch3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch3.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
    var count = 0
    lateinit var binding: ActivityTest1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.intentBtn.setOnClickListener {
            startActivity(Intent(this, SomeActivity::class.java))
        }

        binding.countView.text = "$count"
        binding.incrementBtn.setOnClickListener {
            count++
            binding.countView.text = "$count"
        }
        Log.d("jay", "onCreate....")
    }

    override fun onStart() {
        super.onStart()
        Log.d("jay", "onStart....")
    }

    override fun onResume() {
        super.onResume()
        Log.d("jay", "onResume....")
    }

    override fun onStop() {
        super.onStop()
        Log.d("jay", "onStop....")
    }

    override fun onPause() {
        super.onPause()
        Log.d("jay", "onPause....")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("jay", "onDestroy....")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
        Log.d("jay", "onSaveInstanceState....")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("count")
        binding.countView.text = "$count"
        Log.d("jay", "onRestoreInstanceState....")
    }
}