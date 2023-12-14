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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
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
import java.lang.NullPointerException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

var s_day : String = ""
var sDate : String = ""
var scheduleList = ArrayList<AddSchedule>()
var rv_adapter = MainRvAdapter(scheduleList)
var scheduleId : String = ""
var indexOfSchedule : Int = 0
var sizeOfSchedule : Int = 0
var monthOfDay : Int = 0
var scheduleSize : Int = 0
const val REQUEST_CODE = 200
const val REQUEST_CODE_EDIT = 123

class MainActivity : AppCompatActivity() {

    val api = TestInterface.create()

    @SuppressLint("MissingInflatedId", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("onLaunch", "onCreate success")

        //날짜 관련 객체 생성
        val dayText: TextView = findViewById(R.id.day_text)
        val calendarView: MaterialCalendarView = findViewById(R.id.calendarView)
        val rvSchedule: RecyclerView = findViewById(R.id.rv_schedule)

        //date 타입(오늘 날짜)
        calendarView.setSelectedDate(CalendarDay.today())
        s_day = calendarView.selectedDate?.date.toString()
        //날짜 textView 에 담기
        dayText.text = s_day
        //recyclerView adapter 연결
        rvSchedule.adapter = rv_adapter
        rvSchedule.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)

        //점 초기화
        getSchedule(stringToInt(s_day))

