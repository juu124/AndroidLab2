package com.example.ch4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class MyRemoteInputReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // remote input data 추출한다.
        // 자신을 실행시킬 인텐트에서 문자열 키값을 가져오겠다
        val data = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_reply")
        Log.d("jay", "reply : $data")

        // noti의 remote input 에서 전송 버튼을 누르면 빙빙빙 돈다.
        // 받은 곳에서 처리완료 되었다고 noti를 갱신해야 한다. 동일 id의 noti를..
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

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
            builder = NotificationCompat.Builder(context, "channelId")
        } else {
            builder = NotificationCompat.Builder(context)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("답장 완료")
        builder.setContentText("메세지가 성공적으로 전송되었습니다.")
        manager.notify(11, builder.build())
    }
}