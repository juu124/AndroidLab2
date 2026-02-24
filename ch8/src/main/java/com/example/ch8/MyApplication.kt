package com.example.ch8

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 최초의 한번
class MyApplication : Application() {

    var networkService: UserNetworkService


    // retrofit에 등록할 client
    // retrofit으로 서버 연동이 발생할 때 서버 연동을 가로챌 인터셉터
    // addInterceptor 필요하다면 사용하면 된다. 필수는 아님

    // 응용프로그램이 실행되고 있었음 중간에 명령을함
    // 인터셉터를 추가하면 인터셉터를 추가해서 명령을 전달한다.
    // 매번요청이 들어갈때 공통적으로 해야할 일을 보통 인터셉터작업을 한다.
    // 서버가 잘 가다가 중간에 공통적으로 해야할 일을 요청하면서 해당 인터셉터를 당하고 서버로 전송된다.
    // 여러 요청시에 공통으로 설정, 실행해야 하는 코드가 있다면 인터셉터에 담아 놓는 것이 좋다.
    // 서버 요청시 key를 요구하는 경우. 그 key를 header에 추가해야 한다.
    // 매번 모던 interface 함수에 @Header로 추가할 수도 있지만, 귀찮고 동적이지도 않다.
    // 인터셉터에서 header 값 조정되게 모든 요청시 조정된 header 값 적용된다.
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-api-key", "reqres_a662c2f9159747e8b6988c4b000b2b12")
                .addHeader("Content-Type", "application/json")
                .build()
            // 매개변수가 chain인데 그 다음 실행시켜야 하는 것을 실행시키라는 뜻
            // 아래의 코드가 없다면, 서버 요청은 발생되지 않는다.
            chain.proceed(request)

            // proceed()하단에 작성한 코드는 응답이 되면 실행된다.
        }.build()
    // 원한다면 addInterceptor로 여러개 등록.. 등록한 순서대로 실행된다.

    // retrofit 초기화
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        // 네트워킹 요청 함수가 등록된 UserNetworkService 인터페이스를 retrofit에 등록
        networkService = retrofit.create(UserNetworkService::class.java)
    }
}