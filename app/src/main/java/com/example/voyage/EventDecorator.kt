package com.example.voyage

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import android.util.Log
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS

class EventDecorator(dates: Collection<CalendarDay>) : DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)
    private lateinit var colors : IntArray
    var colorList
    = arrayListOf<Int>(R.color.red)
//        , R.color.orange, R.color.yellow_300, R.color.green_200,
//        R.color.green_700, R.color.blue_200, R.color.blue_700, R.color.brown, R.color.gray)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        colors = IntArray(colorList.size)

        for (i in colorList.indices) {
            colors[i] = colorList[i]
            Log.d("colors", "${colors[i]}")
        }
        view?.addSpan(CustomMultipleDotSpan(7f, colors))
//        Log.d("color", "$colors")
//        view?.addSpan(CustomMultipleDotSpan(7f, Color.parseColor("303f9f")))
    }
}

class CustomMultipleDotSpan : LineBackgroundSpan {
    private val radius: Float
    private var color = IntArray(0)
    private var dotColor = 0

    constructor() {
        this.radius = DEFAULT_RADIUS
        this.color[0] = 0
    }
    constructor(color: Int) {
        this.radius = DEFAULT_RADIUS
        this.color[0] = 0
    }
    constructor(radius: Float) {
        this.radius = radius
        this.color[0] = 0
    }
    constructor(radius: Float, color: IntArray) {
        this.radius = radius
        this.color = color
    }

    @SuppressLint("ResourceAsColor")
    override fun drawBackground(canvas: Canvas, paint: Paint, left: Int, right: Int,
                                top: Int, baseline: Int, bottom: Int, text: CharSequence,
                                start: Int, end: Int, lineNumber: Int) {

        val total = if (color.size > 2) 3 else color.size
        var leftMost = (total - 1) * -12

        for (i in 0 until total) {
            val oldColor = paint.color
            if (color[i] != 0) {
                paint.color = color[i]
            }
            canvas.drawCircle(((left + right) / 2 - leftMost).toFloat(),
                bottom + radius, radius, paint)
            paint.color = oldColor
            leftMost += 24      //점과 점 사이 공간
        }

//        for (i in 0 until monthOfDay) {
//            val calDay = MainActivity().stringToInt(s_day)
//            val sYear: Int = calDay.date.year
//            val sMonth: Int = calDay.month
//            val sDay: Int = 1 + i
//            val strDay = "$sYear$sMonth$sDay"
//
//            if (App.prefs.getString(strDay, "") != "") {
//                val total = if (App.prefs.getString(strDay, "") == "1") 1
//                else App.prefs.getString(strDay, "").toInt()
//                Log.d("total", "$total")
//                var leftMost = (total - 1) * -12
//                //점찍기
//                for (k in 0 until total) {
//                    val oldColor = paint.color
//                    if(color[k] != 0) {
//                        paint.color = color[k]
//                    }
//                    canvas.drawCircle(((left + right) / 2 - leftMost).toFloat(), bottom + radius, radius, paint)
//                    paint.color = oldColor
//                    leftMost += 24      //점과 점 사이 공간
//                }
//            }
//        }
    }
//    override fun callBackExample(msg: String) {
//        if (msg == "RED") dotColor = color[0]
//    }
}