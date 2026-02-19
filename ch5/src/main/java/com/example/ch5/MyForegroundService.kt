package com.example.ch5

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("jay", "onCreate...")
        // startForegroundService() 에 의해 실행된 서비스에서 startForeground()를 호출하지 않았을 때 발생하는 에러
        // 아래 오류는 Test2Activity에서 startForegroundService(intent)만 실행했을 때 나오는 오류
        // Context.startForegroundService() did not then call Service.startForeground()
        // 서비스 내에서 startForeground()를 호출해서 notification을 발생시키면 된다.
        // 즉, notification을 통해서 앱을 foreground로 만들어야 한다.

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
        builder.setContentTitle("title")
        builder.setContentText("text...")

        startForeground(11, builder.build())
    }
}