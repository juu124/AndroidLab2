package com.example.ch3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ch3.databinding.ActivityTest3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // 시간이 오래 걸리는 상황에서 유저 이벤트에 의해 실행되어야 할 업무 가정
        binding.toastButton.setOnClickListener {
            Toast.makeText(this, "토스트....", Toast.LENGTH_SHORT).show()
        }

        // 시간이 오래 걸리는 업무 가정
        binding.calcButton.setOnClickListener {
            // case 1 - 에러 상황
            // 시간이 오래 걸리는 상황에서 유저 이벤트 발생해서 에러
            // Input dispatching timeed out
            // ANR
//            var sum = 0L
//            for (i in 1..20_000_000_000) {
//                sum += i
//            }
//            Toast.makeText(this, "sum : $sum", Toast.LENGTH_SHORT).show()

            // case 2 -
            // 개발자 스레드를 만들어서 비동기로 시간이 오래 걸리는 업무 처리
            // main thread는 유저 이벤트 처리 및 화면 반응 가능하다
            // ANR은 발생하지 않는다.
            // 그대신 다른 에러메세지가 추가되었다.
            // 개발자 스레드에서 UI를 건들리면 에러다.
            // java.lang.NullPointerException: Can't toast on a thread that has not called Looper.prepare()
//            val obj = object : Runnable {
//                override fun run() {
//                    var sum = 0L
//                    for (i in 1..20_000_000) {
//                        sum += i
//                    }
//                    // 스레드에서 여기를 접근했기 때문에 에러가 발생했다.
//                    // 개발자 스레드에서 UI를 건들리면 에러다.
//                    Toast.makeText(this@Test3Activity, "sum : $sum", Toast.LENGTH_SHORT).show()
//                }
//            }
//            val thread = Thread(obj)
//            thread.start()  // 스레드 구동 시점

//            // case 3 : Thread - Hander구조
//            val handler = object : Handler(Looper.getMainLooper()) {
//                // 개발자 스레드에서 sendMessage() 하는 순간
//                // 메인 스레드에 의해서 호출된다.
//                override fun handleMessage(msg: Message) {
//                    if (msg.what == 1) {
//                        Toast.makeText(
//                            this@Test3Activity,
//                            "sum : ${msg.obj as Long}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//
//            val obj = object : Runnable {
//                override fun run() {
//                    var sum = 0L
//                    for (i in 1..10_000_000_000) {
//                        sum += i
//                    }
//                    // 메인 스레드에게 의뢰하기
//                    val message = Message() // 데이터가 담길 의뢰서
//                    message.what = 1
//                    message.obj = sum
//                    handler.sendMessage(message)
//                }
//            }
//            val thread = Thread(obj)
//            thread.start()  // 스레드 구동 시점

            // case 4 - coroutine 으로 처리
            lifecycleScope.launch {
                // 비동기 실행 흐름을 하나 만든다.
                val sum = withContext(Dispatchers.Default) {
                    // 이 영역을 main thread 가 아닌 백그라운드 스레드로 처리
                    var result = 0L
                    for (i in 1..10_000_000_000) {
                        result += i
                    }
                    result
                }
                // 이 부분은 메인 스레드가 처리한다.
                Toast.makeText(this@Test3Activity, "sum : $sum", Toast.LENGTH_SHORT).show()
            }
        }
    }
}