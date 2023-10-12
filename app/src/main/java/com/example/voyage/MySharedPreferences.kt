package com.example.voyage

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val prefsFileName = "prefs"
    private val prefsKeyEdt = "Date"
    private val prefs : SharedPreferences = context.getSharedPreferences(prefsFileName, 0)

    fun getString(key: String, str: String) : String {
        return prefs.getString(prefsKeyEdt, str).toString()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(prefsKeyEdt, str).apply()
    }
}