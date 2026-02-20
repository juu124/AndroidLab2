package com.example.ch7

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.databinding.ActivityTest2Binding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

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
                Toast.makeText(this, "permission ok", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button2.setOnClickListener {
            //위치정보 획득 역할...
            val providerClient: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)

            val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {
                //위치 제공자 결정된 순간..
                override fun onConnected(p0: Bundle?) {
                    if (ContextCompat.checkSelfPermission(
                            this@Test2Activity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //위치 획득..
                        providerClient.lastLocation.addOnSuccessListener(
                            this@Test2Activity,
                            object : OnSuccessListener<Location> {
                                override fun onSuccess(p0: Location?) {
                                    if (p0 != null) {
                                        Log.d("jay", "${p0?.latitude}, ${p0?.longitude}")
                                    } else {
                                        // 기기에 마지막 위치 기록이 없는 경우입니다.
                                        Log.d("jay", "Location is null - GPS를 켜고 구글 지도를 실행해 보세요.")
                                    }
                                }
                            }
                        )
                    }
                }

                //사용 불가상태..
                override fun onConnectionSuspended(p0: Int) {

                }
            }

            //활용할 위치정보 제공자가 없는 경우 콜백..
            val onConnectionFailedCallback = object : GoogleApiClient.OnConnectionFailedListener {
                override fun onConnectionFailed(p0: ConnectionResult) {

                }
            }

            //콜백을 등록하면서.. GoogleApiClient 초기화..
            val apiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallback)
                .addOnConnectionFailedListener { onConnectionFailedCallback }
                .build()

            // 위치 정보 제공자 결정하라... 결과는 콜백으로..
            apiClient.connect()
        }
    }
}