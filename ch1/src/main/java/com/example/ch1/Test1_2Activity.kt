package com.example.ch1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ch1.databinding.ActivityTest12Binding

class Test1_2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest12Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTest12Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 초기 화면에 fragment 출력..
        // fragment를 다둘려면 transaction이 있어야한다.
        // transaction이 있으려면 fragmentManager가 필요하다.
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, OneFragment())
        // 변경사항 반영은 commit() 순간
        transaction.commit()

        binding.button.setOnClickListener {
            // fragment 교체
            // 주의할점 : FragmentTransaction commit()이 실행되면 그 transaction은 close된다. 다시 재 사용할 수 없다.
            // 다시 하고싶다면 새 transaction을 만들어서 제어해야한다.
            // java.lang.IllegalStateException: commit already called 발생
            // 아래의 구문만 있으면 위의 exception발생
//            transaction.replace(R.id.fragmentContainer, TwoFragment())
//            transaction.commit()

            // 새로운 transaction을 만들면 된다.
            // fragment는 기본이 back stack을 유지하지 않는다.
            // back stack이 유지되어 back button 에 의해 이전 fragment가 나오게 하려면,
            // addToBackStack()을 이용해야한다.
            val transaction2 = fragmentManager.beginTransaction()
            transaction2.replace(R.id.fragmentContainer, TwoFragment())
            transaction2.addToBackStack(null)
            transaction2.commit()
        }
    }
}