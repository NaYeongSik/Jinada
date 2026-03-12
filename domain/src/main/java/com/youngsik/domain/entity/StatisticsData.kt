package com.youngsik.domain.entity

/**
 * 통계 데이터를 나타내는 비즈니스 엔티티입니다.
 * Totally, Weekly, Monthly 통계 구조를 정의합니다.
 */
sealed interface StatisticsData {
    data class TotallyStatData(
        val totalCompletedMemoCount: Int,
        val bestMonth: String,
        val bestMonthCompletedCount: Int,
    ) : StatisticsData

    data class WeeklyStatData(
        val mostCompletedDayOfWeek: String,
        val mostCompletedCount: Int,
        val incompletedCount: Int,
        val completedCount: Int
    ) : StatisticsData

    data class MonthlyStatData(
        val mostActiveWeekLabel: String,
        val longestCompletionStreak: Int,
        val earlyCompletionCount: Int,
        val onTimeCompletionCount: Int
    ) : StatisticsData
}
