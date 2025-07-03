package com.youngsik.jinada.shared.data

sealed interface StatisticsData {
    data class TotallyStatData(
        val totalCompletedMemoCount: Int,
        val bestMonth: String,
        val bestMonthCompletedCount: Int,
    ) : StatisticsData

    data class WeeklyStatData(
        val mostCompletedDayOfWeek: String,
        val mostCompletedCount: Int,
        val incompletedCount: Int
    ) : StatisticsData

    data class MonthlyStatData (
        val growthRate : Float,
        ) : StatisticsData
}