package com.example.ch8

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

// network 시 호출될 함수를 선언한 interface
// 어노테이션이 꼭 붙여져야한다. 그래야 서버가 안다
interface UserNetworkService {
    // 제네릭 타입.. 데이터 컨버트 DTO
    @GET("api/users")
    fun getUserList(@Query("page") page: String): Call<UserList>

    // 만약에 서버에서 받은 데이터를 그대로 전달해달라.. ResponseBody 이미지 다운로드의 경우
    @GET
    fun getAvatarImage(@Url url: String): Call<ResponseBody>
}