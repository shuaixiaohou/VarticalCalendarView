package com.housaiying.varticalcalendarview.data

/**
 * Created by housaiying on 2020/7/6
 */
class Month(var value: Int, var year: Int) {
    var weeks: ArrayList<Week> = WeekManager.getWeeks(value, year)
    var lastDay: Int = 0

    init {
        lastDay = weeks[weeks.size - 1].getLastDay()
    }
}