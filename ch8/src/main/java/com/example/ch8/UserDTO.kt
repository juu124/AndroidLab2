package com.example.ch8

import com.google.gson.annotations.SerializedName

// 서버에서 받은 데이터 추상화한다.
// 데이터를 묶기 위한 클래스
data class User(
    var id: String,
    // json key 와 변수명 매핑
    // 서버에서 요청하는 변수명과 다르면 어노테이션으로 선언
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var avatar: String  // 프로필 사진 다운로드 url
)

// 유저 페이지 정보
data class UserList(
    // 서버에서 넘어오는 모든 데이터를 변수로 선언할 필요는 없다. 사용하고자 하는 데이터만 선언하면 된다.
    var page: String,
    @SerializedName("per_page")
    var perPage: String,
    var total: String,
    @SerializedName("total_pages")
    var totalPages: String,
    var data: List<User>?
)