package com.example.ch4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch4.databinding.ActivityTest2Binding

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

        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                noti()
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    noti()
                } else {
                    launcher.launch("android.permission.POST_NOTIFICATIONS")
                }
            } else {
                noti()
            }
        }
    }

    fun noti() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 채널을 준비
            val channel = NotificationChannel(
                "channelId",
                "channel Name",
                NotificationManager.IMPORTANCE_HIGH     // 알림 중요도
            )
            // channel을 시스템에 등록한다.
            // 어디선가 1번만 등록만 하면되지만, 반복적으로 매번 등록해도 성능에 지장은 없으니
            // 매번 등록하는 것을 권장한다.
            manager.createNotificationChannel(channel)
            // 등록된 채널의 id를 명시해서 builder 생성한다.
            builder = NotificationCompat.Builder(this, "channelId")
        } else {
            builder = NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("메세지 도착")
        builder.setContentText("안녕하세요.")

        // 확장 클릭 이벤트 처리.. 상세보기 화면으로 이동한다.
        // 이벤트시 발생시킬 인텐트 준비
        val intent = Intent(this, DetailActivity::class.java)
        // 이벤트 발생 의뢰
        val pIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pIntent)

        // 액션 추가
        val intent2 = Intent(this, MyReceiver::class.java)
        val pIntent2 = PendingIntent.getBroadcast(this, 10, intent2, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pIntent2)
        builder.addAction(
            NotificationCompat.Action.Builder(
                android.R.drawable.stat_notify_more,
                "Action1",
                pIntent2
            ).build()
        )

        // remote input...
        val remoteInput = RemoteInput.Builder("key_reply").run {
            setLabel("답장")
            build()
        }
        // 이 remote input 에 입력한 데이터를 받을 component intent 준비..
        val intent3 = Intent(this, MyRemoteInputReceiver::class.java)
        val pIntent3 = PendingIntent.getBroadcast(this, 10, intent3, PendingIntent.FLAG_MUTABLE)
        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send,
                "답장",
                pIntent3
            ).addRemoteInput(remoteInput).build()
        )

        manager.notify(11, builder.build())
    }
}