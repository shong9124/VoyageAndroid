package com.example.voyage

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.prolificinteractive.materialcalendarview.spans.DotSpan.DEFAULT_RADIUS

class EventDecorator(dates: Collection<CalendarDay>) : DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)
    private lateinit var colors : IntArray
    private var colorList = ArrayList<Int>()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        colorList.add(Color.parseColor("#d32f2f"))
//        colorList.add(R.color.orange)
//        colorList.add(R.color.yellow_300)
//        colorList.add(R.color.green_200)
//        colorList.add(R.color.green_700)
//        colorList.add(R.color.blue_200)
//        colorList.add(R.color.blue_700)
//        colorList.add(R.color.brown)
//        colorList.add(R.color.gray)
        colors = IntArray(colorList.size)

        for (i in colorList.indices) {
            colors[i] = colorList[i]
        }
        view?.addSpan(CustomMultipleDotSpan(7f, colors))
        Log.d("color", "$colorList")
//        view?.addSpan(CustomMultipleDotSpan(7f, Color.parseColor("303f9f")))
    }
}

class CustomMultipleDotSpan : LineBackgroundSpan {
    private val radius: Float
    private var color = IntArray(0)

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

    override fun drawBackground(canvas: Canvas, paint: Paint, left: Int, right: Int,
                                top: Int, baseline: Int, bottom: Int, text: CharSequence,
                                start: Int, end: Int, lineNumber: Int) {
        val total = if (color.size > 2) 3 else color.size
        var leftMost = (total - 1) * -12

        for (i in 0 until total) {
            val oldColor = paint.color
            canvas.drawCircle(((left + right) / 2 - leftMost).toFloat(), bottom + radius, radius, paint)
            paint.color = oldColor
            leftMost += 24      //점과 점 사이 공간
        }
    }
}