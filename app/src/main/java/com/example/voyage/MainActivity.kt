@file:Suppress("DEPRECATION")

package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var s_day : String = ""
var scheduleList = ArrayList<AddSchedule>()
var rv_adapter = MainRvAdapter(scheduleList)

class MainActivity : AppCompatActivity() {

    val api = testInterface.create()

    val REQUEST_CODE = 200

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("onLaunch", "onCreate success")

        //날짜 관련 객체 생성
        val dayText: TextView = findViewById(R.id.day_text)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val rv_schedule: RecyclerView = findViewById(R.id.rv_schedule)

        //날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        //date 타입(오늘 날짜)
        val date: Date = Date(calendarView.date)
        s_day = dateFormat.format(date)

        //날짜 텍스트뷰에 담기
        dayText.text = dateFormat.format(date)

        rv_schedule.adapter = rv_adapter
        rv_schedule.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)

        //CalendarView 날짜 변환 이벤트
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            //날짜 변수에 담기
            s_day = "${year}-%02d-%02d".format(month + 1, dayOfMonth)
            //변수 텍스트뷰에 담기
            dayText.text = s_day

            //api 호출
            CallApiThread().start()
            Log.d("ASL", "${scheduleList}")
        }

        //화면 변환
        val add: Button = findViewById(R.id.btn_add)
        add.setOnClickListener{
            val intent = Intent(this, AddScheduleScreen :: class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    // onCreate() 이후에 작동
    override fun onStart() {
        super.onStart()
        CallApiThread().start()
        Log.d("onLaunch", "onStart success")
    }

    //화면 전환할 때 데이터 받아 오는 함수..
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when(requestCode) {
                REQUEST_CODE -> {

                    //받아온 editText 값들의 text를 추출해서 get어쩌고에 다 넣어줌
                    var getContent = data?.getStringExtra("content")
                    var getColor = data?.getStringExtra("color")
                    var getMemo = data?.getStringExtra("memo")
                    var getEndTime = data?.getStringExtra("endTime")

                    //입력값이 없을 경우
                    if (getContent == null) {
                        var content_tv : TextView = findViewById(R.id.tv_content)
                        content_tv.visibility = View.INVISIBLE
                    }
                    if (getColor == null) {
                        var color_tv : TextView = findViewById(R.id.tv_color)
                        color_tv.visibility = View.INVISIBLE
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

                    //서버에 데이터 저장(get)
                    api.getSchedule(s_day).enqueue(object: Callback<GetResponse> {
                        override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                            if(response.isSuccessful()) {
                                Log.d("log", "get: " + response.toString())
                                Log.d("log", "get: " + response.body().toString())
                            }
                        }
                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                            Log.d("log", "get: fail to save data")
                        }
                    })

                    //post
                    val data =
                        PostModel(getContent.toString(), getColor.toString(),
                            getMemo.toString(), s_day, getEndTime.toString())

                    api.postSchedule(data).enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("log", "post: " + response.toString())
                            Log.d("log", "post: " + response.body().toString())
                        }
                        override fun onFailure(call: Call<PostModel>, t: Throwable) {
                            Log.d("log", "post: fail to save data")
                        }
                    })

                    Log.d("onLaunch", "In onActivityResult")

                    //delete(에러&보류)
//                    api.deleteSchedule(s_day, "test").enqueue(object: Callback<GetResponse> {
//                        override fun onResponse(
//                            call: Call<GetResponse>,
//                            response: Response<GetResponse>) {
//                            Log.d("log", "deleted")
//                        }
//                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
//                            Log.d("log", "fail to delete")
//                        }
//                    })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        CallApiThread().start()
        Log.d("onLaunch", "onResume successㅎ")
    }

    //api 호출
    inner class CallApiThread : Thread() {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            var testDate = s_day
            val testUserId = "64240be120a07443f9de31f7"

            var url =
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

            //Ui 접근 가능하게 함 -> 메인스레드에서 동작
            runOnUiThread {
                //api에서 data부분 내용을 가져옴
                val jsonArray : JSONArray = root.optJSONArray("data")
                var jsonObject : JSONObject
                //scheduleList 초기화
                scheduleList.clear()
                //api 일정 불러오기
                for (index in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(index)

                    val apiContent = jsonObject.getString("content")
                    val apiColor = jsonObject.getString("color")
                    val apiMemo = jsonObject.getString("memo")
                    val apiEndDate = jsonObject.getString("endDate")
                    val apiEndTime = jsonObject.getString("endTime")

                    var group = AddSchedule(apiContent, apiColor, apiMemo, apiEndDate, apiEndTime)
                    //불러온 일정 scheduleList에 저장
                    scheduleList.add(group)
                }
                rv_adapter.notifyDataSetChanged()   //전체 새로고침
                Log.d("onLaunch", "CallApiThread")
            }
        }
    }

    // 인터페이스
    interface testInterface {
        @GET("schedule/endAt?ownerId=64240be120a07443f9de31f7")
        fun getSchedule(
            //Query: url부분에 추가되는 부분
            @Query("date") date: String
        ): Call<GetResponse>

        @POST("64240d2c20a07443f9de31fc")
        fun postSchedule(
            @Body jsonparams: PostModel,
        ): Call<PostModel>

        @DELETE("schedule/endAt?ownerId=64240be120a07443f9de31f7")
        fun deleteSchedule(
            @Query("date") date: String,
            @Path("content") content: String
        )

        companion object {
            fun create(): testInterface {

                val gson: Gson = GsonBuilder().setLenient().create();

                return Retrofit.Builder()
                    .baseUrl(API_SCHEDULE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(testInterface::class.java)
            }
        }
    }

    companion object {
        const val TAG = "DEV"

        const val API_PERSONAL_SCHEDULE_BASE_URL = "http://13.209.38.50:8080/api/personal/schedule"
        const val API_SCHEDULE_URL = "http://13.209.38.50:8080/api/personal/"
    }
}

///api/personal/schedule/endAt?ownerId=64240be120a07443f9de31f7&date=2023-03-29