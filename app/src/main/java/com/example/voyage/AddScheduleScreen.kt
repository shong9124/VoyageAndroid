package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime
import java.util.ArrayList

class AddScheduleScreen : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule_screen)

        //객체 생성
        val submitSchedule: Button = findViewById(R.id.btn_send_schedule)
        var title: EditText? = findViewById(R.id.title_edt)
        var content: EditText? = findViewById(R.id.content_edt)
        var memo: EditText? = findViewById(R.id.memo_edt)

        //timePicker 관련 객체
        var endAt: TimePicker = findViewById(R.id.tp_endAt)
        var endTime: String = ""

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            endTime = "End event at ${hourOfDay} : ${minute}"
        }

        //화면 전환
        submitSchedule.setOnClickListener {
            val intent = Intent()//this, MainActivity :: class.java)
            intent.putExtra("title", title?.text.toString())
            intent.putExtra("content", content?.text.toString())
            intent.putExtra("memo", memo?.text.toString())
            intent.putExtra("endTime", endTime)



            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }
}