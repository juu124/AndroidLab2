package com.example.ch5

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch5.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent(this, MyService::class.java)

        binding.startButton.setOnClickListener {
            startService(intent)
        }

        binding.stopButton.setOnClickListener {
            stopService(intent)
        }

        val connection: ServiceConnection = object  : ServiceConnection {
            // service에서 객체가 전달되는 순간에 호출된다.
            // 두번째 매개변수가 service의 onBind()에서 리턴시킨 객체이다.
            // MyService에서 리턴시킨 onBind 함수의 객체가 두번째 매개변수 값
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                val serviceBinder = service as MyService.MyBinder
                // 원하는 만큼 바인딩된 객체의 함수를 호출해서 상호작용한다.
                // 바인딩된 객체의 함수 호출해서 서비스 제어하거나,
                // 매개변수, 리턴값으로 상호 데이터를 주고 받을 때 해당 로직을 여기에서 코딩한다.
                val result = serviceBinder.funA(10)
                Log.d("jay", "activity... service result : $result")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }

        binding.bindButton.setOnClickListener {
            // flag BIND_AUTO_CREATE : 현재 객체 살아 있는 거 없으면 살려라
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        binding.unBindButton.setOnClickListener {
            unbindService(connection)
        }
    }
}