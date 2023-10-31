package com.example.voyage

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val prefsFileName = "prefs"
    private val prefs : SharedPreferences = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = prefs.edit()
    //데이터 조회
    fun getString(key: String, str: String) : String {
        return prefs.getString(key, str).toString()
    }
    //데이터 저장
    fun setString(key: String, str: String) {
        editor.putString(key, str)
        editor.apply()
    }
    //데이터 삭제
    fun delete(key: String) {
        editor.remove(key)
        editor.apply()
    }
}