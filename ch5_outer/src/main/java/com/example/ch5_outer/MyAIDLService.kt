package com.example.ch5_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

// 실제 원천 기능을 구현하고 있는 서비스
// 외부 앱에서 bindService 방식, aidl 방식으로 이용할 서비스
// 이곳에서 만들어진 aidl 파일을 구현해야 한다.
// 꼭 build -> compile을 먼저해야.. aidl 파일이 인지된다.
class MyAIDLService : Service() {

    // 음원, 영상 플레이 기능을 제공한다.
    lateinit var player: MediaPlayer

    override fun onBind(intent: Intent): IBinder {
        // 자신을 실행시킨 곳에 객체를 리턴한다.
        // aidl 을 구현한 객체의 프로세스간 통신 대행자(Stub)를 리턴한다.
        return object : MyAIDLInterface.Stub() {
            // 외부 앱에서 aidl 방식으로 함수를 호출했을 때 실행할 로직 구현
            override fun getMaxDuration(): Int {
                return if (player.isPlaying)
                    player.duration
                else
                    0
            }

            override fun start() {
                if (!player.isPlaying) {
                    player = MediaPlayer.create(this@MyAIDLService, R.raw.music)
                    try {
                        player.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun stop() {
                if (player.isPlaying)
                    player.stop()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}