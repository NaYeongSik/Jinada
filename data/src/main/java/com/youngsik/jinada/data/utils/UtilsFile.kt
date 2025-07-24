package com.youngsik.jinada.data.utils

import com.google.firebase.Timestamp
import com.youngsik.jinada.data.dataclass.CompleteRateData
import com.youngsik.jinada.data.dataclass.TodoItemData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)

fun changeToStringDate(date: LocalDate): String = date.format(formatter)

fun changeToLocalDate(dateString: String) = LocalDate.parse(dateString,formatter)

fun getCompleteRateData(memoList: List<TodoItemData>): CompleteRateData{
    val totalMemoCount = memoList.size
    val successMemoCount = memoList.count { it.isCompleted }
    val completeRate = if (totalMemoCount > 0) successMemoCount.toFloat() / totalMemoCount else 0f
    return CompleteRateData(completeRate,totalMemoCount,successMemoCount)
}

fun getWeekRange(date: LocalDate): Pair<LocalDate, LocalDate> {

    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    return Pair(startOfWeek, endOfWeek)
}

fun Timestamp.toLocalDate(): LocalDate = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
fun LocalDate.toTimestamp(): Timestamp = Timestamp(this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0)

