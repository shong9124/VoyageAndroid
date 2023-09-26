package com.example.voyage

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(dates: Collection<CalendarDay>) : DayViewDecorator {
    var dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        if (scheduleList[0].color == "RED")
            view?.addSpan(DotSpan(5F, Color.parseColor("#d32f2f")))
        if (scheduleList[0].color == "ORANGE")
            view?.addSpan(DotSpan(5F, Color.parseColor("#f57c00")))
        if (scheduleList[0].color == "YELLOW")
            view?.addSpan(DotSpan(5F, Color.parseColor("#dfbc02")))
        if (scheduleList[0].color == "GREEN")
            view?.addSpan(DotSpan(5F, Color.parseColor("#689f38")))
        if (scheduleList[0].color == "DEEP GREEN")
            view?.addSpan(DotSpan(5F, Color.parseColor("#00796b")))
        if (scheduleList[0].color == "BLUE")
            view?.addSpan(DotSpan(5F, Color.parseColor("#0288d1")))
        if (scheduleList[0].color == "DEEP BLUE")
            view?.addSpan(DotSpan(5F, Color.parseColor("#303f9f")))
        if (scheduleList[0].color == "BROWN")
            view?.addSpan(DotSpan(5F, Color.parseColor("#5d4037")))
        if (scheduleList[0].color == "GRAY")
            view?.addSpan(DotSpan(5F, Color.parseColor("#455a64")))
    }
}