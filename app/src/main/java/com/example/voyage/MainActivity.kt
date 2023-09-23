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
import androidx.core.content.ContextCompat
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
import retrofit2.http.PUT
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
var scheduleId : String = ""
var indexOfSchedule : Int = 0
const val REQUEST_CODE = 200
const val REQUEST_CODE_EDIT = 123

class MainActivity : AppCompatActivity() {

    val api = testInterface.create()

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
            Log.d("ASL", "$scheduleList")
        }

        //CalendarView 일정이 있는 날 표시
        if (scheduleList.size > 0) {
            calendarView.setSelectedDateVerticalBar(getDrawable(R.drawable.round_icon))
        }

        //화면 변환
        val add: Button = findViewById(R.id.btn_add)
        add.setOnClickListener{
            val intent = Intent(this, AddScheduleScreen :: class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
        //일정 클릭 이벤트(PUT&DELETE)
        rv_adapter.setItemClickListener(object: MainRvAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                CallApiThread().start()
                Log.d("clicked", "${CallApiThread().id}")
                Log.d("clicked", "$indexOfSchedule")
                goToEditScheduleScreen()
            }
        })
    }

    // onCreate() 이후에 작동
    override fun onStart() {
        super.onStart()
        CallApiThread().start()
        Log.d("onLaunch", "onStart success")
    }

    fun goToEditScheduleScreen() {
        val intent = Intent(this, EditScheduleScreen :: class.java)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
    }

    //화면 전환할 때 데이터 받아 오는 함수..
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE -> {
                    //받아온 editText 값들의 text 를 추출 해서 get 어쩌고에 다 넣어줌
                    val getContent = data?.getStringExtra("content")
                    val getColor = data?.getStringExtra("color")
                    val getMemo = data?.getStringExtra("memo")
                    val getEndTime = data?.getStringExtra("endTime")

                    //서버에 데이터 저장(get)
                    api.getSchedule(s_day).enqueue(object: Callback<GetResponse> {
                        override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                            if(response.isSuccessful()) {
                                Log.d("log", "get: $response")
                                Log.d("log", "get: " + response.body().toString())
                            }
                        }
                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                            Log.d("log", "get: fail to save data")
                        }
                    })
                    //post
                    val data = PostModel(getContent.toString(), getColor.toString(),
                            getMemo.toString(), s_day, getEndTime.toString())

                    api.postSchedule(data).enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("log", "post: $response")
                            Log.d("log", "post: " + response.body().toString())
                            CallApiThread().start()
                        }
                        override fun onFailure(call: Call<PostModel>, t: Throwable) {
                            Log.d("log", "post: fail to save data")
                        }
                    })
                }
                //put으로 main화면에 돌아왔을 때
                REQUEST_CODE_EDIT -> {
                    val getContent = data?.getStringExtra("content")
                    val getColor = data?.getStringExtra("color")
                    val getMemo = data?.getStringExtra("memo")
                    val getEndTime = data?.getStringExtra("endTime")

                    val data = PostModel(getContent.toString(), getColor.toString(),
                        getMemo.toString(), s_day, getEndTime.toString())

                    //put
                    api.editSchedule("64240d2c20a07443f9de31fc", scheduleId, data)
                        .enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("API_PUT", "put: " + response.toString())
                            Log.d("API_PUT", "put: " + response.body().toString())
                            Log.d("API_PUT", "put: schedule edited successfully")
                            CallApiThread().start()
                        }
                        override fun onFailure(call: Call<PostModel>, t: Throwable) {
                            Log.d("API_PUT", "put: fail to edit")
                        }
                    })
                }
            }
        }
    }

    //api 호출
    open inner class CallApiThread : Thread() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun run() {
            val testDate = s_day
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
            val jsonArray : JSONArray = root.optJSONArray("data")
            //api에서 data부분 내용을 가져옴
            var jsonObject : JSONObject
            //일정의 id를 가져오는 함수
            fun getId() {
                if (jsonArray.length() != 0) {
                    val jso : JSONObject = jsonArray.getJSONObject(indexOfSchedule)
                    Log.d("getId", "$indexOfSchedule")
                    val id : String = jso.getString("id")
                    scheduleId = id
                    indexOfSchedule = 0
                }
            }

            Log.d(Companion.TAG, "root: $root")
            Log.d(Companion.TAG, "code: ${root.get("code")}")
            Log.d(Companion.TAG, "data: ${root.get("data")}")

            //Ui 접근 가능하게 함 -> 메인스레드에서 동작(=CallApiThread)
            runOnUiThread {
                //scheduleList 초기화
                scheduleList.clear()
                getId()
                //api 일정 불러 오기
                for (index in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(index)

                    val apiContent = jsonObject.getString("content")
                    val apiColor = jsonObject.getString("color")
                    val apiMemo = jsonObject.getString("memo")
                    val apiEndDate = jsonObject.getString("endDate")
                    val apiEndTime = jsonObject.getString("endTime")

                    val group = AddSchedule(apiContent, apiColor, apiMemo, apiEndDate, apiEndTime)

                    //불러온 일정 scheduleList에 저장
                    scheduleList.add(group)
                }
                rv_adapter.notifyDataSetChanged()   //전체 새로고침
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

        @PUT("schedule?")
        fun editSchedule(
            @Query("personalCalenderId") personalCalenderId: String,
            @Query("personalScheduleId") personalScheduleId: String,
            @Body postModel: PostModel
        ): Call<PostModel>

        @DELETE("schedule/{personalCalenderId}/{personalScheduleId}")
        fun deleteSchedule(
            @Path("personalCalenderId") personalCalenderId: String,
            @Path("personalScheduleId") personalScheduleId: String
        ): Call<Void>

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