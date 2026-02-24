package com.example.ch8

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.ch8.databinding.ActivityTest2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Test2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTest2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTest2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 네트워킹...
        val networkService = (applicationContext as MyApplication).networkService
        val call = networkService.getUserList("1")
        call.enqueue(object: Callback<UserList> {
            override fun onResponse(
                call: Call<UserList?>,
                response: Response<UserList?>
            ) {
                // 서버데이터 획득 : json 형태로 데이터가 올 것임 UserList로 파싱된 데이터를 받게됨
                val userList = response.body()
                binding.recyclerView.adapter = MyAdapter(this@Test2Activity, userList?.data)
                binding.recyclerView.addItemDecoration(DividerItemDecoration(this@Test2Activity,
                    DividerItemDecoration.VERTICAL))
                binding.pageView.text = userList?.page
                binding.totalView.text = userList?.total
            }

            override fun onFailure(
                call: Call<UserList?>,
                t: Throwable
            ) {
                t.printStackTrace()
            }
        })
    }
}