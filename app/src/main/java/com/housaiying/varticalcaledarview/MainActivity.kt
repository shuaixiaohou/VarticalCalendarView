package com.housaiying.varticalcaledarview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.housaiying.varticalcalendarview.VerticalCalendarView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by housaiying on 2020/7/6
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView.setOnDayClickListener(object : VerticalCalendarView.OnDayClickListener {
            override fun onClick(day: Int, month: Int, year: Int, hasEvent: Boolean) {
                if (hasEvent)
                    Toast.makeText(
                        this@MainActivity,
                        "正在跳转 $year/$month/$day 的聊天记录界面.........",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(
                        this@MainActivity,
                        "$year/$month/$day 没有聊天记录",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        })
        //    Handler().postDelayed(Runnable {//模拟耗时操作
     //   calendarView?.addEvent(1, 6, 2020)
      //  calendarView?.addEvent(5, 6, 2020)
     //   calendarView?.addEvent(6, 6, 2020)
     //   calendarView?.addEvent(2, 6, 2020)
     //   calendarView?.addEvent(1, 7, 2020)
     //   calendarView?.addEvent(3, 7, 2020)
     //   calendarView?.addEvent(5, 7, 2020)
       //  }, 5000)
        Handler().postDelayed(Runnable {//模拟网络请求耗时操作
        for (dayList in 1..31) {
            for (monthList in 1..12) {
                for (yearList in 2018..2020) {
                    val obj = 1
                    calendarView?.addEvent(dayList, monthList, yearList, obj)
                }
            }
        }
        }, 5000)
    }
}
