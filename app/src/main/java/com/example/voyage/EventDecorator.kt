package com.example.voyage

import android.graphics.Color
import android.util.Log
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
//        view?.addSpan(DotSpan(7F, Color.parseColor("#303f9f")))
        if (App.prefs.getString("color", "") == "RED")
            redDecorate(view)
        if (App.prefs.getString("color", "") == "ORANGE")
            orangeDecorate(view)
        if (App.prefs.getString("color", "") == "YELLOW")
            yellowDecorate(view)
        if (App.prefs.getString("color", "") == "GREEN")
            greenDecorate(view)
        if (App.prefs.getString("color", "") == "DEEP GREEN")
            deepGreenDecorate(view)
        if (App.prefs.getString("color", "") == "BLUE")
            blueDecorate(view)
        if (App.prefs.getString("color", "") == "DEEP BLUE")
            deepBlueDecorate(view)
        if (App.prefs.getString("color", "") == "BROWN")
            brownDecorate(view)
        if (App.prefs.getString("color", "") == "GRAY")
            grayDecorate(view)
    }
    private fun redDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#d32f2f")))
    }
    private fun orangeDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#f57c00")))
    }
    private fun yellowDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#dfbc02")))
    }
    private fun greenDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#689f38")))
    }
    private fun deepGreenDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#00796b")))
    }
    private fun blueDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#0288d1")))
    }
    private fun deepBlueDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#303f9f")))
    }
    private fun brownDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#5d4037")))
    }
    private fun grayDecorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, Color.parseColor("#455a64")))
    }
}