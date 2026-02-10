package com.example.ch1

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
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

//        // case 1 : RecyclerView의 adapter를 사용하는 케이스
//        binding.main.adapter = PagerAdapter()

        // case 2 : FragmentStateAdapter를 사용하는 케이스
        binding.main.adapter = PagerFragmentAdapter(this)
        binding.main.orientation = ViewPager2.ORIENTATION_VERTICAL
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

// viewPager 항목은 fragment로 준비하는 경우의 adapter 영역
class PagerFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val fragments: List<Fragment>

    init {
        // fragment 미리 생성
        // fragment 생성은 상황에 따라 생성하면 된다.
        // fragment가 많다면 여기서 하는게 비효율적일 수도 있다.
        fragments = listOf(OneFragment(), TwoFragment())
    }

    // 각 항목(화면)을 위한 fragment를 결정하기 위해서 자동 호출한다.
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount() = fragments.size

}