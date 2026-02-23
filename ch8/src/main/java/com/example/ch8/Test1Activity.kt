package com.example.ch8

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch8.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() // 여러개의 퍼미션을 한꺼번에 유저에게 허락하게 하기
        ) { permissions ->
            // 여러개의 퍼미션들이 하나하나 퍼미션 상태가 boolean 으로 집합 객체로 전달된다.
            // 모두 허락한 상태인가?
            val allGranted = permissions.values.all { it }  // 모두 true 이면 전체 true
            if (allGranted) {
                Toast.makeText(this, "permission ok", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
            }

        }

        binding.button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // 전화기로서의 상태 변화 감지하자
                    telephonyManager.registerTelephonyCallback(
                        // 전화기로서의 상태 변경시에 실행될 callback을 만들때
                        // TelephonyCallback은 기본 상속..
                        // 감지하고자 하는 Listener 만 등록한다.
                        mainExecutor,
                        object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                            // 전화 걸려오는 상태 변경시마다 호출된다. 매개변수가 상태 정보
                            override fun onCallStateChanged(state: Int) {
                                when (state) {
                                    TelephonyManager.CALL_STATE_IDLE -> {
                                        Log.d("jay", "IDLE ======")
                                    }

                                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                                        Log.d("jay", "OFFHOOK ======")
                                    }

                                    TelephonyManager.CALL_STATE_RINGING -> {
                                        Log.d("jay", "RINGING ======")
                                    }
                                }
                            }
                        }
                    )
                }

                // 전화 번호 획득
                var phoneNumber = "unknown"
                // 버전에 따라 전화번호 획득의 차이가 있다.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // SubscriptionManager를 통해서 전화번호를 획득하게
                    val subscriptionManager =
                        getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                    for (info: SubscriptionInfo in subscriptionManager.activeSubscriptionInfoList!!) {
                        val id = info.subscriptionId
                        phoneNumber = subscriptionManager.getPhoneNumber(id)
                    }
                } else {
                    phoneNumber = telephonyManager.line1Number
                }
                Log.d("jay", "phone number : $phoneNumber")

                getRequestNetwork()

            } else {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_PHONE_NUMBERS
                    )
                )
            }
        }
    }

    private fun getRequestNetwork() {
        // 요청 조건
        // 어떤 네트워크의 조건에 관심이 있는지..
        val networkRequest = NetworkRequest.Builder()
            // 데이터 통신 네트워킹
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val connManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        // ConnectivityManager 에게 조건을 등록하고 결과는 callback으로
        connManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("jay", "onAvailable...")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("jay", "onUnavailable...")
            }
        })
    }
}