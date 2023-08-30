package com.example.voyage

import android.widget.EditText
import android.widget.TimePicker
import com.google.gson.annotations.SerializedName

data class GetResponse(
    var code: String? = null,
    var codeMsg: String? = null,
    var data: ArrayList<AddSchedule>? = null
    )

//추가할 일정 모델
data class AddSchedule(
    var content: String,
    var color: String,
    var memo: String,
    var endDate: String,
    var endTime: String
)

data class PostModel(
    var content: String,
    var color: String,
    var memo: String,
    var endDate: String,
    var endTime: String,
    val remindTime: String? = null,
    val remindDate: String? = null
)