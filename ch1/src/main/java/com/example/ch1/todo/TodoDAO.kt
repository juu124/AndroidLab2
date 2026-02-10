package com.example.ch1.todo

import android.content.ContentValues
import android.content.Context

// Data Access Object - data 저장, 삭제, 수정, 획득을 하는 함수를 가지고 있는 객체
fun insertTodo(context: Context, values: ContentValues) {
    val db = DBHelper(context).writableDatabase
    db.insert("tb_todo", null, values)
    db.close()
}

fun selectTodos(context: Context): MutableList<Todo> {
    val results = mutableListOf<Todo>()
    val db = DBHelper(context).readableDatabase
    val cursor = db.rawQuery("SELECT * FROM tb_todo ORDER BY date DESC", null)
    while (cursor.moveToNext()) {
        cursor.run {
            val vo = Todo(getInt(0), getString(1), getString(2), getString(3), getInt(4))
            results.add(vo)
        }
    }
    db.close()
    return results
}