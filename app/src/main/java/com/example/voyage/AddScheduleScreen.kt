package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddScheduleScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule_screen)

        //객체 생성
        val submitSchedule: Button = findViewById(R.id.btn_send_schedule)
        val content: EditText? = findViewById(R.id.content_edt)
        val color: EditText? = findViewById(R.id.color_edt)
        val memo: EditText? = findViewById(R.id.memo_edt)
        val sendFragment: Button = findViewById(R.id.btn_send_fragment)

        //fragment로 보내기
        sendFragment.setOnClickListener {
            val bundle: Bundle = Bundle()
            val message: String = color?.text.toString()
            //데이터 담기
            bundle.putString("message", message)
            //fragment 선언
            val fragment = ColorPalette()
            //fragment에 데이터 넘기기
            fragment.arguments = bundle
            //fragment 추가, 변경, 삭제 기능
            val manager: FragmentManager = supportFragmentManager
            //fragment 화면 보여주기
            fragment.show(manager, fragment.tag)
        }

        //timePicker 관련 객체
        var endAt: TimePicker = findViewById(R.id.tp_endAt)
        var endTime: String = ""

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            endTime = "${hourOfDay}:${minute}"
        }

        //화면 전환
        submitSchedule.setOnClickListener {
            val intent = Intent()//this, MainActivity :: class.java)
            intent.putExtra("content", content?.text.toString())
            intent.putExtra("color", color?.text.toString())
            intent.putExtra("memo", memo?.text.toString())
            intent.putExtra("endTime", endTime)

            setResult(Activity.RESULT_OK, intent)

            finish()
        }
    }
}