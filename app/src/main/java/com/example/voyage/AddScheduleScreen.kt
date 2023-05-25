package com.example.voyage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddScheduleScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule_screen)

        //객체 생성
        val submitSchedule: Button = findViewById(R.id.btn_send_schedule)
        val title: EditText = findViewById(R.id.title_edt)
        val content: EditText = findViewById(R.id.content_edt)
        val memo: EditText = findViewById(R.id.memo_edt)
        val endAt: TimePicker = findViewById(R.id.tp_endAt)
        val tv_endAt: TextView = findViewById(R.id.tv_endAt)

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            var time: String = "${hourOfDay}시 ${minute}분"
            tv_endAt.text = time
        }


        //화면 전환
        submitSchedule.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java)
//            intent.putExtra("addSchedule", title.text.toString())
            startActivity(intent)
        }
    }
}