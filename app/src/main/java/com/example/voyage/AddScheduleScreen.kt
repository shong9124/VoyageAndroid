package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager

var color: String = "BLUE"

class AddScheduleScreen : AppCompatActivity(), CallBack {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule_screen)

        //객체 생성
        val submitSchedule: Button = findViewById(R.id.btn_send_schedule)
        val content: EditText? = findViewById(R.id.content_edt)
        val colorBtn: Button = findViewById(R.id.btn_palette)
        val memo: EditText? = findViewById(R.id.memo_edt)

        //fragment 로 보내기
        colorBtn.setOnClickListener {
            //fragment 선언
            val fragment = ColorPalette()
            //fragment 추가, 변경, 삭제 기능
            val manager: FragmentManager = supportFragmentManager
//            val transaction: FragmentTransaction = manager.beginTransaction()
            //fragment 추가
//            transaction.add(R.id.frameLayout, fragment)
            //적용
//            transaction.commit()
            //fragment 화면 보여 주기
            fragment.show(manager, fragment.tag)
        }

        //timePicker 관련 객체
        val endAt: TimePicker = findViewById(R.id.tp_endAt)
        var endTime = ""

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            endTime = "${hourOfDay}:${minute}"
        }

        //화면 전환
        submitSchedule.setOnClickListener {
            val intent = Intent()//this, MainActivity :: class.java)
            intent.putExtra("content", content?.text.toString())
            intent.putExtra("color", color)
            intent.putExtra("memo", memo?.text.toString())
            intent.putExtra("endTime", endTime)

            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }
    override fun callBackExample(msg: String) {
        val colorBtn : Button = findViewById(R.id.btn_palette)

        if (msg == "RED") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.red))
        }
        if (msg == "ORANGE") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.orange))
        }
        if (msg == "YELLOW") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.yellow_300))
        }
        if (msg == "GREEN") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_200))
        }
        if (msg == "DEEP GREEN") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.green_700))
        }
        if (msg == "BLUE") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.blue_200))
        }
        if (msg == "DEEP BLUE") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.blue_700))
        }
        if (msg == "BROWN") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.brown))
        }
        if (msg == "GRAY") {
            colorBtn.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
        }
    }
}