package com.youngsik.shared.model

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

    data class MonthlyStatData (
        val mostActiveWeekLabel: String,
        val longestCompletionStreak: Int,
        val earlyCompletionCount: Int,
        val onTimeCompletionCount: Int
        ) : StatisticsData
}