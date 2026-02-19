package com.example.ch5

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch5.databinding.ActivityTest2Binding

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
                Toast.makeText(this, "permission ok...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.foregroundButton.setOnClickListener {
            val intent = Intent(this, MyForegroundService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    } else {
                        // 하위 버전에는 백그라운드 제약이 없다.
                        startService(intent)
                    }
                } else {
                    launcher.launch("android.permission.POST_NOTIFICATIONS")
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    // 하위 버전에는 백그라운드 제약이 없다.
                    startService(intent)
                }
            }
        }

        binding.jobButton.setOnClickListener {
            val jobScheduler: JobScheduler? =
                getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            // 등록 조건
            // 어느 서비스를 실행시키는 조건인지 명시되었다.
            // jobId : 외부(액티비티 같은 곳에서)에서 구동중인 JobService를 종료시킬 때 필요하다.
            val builder = JobInfo.Builder(
                1,
                ComponentName(this, MyJobService::class.java)
            ) // 어떤서비스를 아래의 조건을 지정한다.
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)  // wifi에 연결되어있다면 상태의 조건을 지정
            // 시스템에 동록
            jobScheduler?.schedule(builder.build())

        }
    }
}