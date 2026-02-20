package com.example.ch6

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch6.databinding.ActivityTest3Binding

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

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                // gallery app 에서 사진 선택해서 되돌아 오면, 선택한 사진 식벽라자 url로 넘어온다.
                // provider 에게 구체적으로 원하는 데이터를 요청할 수 있다.
                // 유저가 선택한 이미지를 stream 으로 읽어서 활용만 하면된다면?(즉 화면에 단순 출력하거나 서버에 업로드 하거나..)
                // provider가 제공하는 이미지를 일기 위한 stream을 바로 이용한다. 즉, file 경로 같은것은 필요 없다.
                // 그런데 만약 한번 선택한 이미지를 이후 앱이 실행될때도 다시 나오게 해야 한다면, DB 같은데 저장해야 한다.
                // 이때는 파일 경로로 저장해야 한다.
                // 목적에 따라서 Stream만 사용할 것인지 file 경로로 사용할 것인지 개발자가 선택해야한다.

                val stream = contentResolver.openInputStream(it.data!!.data!!)  // 이 매개변수는 Uri가 들어가고 식별자가 들어가게 된다.
                // 아래 까지만 하면 OOM 문제가 발생할 수 있다.
                // OOM 을 고려해서 읽을 때 데이터 사이즈를 줄여서 로딩해야한다.
                val option = BitmapFactory.Options()
                option.inSampleSize = 10    // 원본 이미지 사이즈를 10분의 1로 줄여라
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()     // stream를 닫아준다. 물줄기 봉인

                bitmap?.let {
                    // 실제 읽은게 있다면
                    binding.imageView.setImageBitmap(bitmap)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.button3.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // 어느 타입의 데이터를..
            intent.type = "image/*"
            launcher.launch(intent)
        }
    }
}