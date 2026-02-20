package com.example.ch7

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.databinding.ActivityTest1Binding

class Test1Activity : AppCompatActivity() {
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

        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(this, "permission ok", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val manager = getSystemService(LOCATION_SERVICE) as LocationManager

                // 위치 획득
                val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val accuracy = location.accuracy
                    Log.d(
                        "jay",
                        "latitude : $latitude, longitude : $longitude, accuracy : $accuracy"
                    )
                } ?: {
                    Log.d("jay", "location is null")
                }
            } else {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}