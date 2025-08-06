package com.youngsik.domain.model

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
        val growthRate : Float,
        ) : StatisticsData
}