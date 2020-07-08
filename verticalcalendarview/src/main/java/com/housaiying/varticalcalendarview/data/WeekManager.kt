package com.housaiying.varticalcalendarview.data

/**
 * Created by housaiying on 2020/7/6
 */
object WeekManager {

    fun getWeeks(month: Int, year: Int): ArrayList<Week> {
        val wkCount = getWeekCount(month, year)

        val weeks = ArrayList<Week>()

        var weekDay = getWeekDay(1, month, year)
        var firstDay = 1
        val weekMaxDay = getWeekMaxDay(month, year)

        var wk: Week
        for (week in 0 until wkCount) {
            wk = Week(firstDay, weekDay, weekMaxDay)
            weeks.add(wk)
            firstDay = wk.getLastDay() + 1
            weekDay = 1
        }

        return weeks
    }

    private fun getWeekDay(day: Int, month: Int, years: Int): Int {
        var year = years
        var val2x = month

        if (month == 1) {
            val2x = 13
            year -= 1
        }

        if (month == 2) {
            val2x = 14
            year -= 1
        }

        val val4 = (val2x + 1) * 3 / 5
        val val5 = year / 4
        val val6 = year / 100
        val val7 = year / 400

        val val8 = day + val2x * 2 + val4 + year + val5 - val6 + val7 + 2
        val val9 = val8 / 7
        val val0 = val8 - val9 * 7

        return if (val0 == 0) 7 else val0
    }

    private fun getWeekCount(month: Int, year: Int): Int {
        val wd = getWeekDay(1, month, year)

        //Check if month has 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return if (wd == 7) 6 else 5

        }

        // Check if month is February
        if (month == 2) {
            //Check if Leap year
            if (isLeapYear(year)) {
                return 5
            }
            // Month has 28 days
            return if (wd > 1) 5 else 4

        } else {
            //Month has 31 days
            return if (wd > 5) 6 else 5
        }
    }

    private fun getWeekMaxDay(month: Int, year: Int): Int {
        return if (month == 4 || month == 6 || month == 9 || month == 11) {
            30
        } else {
            if (month == 2) {
                if (isLeapYear(year)) 29 else 28
            } else {
                31
            }
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return when {
            year % 4 > 0 -> false
            year % 100 > 0 -> true
            else -> year % 400 <= 0
        }
    }
}