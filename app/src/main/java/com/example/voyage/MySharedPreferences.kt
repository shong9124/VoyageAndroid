package com.example.voyage

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val prefsFileName = "prefs"
    private val prefs : SharedPreferences = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
    //데이터 조회
    fun getString(key: String, str: String) : String {
        return prefs.getString(key, str).toString()
    }
    //데이터 저장
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
    //데이터 삭제
    fun delete(key: String) {
        prefs.edit().remove(key).apply()
    }
}