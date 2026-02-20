package com.example.ch6

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch6.databinding.ActivityTest2Binding

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

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                // 정상적으로 하나를 선택해서 되돌아 온 경우..
                Log.d("jay", "${it.data?.data}")
                // 선택한 항목의 식별자를 url(provider 이용)로 구성해서 전달해 준다.
                // url 을 그래도 이용해 구체적으로 원하는 데이터를 provider 에게 요청해야 한다.
                val cursor = contentResolver.query(
                    it.data!!.data!!,
                    arrayOf(    // 획득하고자 하는 데이터
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null, null, null
                )
                if (cursor!!.moveToFirst()) {
                    Log.d("jay", "${cursor.getString(0)} - ${cursor.getString(1)}")
                }
            }
        }

        binding.button2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            launcher.launch(intent)

        }
    }
}