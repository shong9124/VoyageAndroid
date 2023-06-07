package com.example.voyage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 200

    @SuppressLint("MissingInflatedId")
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

        //화면 변환
        val add: Button = findViewById(R.id.btn_add)
        add.setOnClickListener{
            val intent = Intent(this, AddScheduleScreen :: class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        //일정 관련 객체
        var title: EditText? = findViewById(R.id.title_edt)
        var content: EditText? = findViewById(R.id.content_edt)
        var memo: EditText? = findViewById(R.id.memo_edt)
        val rv_schedule: RecyclerView = findViewById(R.id.rv_schedule)

        //null exception 해결
//        if (title != null) {
//            if(title.getText().toString().length == 0) {
//                title.setText("There's no event")
//            }
//        }
//        if(content.getText().toString().length == 0) {
//            content.setText("No content")
//        }
//        if(memo.getText().toString().length == 0) {
//            memo.setText("No memo")
//        }

        //일정 추가
        var scheduleList = ArrayList<AddSchedule>()

        scheduleList.add(AddSchedule(title.toString(), content.toString(), memo.toString()))

        val rv_adapter = MainRvAdapter(scheduleList)
        rv_adapter.notifyDataSetChanged()
        rv_schedule.adapter = rv_adapter
        rv_schedule.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


//        if(title != null && content != null && memo != null) {
//            var add_schedule = arrayListOf(
//                AddSchedule(title.text.toString(), content.text.toString(), memo.text.toString())
//            )
//            rv_schedule.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            rv_schedule.setHasFixedSize(true)
//            rv_schedule.adapter = MainRvAdapter(add_schedule)
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var title_tv = findViewById<TextView>(R.id.tv_title)
        var content_tv = findViewById<TextView>(R.id.tv_content)
        var memo_tv = findViewById<TextView>(R.id.tv_memo)

        if (requestCode == REQUEST_CODE){

            if (resultCode == RESULT_OK) {

                var getTitle = data?.getStringExtra("title")
                var getContent = data?.getStringExtra("content")
                var getMemo = data?.getStringExtra("memo")

                title_tv.text = getTitle
                content_tv.text = getContent
                memo_tv.text = getMemo

            }
        }
    }

    inner class CallApiThread : Thread() {

        override fun run() {
            var testDate = "2023-05-29"
//                calendarView.setOnDateChangeListener{ calendarView, year, month, dayOfMonth ->
//                "${year}-${month + 1}-${dayOfMonth}"
//            }
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