package com.example.ch8

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ch8.databinding.ActivityTest3Binding

class Test3Activity : AppCompatActivity() {
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

        // 리소스 이미지 로딩
//        Glide.with(this)
//            .load(R.drawable.seoul)
//            .override(200, 200) // 화면에 출력되는 view 사이트가 아니라 데이터의 원본데이터 사진 크기 OOM 해결
//            .into(binding.imageView)

        // gallery 이미지 출력
//        val launcher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) {
//            Glide.with(this)
//                .load(it.data!!.data)
//                .into(binding.imageView)
//        }
//
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        launcher.launch(intent)

        // network
        Glide.with(this)
            .load("https://mblogthumb-phinf.pstatic.net/MjAyNDA1MjRfNDMg/MDAxNzE2NTYyMjM5NTc1.ZMKXOcTul_ki_U3gP9kxJrOJWZlAu-ds1FUym9QO2Asg.oIpXzH9p-4OuEtcpUOJxU-rcYI2ub0AH5_i82MBwOrYg.JPEG/%EC%95%88%EA%B0%9C%EC%9D%98_%EC%9A%A9_%EC%95%84%EB%B0%94%ED%83%80_%EB%A9%94%EC%9D%B8%EC%9A%A91.jpg?type=w800")
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(binding.imageView)
    }
}