@file:Suppress("DEPRECATION")

package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
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
    var scheduleList = ArrayList<AddSchedule>()
    val rv_adapter = MainRvAdapter(scheduleList)
    var s_day : String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //날짜 관련 객체 생성
        val dayText: TextView = findViewById(R.id.day_text)
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        //날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        //date 타입(오늘 날짜)
        val date: Date = Date(calendarView.date)

        //날짜 텍스트뷰에 담기
        dayText.text = dateFormat.format(date)

        //CalendarView 날짜 변환 이벤트
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            //날짜 변수에 담기
            s_day = "${year}-%02d-${dayOfMonth}".format(month + 1)
            //변수 텍스트뷰에 담기
            dayText.text = s_day

            //api 호출
            CallApiThread().start()
        }

        //일정 추가 관련 객체
        var title: EditText? = findViewById(R.id.title_edt)
        var content: EditText? = findViewById(R.id.content_edt)
        var memo: EditText? = findViewById(R.id.memo_edt)
        var tv_endAt: TextView? = findViewById(R.id.tv_endAt)
        val rv_schedule: RecyclerView = findViewById(R.id.rv_schedule)

        //화면 변환
        val add: Button = findViewById(R.id.btn_add)
        add.setOnClickListener{
            val intent = Intent(this, AddScheduleScreen :: class.java)

            //일정 추가
            scheduleList.add(AddSchedule(
                title?.text.toString(),
                content?.text.toString(),
                memo?.text.toString(),
                tv_endAt?.text.toString()
            ))

            rv_schedule.adapter = rv_adapter
            rv_schedule.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false)

            startActivityForResult(intent, REQUEST_CODE)
        }

    }

    //화면 전환할 때 데이터 받아 오는 함수..
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            Log.d("MDM", "In onActivityResult")

            when(requestCode) {
                REQUEST_CODE -> {

                    //받아온 editText 값들의 text를 추출해서 get어쩌고에 다 넣어줌
                    var getTitle = data?.getStringExtra("title")
                    var getContent = data?.getStringExtra("content")
                    var getMemo = data?.getStringExtra("memo")
                    var getEndTime = data?.getStringExtra("endTime")

                    //입력값이 없을 경우
                    if (getTitle == null) {
                        var title_tv : TextView = findViewById(R.id.tv_title)
                        title_tv.visibility = View.INVISIBLE
                    }
                    if (getContent == null) {
                        var content_tv : TextView = findViewById(R.id.tv_content)
                        content_tv.visibility = View.INVISIBLE
                    }
                    if (getMemo == null) {
                        var memo_tv : TextView = findViewById(R.id.tv_memo)
                        memo_tv.visibility = View.INVISIBLE
                    }
                    //시간 변경값이 없을 경우
                    if (getEndTime == null) {
                        var endAt : TextView = findViewById(R.id.tv_endAt)
                        endAt.visibility = View.INVISIBLE
                    }

                    //scheduleList에 각각 넣어줌
                    scheduleList[scheduleList.size - 1].title = getTitle.toString()
                    scheduleList[scheduleList.size - 1].content = getContent.toString()
                    scheduleList[scheduleList.size - 1].memo = getMemo.toString()
                    scheduleList[scheduleList.size - 1].endTime = getEndTime.toString()

                    rv_adapter.notifyDataSetChanged()   //전체 새로고침
                    //확인
                    Log.d("ASL", "${scheduleList}")
                }
            }
        }
    }

    inner class CallApiThread : Thread() {

        override fun run() {
            var testDate = s_day
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