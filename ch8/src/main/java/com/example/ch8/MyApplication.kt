package com.example.ch8

import android.app.Application
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 애플리케이션 내에서 Application을 상속 받은 클래스를 하나 만들 수도 있다. 없을 수도 있다.
// 만약에 필요하다고 해서 Application을 상속 받은 클래스를 만들었다면 1개만 만들 수 있다.
// manifest.xml에 등록하면 <application 태그에
// 앱의 프로세스가 구동 될때, 최초에 가장 먼저 singletone 으로 생성 유지된다.
// 앱 구동후 최초 한번 실행된다. 대부분 초기화하는 코드를 넣기에 좋다.
// 앱 종료까지 유지되기 때문에. 과도한 데이터를 Application 객체에서 유지하는 것은 좋지 않다.
// 다른 곳에서 (activity, service...)에서 Application 객체 이용은 applicationcontext..

// 앱이 실행되면서 singleton으로 단 한번 실행되어야 하는 코드
// 하지만 앱이 구동되고 바로 실행되지는 않는다.
// 코틀린에서는 싱글톤 형태인 object AAA { } 을 이용할 수도 있다.


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