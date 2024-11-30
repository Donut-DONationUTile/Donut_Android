package org.gdgoc.donut.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar

class DonutUtil {
    @SuppressLint("SimpleDateFormat")
    fun getDDayInfo(date: String): Long {
        val systemDate = Calendar.getInstance()
        val eventDate = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return (systemDate.time.time - eventDate.time) / (60 * 60 * 24 * 1000) - 1
    }

    @SuppressLint("SimpleDateFormat")
    fun setCalendarFormat(date: String): String? {
        val parsedDate = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return parsedDate?.let { SimpleDateFormat("MMM d").format(it) }
    }

    fun formatDate(date: String): String {
        val regex = """(\d{4})년 (\d{1,2})월 (\d{1,2})일""".toRegex()
        val matchResult = regex.find(date)
        return if (matchResult != null) {
            val (year, month, day) = matchResult.destructured
            "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
        } else {
            date
        }
    }
}