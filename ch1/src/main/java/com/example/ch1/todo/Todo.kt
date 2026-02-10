package com.example.ch1.todo

// to_do data 추상화
data class Todo(
    val id: Int,
    var title: String,
    var content: String,
    var data: String,
    var completed: Int
)