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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.ArrayList

class EditScheduleScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_screen)

        //객체 생성
        val editSchedule: Button = findViewById(R.id.btn_edit)
        val deleteSchedule: Button = findViewById(R.id.btn_delete)
        var content: EditText? = findViewById(R.id.content_edt2)
        var color: EditText? = findViewById(R.id.color_edt2)
        var memo: EditText? = findViewById(R.id.memo_edt2)

        //timePicker 관련 객체
        var endAt: TimePicker = findViewById(R.id.tp_endAt2)
        var endTime: String = ""

        //timePicker 값 변경 이벤트
        endAt.setOnTimeChangedListener{ endAt, hourOfDay, minute ->
            endTime = "${hourOfDay}:${minute}"
        }

        content?.setText(scheduleList[MainRvAdapter(scheduleList).itemCount - 1].content)
        color?.setText(scheduleList[MainRvAdapter(scheduleList).itemCount - 1].color)
        memo?.setText(scheduleList[MainRvAdapter(scheduleList).itemCount - 1].memo)

        //화면 전환
        editSchedule.setOnClickListener {
            val intent = Intent(this, MainActivity :: class.java)
            intent.putExtra("content", content?.text.toString())
            intent.putExtra("color", color?.text.toString())
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
                        Log.d("DELETE", "delete: " + response.toString())
                        Toast.makeText(this@EditScheduleScreen,
                            "schedule deleted", Toast.LENGTH_SHORT).show()
                        MainActivity().CallApiThread().start()
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("DELETE", "delete: fail to delete")
                    }
                })
            finish()
        }
    }
}