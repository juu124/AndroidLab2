package com.example.ch1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch1.databinding.ActivityTest8Binding
import com.google.android.material.tabs.TabLayoutMediator

class Test8Activity : AppCompatActivity() {
    // 옆의 메뉴가 열리는지 선언하는 토글
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 개발자가 넣은 toolbar를 actionbar로 대체하겠다는 코드
        setSupportActionBar(binding.toolbar)

        // toggle 준비
        toggle =
            ActionBarDrawerToggle(this, binding.main, R.string.drawer_open, R.string.drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()  // 햄버거 아이콘과 업버튼이 drawer 상태에 맞춰야한다.

        binding.viewPager.adapter = PagerFragmentAdapter(this)

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = "Tab${position + 1}"
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_test8, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // toggle 버튼이 내부적으로 메뉴로 준비가 된다.
        // 유저가 toggle 클릭하면 메뷰 이벤트 함수가 호출된다.
        // toggle 내부의 이벤트 처리를 호출해서 drawer가 제어되게 해주어야 한다
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}