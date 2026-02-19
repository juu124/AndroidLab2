package com.example.ch5

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    // onBind()의 리턴으로 사용할 클래스를 준비한다.
    class MyBinder: Binder() {
        // Binder 라는 IBinder 구현 클래스를 상속 받아서 작성한다.
        // 내부 작성 규칙은 따로 없다.
        // 자신을 구동시킨 곳에서 호출할 함수를 자유롭게 선언한다.
        fun funA(arg: Int): Int {
            return arg * arg
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d("jay", "onBind...........")
        return MyBinder()   // 사진을 bindService로 구동시킨 곳에 전달된다.
    }

    override fun onCreate() {
        Log.d("jay", "onCreate...........")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("jay", "onDestroy...........")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("jay", "onStartCommand...........")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("jay", "onUnbind...........")
        return super.onUnbind(intent)
    }
}