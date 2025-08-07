package com.youngsik.jinada.data.utils

import android.location.Location
import com.google.firebase.Timestamp
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.model.CompleteRateData
import com.youngsik.domain.model.StatisticsData
import com.youngsik.domain.model.TodoItemData
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
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
    val mostCompletedCount = filteredData.maxByOrNull { it.value }?.value ?: 0
    val incompletedCount = memoList.filter { !it.isCompleted }.size
    val completedCount = memoList.filter { it.isCompleted }.size

    return StatisticsData.WeeklyStatData(dayOfWeek,mostCompletedCount,incompletedCount,completedCount)
}

fun getMonthlyStatData(memoList: List<TodoItemData>): StatisticsData.MonthlyStatData{
    val weekFields = WeekFields.of(Locale.KOREAN)

    val completedMemos = memoList
        .filter { it.isCompleted && !it.completeDate.isNullOrBlank() }
        .mapNotNull { memo ->
            val completeDate = runCatching { changeToLocalDate(memo.completeDate!!) }.getOrNull()
            if (completeDate != null) memo to completeDate else null
        }

    val mostActiveWeekLabel = completedMemos
        .groupingBy { (_, completeDate) -> completeDate.get(weekFields.weekOfMonth()) }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
        ?.let {
            when (it) {
                1 -> "첫째 주"
                2 -> "둘째 주"
                3 -> "셋째 주"
                4 -> "넷째 주"
                5 -> "다섯째 주"
                else -> ""
            }
        } ?: ""

    val longestCompletionStreak = getMaxConsecutiveDays(memoList)

    var onTimeCompletionCount = 0
    var earlyCompletionCount  = 0

    for ((memo, completeDate) in completedMemos) {
        val deadlineDate = changeToLocalDate(memo.deadlineDate) ?: continue

        if (completeDate == deadlineDate) {
            onTimeCompletionCount++
        } else if (completeDate < deadlineDate) {
            earlyCompletionCount ++
        }
    }

    return StatisticsData.MonthlyStatData(mostActiveWeekLabel,longestCompletionStreak,earlyCompletionCount ,onTimeCompletionCount)

}

fun getTotalyStatData(memoList: List<TodoItemData>): StatisticsData.TotallyStatData{
    val totalCompletedCount = memoList.filter { it.isCompleted }.size

    val completedByMonth = memoList
        .filter { it.isCompleted && it.completeDate != null }
        .groupBy { changeToLocalDate(it.deadlineDate).withDayOfMonth(1) }
        .mapValues { it.value.size }

    val maxMonth = completedByMonth.maxByOrNull { it.value }
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월")

    val bestMonth = maxMonth?.key?.format(formatter) ?: ""
    val bestMonthCompletedCount = maxMonth?.value ?: 0

    return StatisticsData.TotallyStatData(totalCompletedCount,bestMonth,bestMonthCompletedCount)
}

fun getMaxConsecutiveDays(memoList: List<TodoItemData>): Int {
    if (memoList.isEmpty()) {
        return 0
    }

    val completedDates = memoList
        .filter { it.isCompleted && it.completeDate != null }
        .map { it.completeDate!! }
        .distinct()
        .sorted()

    if (completedDates.isEmpty()) {
        return 0
    }

    var maxStreak = 1
    var currentStreak = 1

    for (i in 1 until completedDates.size) {
        if (changeToLocalDate(completedDates[i]).minusDays(1) == changeToLocalDate(completedDates[i-1])) {
            currentStreak++ // 연속 기록 증가
        } else {
            // 연속이 끊기면, 현재 기록을 최대 기록과 비교하고 초기화
            maxStreak = maxOf(maxStreak, currentStreak)
            currentStreak = 1
        }
    }

    // 마지막으로 기록된 연속 기록도 확인
    return maxOf(maxStreak, currentStreak)
}


fun Timestamp.toLocalDate(): LocalDate = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
fun LocalDate.toTimestamp(): Timestamp = Timestamp(this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0)

fun LatLng.toLocation(): Location{
    val location = Location("location")
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}

