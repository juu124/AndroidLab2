package com.example.ch1.todo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "tododb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
           CREATE TABLE tb_todo (
               _id INTEGER PRIMARY KEY AUTOINCREMENT,
               title,
               content,
               date,
               completed
           ) 
        """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE tb_todo")
        onCreate(db)
    }
}