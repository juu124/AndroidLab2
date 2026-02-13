package com.example.ch4

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch4.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest1Binding
    lateinit var receiver: BroadcastReceiver

    lateinit var screenReceiver: BroadcastReceiver

    @SuppressLint("UnspecifiedRegisterReceiverFlag")    // IDE가 예측할 수 있는 경고를 때리는 것 빌드하지 않고도 런타임 에러가 날 수도 있다는 것을 알려줌
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

        binding.staticButton.setOnClickListener {
            val intent = Intent(this, MyReceiver::class.java)
            sendBroadcast(intent)
        }

        // 동적으로 리시버 등록
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("jay", "dynamic receiver.......")
            }
        }
        // 동적 등록은 action 문자열로 한다
        val filter = IntentFilter("com.example.ch4.MY_ACTION")
        // 등록.. 버전별 상이
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(return, filter)
        }

        binding.dynamicButton.setOnClickListener {
            val intent = Intent("com.example.ch4.MY_ACTION")
            sendBroadcast(intent)
        }

        // screen on/off 리시버 등록
        screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                // 자신을 실행시킨 intent 정보가 매개변수로 전달..
                when (intent?.action) { // 나를 실행시킨 intent 의 액션 문자열
                    Intent.ACTION_SCREEN_ON -> Log.d("jay", "screen on........")
                    Intent.ACTION_SCREEN_OFF -> Log.d("jay", "screen off........")
                }
            }
        }
        val screenFilter = IntentFilter(Intent.ACTION_SCREEN_ON).apply {
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenReceiver, screenFilter)

        // 코드에서 원하는 시점에 배터리 정보 획득
        // 아래처럼 등록하면된다. receiver를 null 로 전달하면 배터리와 관련된 정보를 추가해서 intent를 리턴한다.
        // 아래의 코드는 intent를 리턴해준다.
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            when (getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    // 전원 공급중이라면..
                    when (getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)) {
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            // 지속 충전
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.usb)
                            )
                        }

                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources, R.drawable.ac)
                            )
                        }
                    }
                }

                else -> {
                    binding.chargingResultView.text = "No Plugged"
                }
            }

            // 현재 유저 폰의 베터리 량...
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val pct = level / scale.toFloat() * 100
            binding.percentResultView.text = "$pct %"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}