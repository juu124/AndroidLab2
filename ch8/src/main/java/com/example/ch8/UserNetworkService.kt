package com.example.ch8

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

// network 시 호출될 함수를 선언한 interface
// 어노테이션이 꼭 붙여져야한다. 그래야 서버가 안다
interface UserNetworkService {
    // 제네릭 타입.. 데이터 컨버트 DTO
    // URL이 쿼리데이터로 넘어가는 경우
    @GET("api/users")
    fun getUserList(@Query("page") page: String): Call<UserList>

    // 만약에 서버에서 받은 데이터를 그대로 전달해달라.. ResponseBody 이미지 다운로드의 경우
    @GET
    fun getAvatarImage(@Url url: String): Call<ResponseBody>

    // 동적 path로 데이터가 넘어가는 경우
    // 동적으로 구성되어야하는 데이터를 중괄호({})로 묶어준다.
    @GET("group/{a1}/users/{a2}")
    fun test1(@Path("a1") data1: String, @Path("a2") data2: String): Call<UserList>

    // QueryMap.. Query 데이터 많거나.. 상황에 따라 갯수가 다르거나..
    @GET("group/users")
    fun test2(@QueryMap datas: Map<String, String>, @Query("name") name: String): Call<UserList>
    // 대량의 데이터 파일의 업로드, 보안상 문제되는 데이터는 GET방식으로 불가능하다.
    // GET으로는 유저가 어떤 데이터가 전송되는지 유저가 확인할 수 있다. -> 보안에 취약
    // => POST방식 사용
    @POST("user/edit")
    fun test3(
        @Body user: User, // 이 데이터를 json 으로 만들어서 body stream 으로 서버에 전송한다.
        @Query("name") name: String // POST 방식이라고 하더라도 query string 으로 몇몇 데이터 전송 가능
    ): Call<UserList>
}