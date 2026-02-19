package com.example.ch5

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ch5.databinding.ActivityTest3Binding
import com.example.ch5_outer.MyAIDLInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test3Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest3Binding

    var connectionMode = "none"   // 화면 제어하기 위해서 외부 앱과 연결 상태를 표시한다.

    var aidlService : MyAIDLInterface? = null

    private fun changeViewEnable() = when(connectionMode) {
        "aidl" -> {
            binding.playButton.isEnabled = false
            binding.stopButton.isEnabled = true
        } else -> {
            binding.playButton.isEnabled = true
            binding.stopButton.isEnabled = false
            binding.progressView.progress = 0
        }
    }

    val connection = object: ServiceConnection {
        // 외부 앱에 연결되면 아래 함수 호출됨
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 외부 앱에서 전달하는 프로세스간 통신이 가능한 Stub 객체 획득
            aidlService = MyAIDLInterface.Stub.asInterface(service)
            // 연결되자마자 음악 play 해주고
            // 외부 앱에 명령을 내리고 있다. 마치 자신 앱의 객체를 호출하듯이
            aidlService!!.start()
            binding.progressView.max = aidlService!!.maxDuration
            // 코루틴을 구동시켜서 계속 프로그래스가 진행되게 한다
            lifecycleScope.launch {
                while (binding.progressView.progress < binding.progressView.max) {
                    delay(1000)
                    binding.progressView.incrementProgressBy(1000)
                }
            }
            connectionMode = "aidl" // aidl 상태로 바꾸고
            changeViewEnable()
        }

        // 외부 앱과 연결이 끊어졌다
        override fun onServiceDisconnected(name: ComponentName?) {
            aidlService = null
        }
    }

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

        binding.playButton.setOnClickListener {
            val intent = Intent("com.example.ch5_outer.ACTION_AIDL")
            intent.setPackage("com.example.ch5_outer")
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        binding.stopButton.setOnClickListener {
            aidlService!!.stop()
            unbindService(connection)
            connectionMode = "none"
            changeViewEnable()
        }
    }
}