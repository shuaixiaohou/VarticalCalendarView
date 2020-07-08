package com.housaiying.varticalcalendarview.holders

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.housaiying.varticalcalendarview.R
import com.housaiying.varticalcalendarview.VerticalCalendarView
import java.util.*

/**
 * Created by housaiying on 2020/7/6
 */
class MonthViewHolder(
    itemView: View, var weekRowsCount: Int,
    private val attrs: VerticalCalendarView.Attributes,
    private val mOnDayClickListener: VerticalCalendarView.OnDayClickListener
) : RecyclerView.ViewHolder(itemView) {

    private val mContext: Context
    private val weeksContainer: LinearLayout
    var tvMonth: TextView
    var weeksColumns: ArrayList<Array<WeekDayView?>>
    var mMonth = 0
    var mYear = 0

    init {
        (itemView.layoutParams as RecyclerView.LayoutParams).setMargins(
            0,
            0,
            0,
            attrs.monthDividerSize
        )

        mContext = itemView.context
        tvMonth = itemView.findViewById(R.id.label_month)
        tvMonth.layoutParams.height = attrs.monthLabelHeight
        TextViewCompat.setTextAppearance(tvMonth, attrs.monthTextAppearanceId)
        weeksContainer = itemView.findViewById(R.id.weeks_container)
        weeksColumns = ArrayList()
        val weekDayNames: LinearLayout = itemView.findViewById(R.id.label_days)
        weekDayNames.layoutParams.height = attrs.weekdayHeight

        for (i in 0 until weekDayNames.childCount) {
            weekDayNames.getChildAt(i).layoutParams.width = attrs.dayWidth

            TextViewCompat.setTextAppearance(
                (weekDayNames.getChildAt(i) as TextView),
                attrs.weekDayTextAppearanceId
            )
        }
    }

    fun generateWeekRows() {
        var linearLayout: LinearLayout

        var layoutParams: LinearLayout.LayoutParams
        for (i in 0 until weekRowsCount) {
            linearLayout = LinearLayout(mContext)
            layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, attrs.dayHeight)

            linearLayout.layoutParams = layoutParams
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.gravity = Gravity.CENTER
            generateWeekColumns(linearLayout)

            weeksContainer.addView(linearLayout)
        }
    }

    private fun generateWeekColumns(linearLayout: LinearLayout) {
        val columns = arrayOfNulls<WeekDayView>(7)

        val inflater = LayoutInflater.from(mContext)

        var tvDay: TextView
        var container: View
        for (i in 0..6) {
            container = inflater.inflate(R.layout.day_view, linearLayout, false)
            container.tag = i
            container.layoutParams.width = attrs.dayWidth

            val eventCircleView: View = container.findViewById(R.id.circle)
            val todayCircleView: View = container.findViewById(R.id.today_circle)

            (eventCircleView.background as GradientDrawable).setColor(attrs.eventCircleColor)
            (todayCircleView.background as GradientDrawable).setColor(attrs.todayCircleColor)

            todayCircleView.layoutParams.width = attrs.todayCircleSize
            todayCircleView.layoutParams.height = attrs.todayCircleSize

            tvDay = container.findViewById(R.id.tv_day)
            TextViewCompat.setTextAppearance(tvDay, attrs.dateTextAppearanceId)

            tvDay.layoutParams.width = attrs.todayCircleSize
            tvDay.layoutParams.height = attrs.todayCircleSize

            container.setOnClickListener { view ->
                val day = view.tag as Int
                if (day > 0) {
                    mOnDayClickListener.onClick(day, mMonth, mYear, false)
                }
            }

            linearLayout.addView(container)

            columns[i] = WeekDayView(container, tvDay, eventCircleView, todayCircleView)
        }
        weeksColumns.add(columns)
    }

    inner class WeekDayView internal constructor(
        var container: View,
        var tvvalue: TextView,
        var viewEventCircle: View,
        var viewTodayCircle: View
    ) {
        init {
            this.viewEventCircle.visibility = View.INVISIBLE
            this.viewTodayCircle.visibility = View.INVISIBLE
        }
    }
}
