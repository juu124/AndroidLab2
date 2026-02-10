package com.example.ch1

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ch1.databinding.ActivityTest2Binding
import com.example.ch1.databinding.ViewpagerItemBinding

class Test2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // case 1 : RecyclerView의 adapter를 사용하는 케이스
        binding.main.adapter = PagerAdapter()
    }
}

// viewpager adapter 영역
// 항목이 일반 뷰 (fragment가 아닌 뷰)로 구성되는 경우
// recyclerView처럼 viewholder와 adapter를 생성해서 사용할 수 있다.
class PagerViewHolder(val binding: ViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root)

class PagerAdapter : RecyclerView.Adapter<PagerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerViewHolder {
        return PagerViewHolder(
            ViewpagerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.binding.run {
            when (position) {
                0 -> {
                    textView.text = "One"
                    textView.setBackgroundColor(Color.RED)
                }

                1 -> {
                    textView.text = "two"
                    textView.setBackgroundColor(Color.GREEN)
                }

                2 -> {
                    textView.text = "three"
                    textView.setBackgroundColor(Color.YELLOW)
                }
            }
        }
    }

    override fun getItemCount() = 3
}