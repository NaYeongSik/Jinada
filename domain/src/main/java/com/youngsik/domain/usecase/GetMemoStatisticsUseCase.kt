package com.youngsik.domain.usecase

import com.youngsik.domain.entity.CompleteRateData
import com.youngsik.domain.entity.StatisticsData
import com.youngsik.domain.entity.TodoItemData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

/**
 * 메모 데이터를 기반으로 다양한 통계 정보를 계산하는 유즈케이스입니다.
 */
class GetMemoStatisticsUseCase @Inject constructor() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREAN)

    fun getCompleteRateData(memoList: List<TodoItemData>): CompleteRateData {
        val totalMemoCount = memoList.size
        val successMemoCount = memoList.count { it.isCompleted }
        val completeRate = if (totalMemoCount > 0) successMemoCount.toFloat() / totalMemoCount else 0f
        return CompleteRateData(completeRate, totalMemoCount, successMemoCount)
    }

    fun getWeeklyStatData(memoList: List<TodoItemData>): StatisticsData.WeeklyStatData {
        val filteredData = memoList.filter { it.isCompleted && !it.completeDate.isNullOrBlank() }
            .groupBy { parseLocalDate(it.deadlineDate).dayOfWeek }
            .mapValues { it.value.size }

        val dayOfWeek = filteredData.maxByOrNull { it.value }?.key?.getDisplayName(TextStyle.FULL, Locale.KOREAN) ?: ""
        val mostCompletedCount = filteredData.maxByOrNull { it.value }?.value ?: 0
        val incompletedCount = memoList.count { !it.isCompleted }
        val completedCount = memoList.count { it.isCompleted }

        return StatisticsData.WeeklyStatData(dayOfWeek, mostCompletedCount, incompletedCount, completedCount)
    }

    fun getMonthlyStatData(memoList: List<TodoItemData>): StatisticsData.MonthlyStatData {
        val weekFields = WeekFields.of(Locale.KOREAN)

        val completedMemos = memoList
            .filter { it.isCompleted && !it.completeDate.isNullOrBlank() }
            .mapNotNull { memo ->
                val completeDate = runCatching { parseLocalDate(memo.completeDate!!) }.getOrNull()
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
        var earlyCompletionCount = 0

        for ((memo, completeDate) in completedMemos) {
            val deadlineDate = parseLocalDate(memo.deadlineDate)

            if (completeDate == deadlineDate) {
                onTimeCompletionCount++
            } else if (completeDate < deadlineDate) {
                earlyCompletionCount++
            }
        }

        return StatisticsData.MonthlyStatData(mostActiveWeekLabel, longestCompletionStreak, earlyCompletionCount, onTimeCompletionCount)
    }

    fun getTotalyStatData(memoList: List<TodoItemData>): StatisticsData.TotallyStatData {
        val totalCompletedCount = memoList.count { it.isCompleted }

        val completedByMonth = memoList
            .filter { it.isCompleted && !it.completeDate.isNullOrBlank() }
            .groupBy { parseLocalDate(it.deadlineDate).withDayOfMonth(1) }
            .mapValues { it.value.size }

        val maxMonth = completedByMonth.maxByOrNull { it.value }

        val bestMonth = maxMonth?.key?.format(monthFormatter) ?: ""
        val bestMonthCompletedCount = maxMonth?.value ?: 0

        return StatisticsData.TotallyStatData(totalCompletedCount, bestMonth, bestMonthCompletedCount)
    }

    private fun getMaxConsecutiveDays(memoList: List<TodoItemData>): Int {
        if (memoList.isEmpty()) return 0

        val completedDates = memoList
            .filter { it.isCompleted && !it.completeDate.isNullOrBlank() }
            .mapNotNull { runCatching { parseLocalDate(it.completeDate!!) }.getOrNull() }
            .distinct()
            .sorted()

        if (completedDates.isEmpty()) return 0

        var maxStreak = 1
        var currentStreak = 1

        for (i in 1 until completedDates.size) {
            if (completedDates[i].minusDays(1) == completedDates[i - 1]) {
                currentStreak++
            } else {
                maxStreak = maxOf(maxStreak, currentStreak)
                currentStreak = 1
            }
        }

        return maxOf(maxStreak, currentStreak)
    }

    private fun parseLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }
}
