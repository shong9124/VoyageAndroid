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
    var title: String,
    var content: String,
    var memo: String,
    @SerializedName("end_time")
    var endTime: String,
    var date: String
    )

data class PostModel(
    var Content: String,
    var Color: String? = null,
    var Memo: String,
    var EndDate: String,
    var EndTime: String,
    val remindTime: String? = null,
    val remindDate: String? = null
)

data class PostResult(
    var data : PostModel
)