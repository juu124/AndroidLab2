package com.example.ch7

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch7.databinding.ActivityTest3Binding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// 지도 객체를 callback 방식으로 전달한다.
class Test3Activity : AppCompatActivity(), OnMapReadyCallback {

    // fragment 내에 있는 지도 제어 객체
    var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityTest3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 지도 콜백 클래스 등록
        // 화면에 출력되고 있는 fragment를 획독하고, 그 fragment에 callback을 등록한다.
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.main.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // 지도가 준비된 순간 자동 호출된다. 매개변수로 지도 객체를 전달한다.
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        moveMap()
    }

    private fun moveMap() {
        // 지도 내에서의 특정 위치, LatLng객체를 준비해야한다.
        val latLng = LatLng(37.519, 127.123)
        // 지도 옵션
        val position = CameraPosition.Builder()
            .target(latLng) // 지도의 센터 위치
            .zoom(16f)  // 초기 zoom level
            .build()

        // 지도 중앙점 이동
        googleMap!!.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        // marker 등록
        // marker 정보
        val markerOptions = MarkerOptions()
        // 마크 아이콘 지정.. 라이브러리에 등록된 아이콘을 이용한다. 개발자 아이콘도 가능
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        markerOptions.position(latLng)
        markerOptions.title("MyLocation")   // 마커 클릭시 나오는 info Window title 부분
        // marker 올리기
        googleMap?.addMarker(markerOptions)

    }
}