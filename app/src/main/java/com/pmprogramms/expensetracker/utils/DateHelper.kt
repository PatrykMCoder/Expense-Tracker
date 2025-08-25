package com.pmprogramms.expensetracker.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
class DateHelper {
    companion object {
        fun weekRange(date: LocalDate = LocalDate.now()): Pair<Long, Long> {
            val start = date.with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault())
            val end =
                date.with(DayOfWeek.SATURDAY).atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())

            val startTS = start.toInstant().toEpochMilli()
            val endTS = end.toInstant().toEpochMilli()

            return startTS to endTS
        }

        fun monthRange(date: LocalDate = LocalDate.now()): Pair<Long, Long> {
            val start = date.with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay(ZoneId.systemDefault())
            val end = date.with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX)
                .atZone(ZoneId.systemDefault())

            val startTS = start.toInstant().toEpochMilli()
            val endTS = end.toInstant().toEpochMilli()

            return startTS to endTS
        }

        fun dayRange(date: LocalDate = LocalDate.now()): Pair<Long, Long> {
            val start = date.atStartOfDay(ZoneId.systemDefault())
            val end = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())

            val startTS = start.toInstant().toEpochMilli()
            val endTS = end.toInstant().toEpochMilli()

            return startTS to endTS
        }
    }
}