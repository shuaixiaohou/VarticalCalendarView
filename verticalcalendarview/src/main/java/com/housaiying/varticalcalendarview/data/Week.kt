package com.housaiying.varticalcalendarview.data

/**
 * Created by housaiying on 2020/7/6
 */
class Week(firstDay: Int = 0, weekDay: Int = 0, maxDay: Int = firstDay + 6) {
    var days: Array<Day> = arrayOf(Day(), Day(), Day(), Day(), Day(), Day(), Day())

    init {
        var day = 0
        var incr = 0
        for (i in 1..7) {
            if (i == weekDay) {
                day = firstDay
                incr = 1
            }
            days[i - 1] = Day(day)
            if (day == maxDay) {
                day = 0
                incr = 0
            }
            day += incr
        }
    }

    fun getLastDay(): Int {
        var i = 6
        while (days[i].value == 0) {
            i--
        }
        return days[i].value
    }
}
