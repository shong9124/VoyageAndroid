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

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(CustomMultipleDotSpan(7f, Color.parseColor("303f9f")))
    }
}

class RedDecorate(dates: Collection<CalendarDay>) : DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#d32f2f")))
        Log.d("DecoDates", "$dates")
    }
}

class DeepBlueDecorate(dates: Collection<CalendarDay>) : DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#303f9f")))
    }
}

class CustomMultipleDotSpan : LineBackgroundSpan {
    private val radius: Float
    private var color: Int

    constructor() {
        this.radius = DEFAULT_RADIUS
        this.color = Color.parseColor("303f9f")
    }
    constructor(color: Int) {
        this.radius = DEFAULT_RADIUS
        this.color = Color.parseColor("303f9f")
    }
    constructor(radius: Float) {
        this.radius = radius
        this.color = Color.parseColor("303f9f")
    }
    constructor(radius: Float, color: Int) {
        this.radius = radius
        this.color = color
    }

    override fun drawBackground(canvas: Canvas, paint: Paint, left: Int, right: Int,
                                top: Int, baseline: Int, bottom: Int, text: CharSequence,
                                start: Int, end: Int, lineNumber: Int) {
        val total = if (scheduleList.size > 2) 3 else scheduleList.size
        var leftMost = (total - 1) * -12

        for (i in 0 until total) {
            val oldColor = paint.color
            canvas.drawCircle(((left + right) / 2 - leftMost).toFloat(), bottom + radius, radius, paint)
            paint.color = oldColor
            leftMost += 24
        }
    }
}