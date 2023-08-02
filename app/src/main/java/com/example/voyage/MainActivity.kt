@file:Suppress("DEPRECATION")

package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PostProcessor
import android.os.Build
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.voyage.MainActivity.RetrofitClass.api
import com.example.voyage.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

var s_day : String = ""
var scheduleList = ArrayList<AddSchedule>()
val rv_adapter = MainRvAdapter(scheduleList)

class MainActivity : AppCompatActivity() {

//    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val api = testInterface.create()

    val REQUEST_CODE = 200

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
        s_day = dateFormat.format(date)

        //날짜 텍스트뷰에 담기
        dayText.text = dateFormat.format(date)

        //CalendarView 날짜 변환 이벤트
        calendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            //날짜 변수에 담기
            s_day = "${year}-%02d-%02d".format(month + 1, dayOfMonth)
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
                tv_endAt?.text.toString(),
                s_day
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

                    // 서버에 저장할 데이터
//                    var callGetSchedule = RetrofitClass.api.getSchedule(s_day)

                    // 서버에 데이터 저장(근데 서버에 저장이 안됨)
                    api.getSchedule(s_day).enqueue(object: Callback<GetResponse> {
                        override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                            if(response.isSuccessful()) {

                                Log.d("log", "get: " + response.toString())
                                Log.d("log", "get: " + response.body().toString())
//                                Toast.makeText(this@MainActivity, "data saved", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                            Log.d("log", "get: fail to save data")
//                            Toast.makeText(this@MainActivity, "fail to save data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    )

                    val data =
                        PostModel(getTitle.toString(), getContent.toString(), getMemo.toString(), s_day, getEndTime.toString())

                    api.postSchedule(data).enqueue(object : Callback<PostModel> {
                        override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                            Log.d("log", "post: " + response.toString())
                            Log.d("log", "post: " + response.body().toString())
//                            Toast.makeText(this@MainActivity, "data saved", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<PostModel>, t: Throwable) {
                            Log.d("log", "post: fail to save data")
                        }
                    })
                }
            }
        }
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

        }
    }

    // 여기서부터는 서버에 데이터 저장하는거 구현
    // retrofit 설정
//    object RetrofitClass {
//        private val retrofit : Retrofit by lazy {
//            Retrofit.Builder()
//            .baseUrl(API_PERSONAL_SCHEDULE_BASE_URL + "/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        }
//        private val _api = retrofit.create(testInterface::class.java)
//            val api
//            get() = _api
//    }

    // 인터페이스
    interface testInterface {
        @GET("schedule/endAt?ownerId=64240be120a07443f9de31f7")
        fun getSchedule(
            //Query: url부분에 추가되는 부분
            @Query("date") date: String,
        ): Call<GetResponse>

        @POST("64240d2c20a07443f9de31fc")
        fun postSchedule(
            @Body jsonparams: PostModel,
//            @Query("date") date: String = s_day
        ): Call<PostModel>

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