package com.example.voyage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditScheduleScreen : AppCompatActivity(), CallBack {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_screen)

        //객체 생성
        val editSchedule: Button = findViewById(R.id.btn_edit)
        val deleteSchedule: Button = findViewById(R.id.btn_delete)
        val content: EditText? = findViewById(R.id.content_edt2)
        val colorBtn: Button = findViewById(R.id.btn_palette2)
        val memo: EditText? = findViewById(R.id.memo_edt2)

        //timePicker 관련 객체
        val endAt: TimePicker = findViewById(R.id.tp_endAt2)
        var endTime: String = ""

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            endTime = "${hourOfDay}:${minute}"
        }

        //fragment로 보내기
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
            //fragment 화면 보여주기
            fragment.show(manager, fragment.tag)
        }

        //화면 전환
        editSchedule.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java)
            intent.putExtra("content", content?.text.toString())
            intent.putExtra("color", color)
            intent.putExtra("memo", memo?.text.toString())
            intent.putExtra("endTime", endTime)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        //일정 삭제
        deleteSchedule.setOnClickListener {
            val api = MainActivity().api
            //delete
            api.deleteSchedule("64240d2c20a07443f9de31fc", scheduleId)
                .enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("DELETE", "delete: $response")
                        Toast.makeText(this@EditScheduleScreen,
                            "schedule deleted", Toast.LENGTH_SHORT).show()
                        MainActivity().CallApiThread().start()
                        MainActivity().dotSchedule(s_day)
                        MainActivity().deletePref(MainActivity().changeString(s_day))
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("DELETE", "delete: fail to delete")
                    }
                })
            finish()
        }
    }
    override fun callBackExample(msg: String) {
        val colorBtn : Button = findViewById(R.id.btn_palette2)

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