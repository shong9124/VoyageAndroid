package com.example.voyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //객체 생성
        val dayText: TextView = findViewById(R.id.day_text)
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        //날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        //date 타입(오늘 날짜)
        val date: Date = Date(calendarView.date)

        //날짜 텍스트뷰에 담기
        dayText.text = dateFormat.format(date)

        //CalendarView 날짜 변환 이벤트
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            CallApiThread().start()
            //날짜 변수에 담기
            var day: String = "${year}년 ${month + 1}월 ${dayOfMonth}일"
            //변수 텍스트뷰에 담기
            dayText.text = day
        }

    }

    inner class CallApiThread : Thread() {
        override fun run() {
            val testDate = "2023-03-29"
            val testUserId = "64240be120a07443f9de31f7"

            val url =
                URL(Companion.API_PERSONAL_SCHEDULE_BASE_URL + "/endAt?ownerId=$testUserId&date=$testDate")
            val conn = url.openConnection()
            val input = conn.getInputStream()
            val isr = InputStreamReader(input)
            val br = BufferedReader(isr)

            var str: String? = null
            val buf = StringBuffer()

            do {
                str = br.readLine()

                if (str != null) {
                    buf.append(str)
                }
            } while (str != null)

            val root = JSONObject(buf.toString())

            Log.d(Companion.TAG, "root: $root")
            Log.d(Companion.TAG, "code: ${root.get("code")}")
            Log.d(Companion.TAG, "data: ${root.get("data")}")
        }
    }

    companion object {
        const val TAG = "DEV"

        const val API_PERSONAL_SCHEDULE_BASE_URL = "http://13.209.38.50:8080/api/personal/schedule"
    }
}

///api/personal/schedule/endAt?ownerId=64240be120a07443f9de31f7&date=2023-03-29