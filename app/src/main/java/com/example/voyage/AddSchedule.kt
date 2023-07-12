package com.example.voyage

import android.widget.EditText
import android.widget.TimePicker
import com.google.gson.annotations.SerializedName

data class TestResponse(var schedule: ArrayList<AddSchedule>)

//추가할 일정 모델
data class AddSchedule(
    var title: String,
    var content: String,
    var memo: String,
    @SerializedName("end_time")
    var endTime: String,
    var date: String
    )