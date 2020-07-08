package com.housaiying.varticalcalendarview

import android.app.Service
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.housaiying.varticalcalendarview.adapters.VerticalCalendarAdapter

/**
 * Created by housaiying on 2020/7/6
 */
class VerticalCalendarView : FrameLayout {

    private var rvCalendar: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mCalendarAdapter: VerticalCalendarAdapter? = null
    private var mOnDayClickListener: OnDayClickListener? = null
    private var calendarAttrs = Attributes()

    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 1
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0
    internal var totalItemCount: Int = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        getAttrs(attrs, defStyle)

        val layoutInflater =
            context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val content = layoutInflater.inflate(R.layout.calendar_view, null, false)
        addView(content)

        rvCalendar = findViewById(R.id.rl_calendar)
        mLayoutManager = LinearLayoutManager(context)
        rvCalendar?.layoutManager = mLayoutManager

        setAdapter()

        mLayoutManager?.scrollToPosition(3)

        rvCalendar?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = recyclerView.childCount
                totalItemCount = mCalendarAdapter!!.itemCount
                firstVisibleItem =
                    (mLayoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold
                    && mCalendarAdapter!!.shouldLoadNextMonths()
                ) {
                    // End has been reached
//                    mCalendarAdapter!!.getNextMonths()
//                    loading = true
                }

                if (!loading && firstVisibleItem <= 1 + visibleThreshold
                    && mCalendarAdapter!!.shouldLoadPreviousMonths()
                ) {
                    // Start has been reached
                    mCalendarAdapter!!.getPreviousMonth()
                    loading = true
                }
            }
        })

        invalidate()
    }

    private fun getAttrs(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.VerticalCalendarView, defStyle, 0
        )
        val displayMetrics = resources.displayMetrics

        calendarAttrs.weekdayHeight = a.getDimension(
            R.styleable.VerticalCalendarView_weekdayNameHeight,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, displayMetrics)
        ).toInt()

        val typedValue = TypedValue()

        calendarAttrs.dayHeight = a.getDimension(
            R.styleable.VerticalCalendarView_dayHeight,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, displayMetrics)
        ).toInt()

        calendarAttrs.dayWidth = a.getDimension(
            R.styleable.VerticalCalendarView_dayWidth,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, displayMetrics)
        ).toInt()

        calendarAttrs.todayCircleSize = a.getDimension(
            R.styleable.VerticalCalendarView_todayCircleSize,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, displayMetrics)
        ).toInt()

        a.getValue(R.styleable.VerticalCalendarView_todayCircleColor, typedValue)

        if (typedValue.type == TypedValue.TYPE_REFERENCE) {
            calendarAttrs.todayCircleColor = ContextCompat.getColor(
                context,
                a.getResourceId(
                    R.styleable.VerticalCalendarView_todayCircleColor,
                    R.color.default_todayCircleColor
                )
            )

        } else {
            calendarAttrs.todayCircleColor = a.getColor(
                R.styleable.VerticalCalendarView_todayCircleColor,
                ContextCompat.getColor(context, R.color.default_todayCircleColor)
            )
        }

        a.getValue(R.styleable.VerticalCalendarView_eventCircleColor, typedValue)

        if (typedValue.type == TypedValue.TYPE_REFERENCE) {
            calendarAttrs.eventCircleColor = ContextCompat.getColor(
                context,
                a.getResourceId(
                    R.styleable.VerticalCalendarView_eventCircleColor,
                    R.color.default_eventCircleColor
                )
            )

        } else {
            calendarAttrs.eventCircleColor = a.getColor(
                R.styleable.VerticalCalendarView_eventCircleColor,
                ContextCompat.getColor(context, R.color.default_eventCircleColor)
            )
        }


        (background as? ColorDrawable?).let {
            if (it != null) {
                calendarAttrs.backgroundColor = it.color
            } else {
                calendarAttrs.backgroundColor = Color.GRAY
            }
        }

        calendarAttrs.monthDividerSize = a.getDimension(
            R.styleable.VerticalCalendarView_monthDividerSize,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, displayMetrics)
        ).toInt()

        calendarAttrs.monthLabelHeight = a.getDimension(
            R.styleable.VerticalCalendarView_monthLabelHeight,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, displayMetrics)
        ).toInt()

        setWeekDayTextAppearance(
            a.getResourceId(
                R.styleable.VerticalCalendarView_weekDayTextAppearance,
                R.style.TextAppearance_VerticalCalendar_WeekDay
            )
        )

        setDateTextAppearance(
            a.getResourceId(
                R.styleable.VerticalCalendarView_dateTextAppearance,
                R.style.TextAppearance_VerticalCalendar_Date
            )
        )

        setMonthTextAppearance(
            a.getResourceId(
                R.styleable.VerticalCalendarView_monthTextAppearance,
                R.style.TextAppearance_VerticalCalendar_Month
            )
        )
        a.recycle()
    }

    fun setDateTextAppearance(taId: Int) {
        calendarAttrs.dateTextAppearanceId = taId
    }

    fun setWeekDayTextAppearance(taId: Int) {
        calendarAttrs.weekDayTextAppearanceId = taId
    }

    private fun setMonthTextAppearance(taId: Int) {
        calendarAttrs.monthTextAppearanceId = taId
    }

    private fun setAdapter() {
        mCalendarAdapter = VerticalCalendarAdapter(context, calendarAttrs)
        rvCalendar?.adapter = mCalendarAdapter

        mCalendarAdapter?.setOnDayClickListener(object : OnDayClickListener {

            override fun onClick(day: Int, month: Int, year: Int, hasEvent: Boolean) {
                mOnDayClickListener?.onClick(day, month, year, hasEvent)
            }
        })
    }

    fun setOnDayClickListener(onDayClickListener: OnDayClickListener) {
        mOnDayClickListener = onDayClickListener
    }

    fun addEvent(day: Int, month: Int, year: Int) {
        mCalendarAdapter!!.addEvent(day, month, year)
    }

    fun deleteEvent(day: Int, month: Int, year: Int) {
        mCalendarAdapter!!.deleteEvent(day, month, year)
    }

    /* Classes & Interfaces*/

    interface OnDayClickListener {
        fun onClick(day: Int, month: Int, year: Int, hasEvent: Boolean)
    }

    inner class Attributes {

        var backgroundColor = 0

        var monthTextAppearanceId = 0
        var dateTextAppearanceId = 0
        var weekDayTextAppearanceId = 0

        var monthLabelHeight = 0
        var weekdayHeight = 0

        var dayWidth = 0
        var dayHeight = 0

        var todayCircleColor = 0
        var todayCircleSize = 0

        var monthDividerSize = 0

        var eventCircleColor = 0
    }
}