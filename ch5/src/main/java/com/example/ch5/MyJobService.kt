package com.example.ch5

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.IBinder
import android.util.Log

// JobScheduler 에 의해 조건에 만족하는 경우 실행될 서비스
class MyJobService : JobService() {
    // onCreate(), onDestory()는 필요한 경우에만 사용한다.

    // JobScheduler에 의해 백그라운드에서 실행할 B/L이 구현되는 곳
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("jay", "MyJobService... onStartJob...")
        return false
    }

    // 비정상 종료되는 경우에 실행된다.
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}