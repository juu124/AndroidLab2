package com.example.ch8

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ch8.databinding.ItemRetrofitBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewHolder(val binding: ItemRetrofitBinding) : RecyclerView.ViewHolder(binding.root)

class MyAdapter(val context: Context, val datas: List<User>?) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ItemRetrofitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = datas?.get(position)
        holder.binding.id.text = user?.id
        holder.binding.firstNameView.text = user?.firstName
        holder.binding.lastNameView.text = user?.lastName

        user?.avatar?.let {
            // 서버에서 이미지 url이 전달되었다면..
            // 서버연동한다.. 인터페이스 함수 호출해서
            val call = (context.applicationContext as MyApplication).networkService.getAvatarImage(it)
            // call 객체의 enqueue 하는 순간. 네트워킹.. callback 등록해서 결과 처리한다.

            // 응답 받은 responseBody가 성공적으로 받거나 실패적일때 처리
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    if (response.isSuccessful) {    // response상태코드가 200
                        if (response.body() != null) {
                            // 이미지.. BitmapFactory로 Bitmap객체를 만든다.
                            val bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                            holder.binding.avatarView.setImageBitmap(bitmap)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseBody?>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }

            })

        }
    }

    override fun getItemCount() = datas?.size ?: 0
}