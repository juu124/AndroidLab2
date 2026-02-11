package com.example.ch1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch1.databinding.ActivityTest6Binding
import com.google.android.material.tabs.TabLayoutMediator

class Test6Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // viewpager adapter 지정
        binding.viewPager.adapter = PagerFragmentAdapter(this)
        // viewPager와 tablayout 상호 연동 처리
        // 3번째 함수 자동 호출.. 등록한 viewpager 항목 갯수만큼
        // tab : tab 버튼
        // position : 몇번째 버튼(0부터 시작)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = "Tab ${position + 1}"
        }.attach()
    }
}