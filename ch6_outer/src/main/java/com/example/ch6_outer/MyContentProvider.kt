package com.example.ch6_outer

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class MyContentProvider : ContentProvider() {

    init {
        Log.d("jay", "MyContentProvider 생성자 호출")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        // 컬럼명을 정의한 커서 선언
        val cursor = MatrixCursor(arrayOf("id", "data"))
        // 전달할 데이터 추가
        cursor.addRow(arrayOf("1", "hello"))
        cursor.addRow(arrayOf("2", "world"))
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}