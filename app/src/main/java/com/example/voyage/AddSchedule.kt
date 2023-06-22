package com.example.voyage

import android.widget.EditText
import android.widget.TimePicker

//추가할 일정 모델
data class AddSchedule(var title: String, var content: String, var memo: String, var endTime: String)