        //CalendarView 달 변환 이벤트
        calendarView.setOnMonthChangedListener(object: OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView, date: CalendarDay) {
                //일정 있으면 점찍기
                calendarView.removeDecorators()
                calendarView.invalidateDecorators()
                getSchedule(date)
            }
        })

        //CalendarView 날짜 변환 이벤트
        calendarView.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                val sYear : Int = date.date.year
                val sMonth : Int = date.month
                val sDay : Int = date.day
                val sDate = "$sYear-%02d-%02d".format(sMonth, sDay)
                //schedule size reset
                if (s_day != sDate) {
                    sizeOfSchedule = 0
                }
                s_day = sDate

                //날짜 textView 에 담기
                dayText.text = s_day
                //api 호출
                CallApiThread().start()
                Log.d("ASL", "$scheduleList")
            }
        })

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

    //일정 존재 유무 확인하고 점찍기
    fun getSchedule(date: CalendarDay) {
        getMonthOfDays(date.month)
        for (i in 0 until monthOfDay) {
            val sYear: Int = date.date.year
            val sMonth: Int = date.month
            val sDay: Int = 1 + i
            sDate = "$sYear-%02d-%02d".format(sMonth, sDay)
            Log.d("sDate", sDate)
            Log.d("PREFS", App.prefs.getString(changeString(sDate), ""))

            if (App.prefs.getString(changeString(sDate), "") != "") {
                dotSchedule(sDate)
            }
        }
    }

    //변수로 받는 날짜에 점 찍어 주는 함수
    fun dotSchedule(day: String) {
        try {
            val calendarView : MaterialCalendarView = findViewById(R.id.calendarView)
            calendarView.addDecorator(EventDecorator(Collections.singleton(stringToInt(day))))
        }
        catch (e: NullPointerException) {
            return
        }
    }

    fun deletePref(day: String) {
        App.prefs.delete(changeString(day))
    }

    // onCreate() 이후에 작동
    override fun onStart() {
        super.onStart()
        CallApiThread().start()
        getSchedule(stringToInt(s_day))
        Log.d("onLaunch", "onStart success")
    }

    fun goToEditScheduleScreen() {
        val intent = Intent(this, EditScheduleScreen :: class.java)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
    }

    //화면 전환할 때 데이터 받아 오는 함수..
    @Deprecated("Deprecated in Java")
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
                            if(response.isSuccessful) {
                                Log.d("log", "get: $response")
                                Log.d("log", "get: " + response.body().toString())
                            }
                        }
                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                            Log.d("log", "get: fail to save data")
                        }
                    })
                    //post
                    val scheduleData = PostModel(getContent.toString(), getColor.toString(),
                            getMemo.toString(), s_day, getEndTime.toString())

                    api.postSchedule(scheduleData).enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("log", "post: $response")
                            Log.d("log", "post: " + response.body().toString())
                            CallApiThread().start()
                            if (response.code() == 200) {
                                //SharedPreferences
                                App.prefs.setString(changeString(s_day), "$sizeOfSchedule")
                                Log.d("size", "$sizeOfSchedule")
                                dotSchedule(s_day)
                                //schedule size
                                sizeOfSchedule += 1
                            }
                        }
                        override fun onFailure(call: Call<PostModel>, t: Throwable) {
                            Log.d("log", "post: fail to save data")
                        }
                    })
                }
                //put으로 main화면에 돌아 왔을 때
                REQUEST_CODE_EDIT -> {
                    val getContent = data?.getStringExtra("content")
                    val getColor = data?.getStringExtra("color")
                    val getMemo = data?.getStringExtra("memo")
                    val getEndTime = data?.getStringExtra("endTime")

                    val scheduleData = PostModel(getContent.toString(), getColor.toString(),
                        getMemo.toString(), s_day, getEndTime.toString())

                    //put
                    api.editSchedule("64240d2c20a07443f9de31fc", scheduleId, scheduleData)
                        .enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("API_PUT", "put: $response")
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

    fun getMonthOfDays(month: Int) {
        if (month == 1) monthOfDay = 31
        if (month == 2) monthOfDay = 28
        if (month == 3) monthOfDay = 31
        if (month == 4) monthOfDay = 30
        if (month == 5) monthOfDay = 31
        if (month == 6) monthOfDay = 30
        if (month == 7) monthOfDay = 31
        if (month == 8) monthOfDay = 31
        if (month == 9) monthOfDay = 30
        if (month == 10) monthOfDay = 31
        if (month == 11) monthOfDay = 30
        if (month == 12) monthOfDay = 31
    }

    fun stringToInt(str: String): CalendarDay {
        val stringYear = str.substring(0, 4)
        val intYear = stringYear.toInt()
        val stringMonth = str.substring(5, 7)
        val intMonth = stringMonth.toInt()
        val stringDay = str.substring(8, 10)
        val intDay = stringDay.toInt()

        return CalendarDay.from(intYear, intMonth, intDay)
    }

    fun changeString(str: String): String {
        val year = str.substring(0, 4)
        val month = str.substring(5, 7)
        val day = str.substring(8, 10)

        return "$year$month$day"
    }

    //api 호출
    open inner class CallApiThread : Thread() {
        @SuppressLint("NotifyDataSetChanged")
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

            //count schedule
            scheduleSize = jsonArray.length()
            sizeOfSchedule = scheduleSize

            //api에서 data부분 내용을 가져옴
            var jsonObject : JSONObject
            //일정의 id를 가져 오는 함수
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

            //Ui 접근 가능 하게 함 -> MainThread 에서 동작(=CallApiThread)
            runOnUiThread {
                //scheduleList 초기화
                scheduleList.clear()
                getId()
                //api 추가한 일정 불러 오기
                for (index in 0 until jsonArray.length()) {
                    jsonObject = jsonArray.getJSONObject(index)

                    val apiContent = jsonObject.getString("content")
                    val apiColor = jsonObject.getString("color")
                    val apiMemo = jsonObject.getString("memo")
                    val apiEndDate = jsonObject.getString("endDate")
                    val apiEndTime = jsonObject.getString("endTime")

                    //sharedPrefs
                    App.prefs.setString("content", apiContent)
                    App.prefs.setString("color", apiColor)
                    App.prefs.setString("memo", apiMemo)

                    val group = AddSchedule(apiContent, apiColor, apiMemo, apiEndDate, apiEndTime)
                    //불러온 일정 scheduleList에 저장
                    scheduleList.add(group)
                }
                rv_adapter.notifyDataSetChanged()   //전체 새로 고침
                //점 삭제
                if (jsonArray.length() == 0) {
                    deletePref(s_day)
                    if (App.prefs.getString(changeString(s_day), "") == "") {
                        try {
                            val calendarView : MaterialCalendarView = findViewById(R.id.calendarView)
                            calendarView.removeDecorators()
                            calendarView.invalidateDecorators()
                        }
                        catch (e: NullPointerException) {
                            Log.d("ERROR", "NullPointerException")
                        }
//                        getSchedule(stringToInt(s_day))
                    }
                }
            }
        }
    }

    // interface
    interface TestInterface {
        @GET("schedule/endAt?ownerId=64240be120a07443f9de31f7")
        fun getSchedule(
            //Query: url 부분에 추가 되는 부분
            @Query("date") date: String
        ): Call<GetResponse>

        @POST("64240d2c20a07443f9de31fc")
        fun postSchedule(
            @Body jsonParams: PostModel,
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
            fun create(): TestInterface {

                val gson: Gson = GsonBuilder().setLenient().create()

                return Retrofit.Builder()
                    .baseUrl(API_SCHEDULE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TestInterface::class.java)
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