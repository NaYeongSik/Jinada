package com.youngsik.jinada.data.utils

import android.location.Location
import com.google.firebase.Timestamp
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.model.CompleteRateData
import com.youngsik.domain.model.StatisticsData
import com.youngsik.domain.model.TodoItemData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
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

fun getWeeklyStatData(memoList: List<TodoItemData>): StatisticsData.WeeklyStatData{

    val filteredData = memoList.filter { it.isCompleted && it.completeDate != null }
        .groupBy { changeToLocalDate(it.deadlineDate).dayOfWeek }
        .mapValues { it.value.size }

    val dayOfWeek = filteredData.maxByOrNull { it.value }?.key?.getDisplayName(TextStyle.FULL,
        Locale.KOREAN) ?: ""
    val completeCount = filteredData.maxByOrNull { it.value }?.value ?: 0
    val incompletedCount = memoList.filter { !it.isCompleted }.size

    return StatisticsData.WeeklyStatData(dayOfWeek,completeCount,incompletedCount)
}

fun getTotalyStatData(memoList: List<TodoItemData>): StatisticsData.TotallyStatData{
    val totalCompletedCount = memoList.filter { it.isCompleted }.size

    val completedByMonth = memoList
        .filter { it.isCompleted && it.completeDate != null }
        .groupBy { changeToLocalDate(it.deadlineDate!!).withDayOfMonth(1) }
        .mapValues { it.value.size }

    val maxMonth = completedByMonth.maxByOrNull { it.value }
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월")

    val bestMonth = maxMonth?.key?.format(formatter) ?: ""
    val bestMonthCompletedCount = maxMonth?.value ?: 0

    return StatisticsData.TotallyStatData(totalCompletedCount,bestMonth,bestMonthCompletedCount)
}

fun getWeekRange(date: LocalDate): Pair<LocalDate, LocalDate> {

    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    return Pair(startOfWeek, endOfWeek)
}

fun Timestamp.toLocalDate(): LocalDate = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
fun LocalDate.toTimestamp(): Timestamp = Timestamp(this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0)

fun Location.toLatLng(): LatLng = LatLng(this.latitude,this.longitude)
fun LatLng.toLocation(): Location{
    val location = Location("location")
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}

fun Double.toStringDistance(): String = String.format("%.1f", this) + "m"

