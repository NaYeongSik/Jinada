package com.youngsik.jinada.presentation.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
class DatePickerSelectableDates(private val allowPastDates: Boolean) : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return if (allowPastDates) { // 모든 날짜 선택
            true
        } else { // 과거 선택 불가
            val todayStartMillis = LocalDate
                .now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
            utcTimeMillis >= todayStartMillis
        }
    }
}