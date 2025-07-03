package com.youngsik.jinada.presentation

import androidx.compose.runtime.mutableStateListOf
import com.youngsik.jinada.shared.utils.changeToLocalDate
import com.youngsik.jinada.shared.data.StatisticsData
import com.youngsik.jinada.shared.data.TodoItemData
import com.youngsik.jinada.shared.utils.getWeekRange
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


object MemoMockData{
    private val memoList = mutableStateListOf<TodoItemData>()
    init {
        with(memoList){
            add(TodoItemData(1, "여름용 반팔 티셔츠 3장", true, "마리오아울렛", "1.2 km", "2025년 5월 12일","2025년 5월 12일"))
            add(TodoItemData(2, "사무용품(A4용지, 볼펜) 구매", true, "다이소 가산디지털단지점", "850 m", "2025년 5월 13일","2025년 5월 13일"))
            add(TodoItemData(3, "샴푸, 바디워시, 폼클렌징", true, "올리브영 가산점", "900 m", "2025년 5월 13일","2025년 5월 13일"))
            add(TodoItemData(4, "운동화 세일하는지 구경하기", true, "W-MALL", "1.1 km", "2025년 6월 19일","2025년 6월 19일"))
            add(TodoItemData(5, "해외 직구 물품 반품 택배 보내기", true, "가산동우체국", "1.5 km", "2025년 6월 19일","2025년 6월 19일"))
            add(TodoItemData(6, "닭가슴살 1kg, 샐러드 채소", true, "홈플러스 금천점", "2.1 km", "2025년 6월 24일","2025년 6월 24일"))
            add(TodoItemData(7, "선물용 커피 원두 구매", true, "스타벅스 가산디지털역점", "1.0 km", "2025년 6월 24일","2025년 6월 24일"))
            add(TodoItemData(8, "저녁으로 먹을 빅맥 세트 포장", true, "맥도날드", "700 m", "2025년 6월 24일","2025년 6월 24일"))

            add(TodoItemData(9, "생수 2L 6개, 즉석밥", true, "이마트 구로점", "2.5 km", "2025년 6월 24일","2025년 6월 24일"))
            add(TodoItemData(10, "점심 먹고 아이스 아메리카노", true, "메가커피 가산점", "650 m", "2025년 6월 25일","2025년 6월 25일"))
            add(TodoItemData(11, "청바지, 여름용 샌들 구매", true, "현대아울렛 가산점", "1.4 km", "2025년 6월 28일","2025년 6월 28일"))
            add(TodoItemData(12, "노트북 파우치, 무선 마우스", true, "일렉트로마트 영등포점", "4.5 km", "2025년 6월 28일","2025년 6월 28일"))
            add(TodoItemData(13, "와인, 치즈, 올리브 구매", false, "롯데팩토리아울렛 가산점", "1.8 km", "2025년 6월 28일"))
            add(TodoItemData(14, "저녁 약속 치킨집 예약", false, "교촌치킨 독산점", "1.9 km", "2025년 6월 28일"))
            add(TodoItemData(15, "주민등록등본 발급받기", false, "금천구청", "2.0 km", "2025년 6월 28일"))
            add(TodoItemData(16, "마스크 KF94 한 박스", false, "조은약국", "500 m", "2025년 6월 29일"))
            add(TodoItemData(17, "읽고 싶은 소설책 대여", false, "가산도서관", "1.3 km", "2025년 6월 29일"))
            add(TodoItemData(18, "기프티콘으로 케이크 픽업", false, "투썸플레이스 구로디지털점", "2.9 km", "2025년 6월 30일"))
            add(TodoItemData(19, "친구랑 볼 영화 시간표 확인", false, "롯데시네마 가산디지털", "1.3 km", "2025년 6월 30일"))
        }
    }

    fun getMemosNearby() = memoList.filter {
        it.isCompleted == false && changeKmToMeter(it.distance) <= 1300
    }.toMutableList()

    fun getMemosOnOrAfter(targetDate: LocalDate)= memoList.filter{
        !changeToLocalDate(it.deadlineDate).isBefore(targetDate)
    }.sortedBy { !it.isCompleted }.toMutableList()

    private fun changeKmToMeter(distance: String): Double{
        when {
            distance.contains("km") -> return distance.replace("km", "").trim().toDouble() * 1000
            else -> return distance.replace("m", "").trim().toDouble()
        }
    }

    fun getCompleteRateDataInWeek(): List<TodoItemData>{
        val (startDate,endDate) = getWeekRange(LocalDate.now())
        return memoList.filter {
            changeToLocalDate(it.deadlineDate).isAfter(startDate) && changeToLocalDate(it.deadlineDate).isBefore(endDate)
        }
    }

    fun getMemosAtMonth() = memoList.filter {
        changeToLocalDate(it.deadlineDate).monthValue == LocalDate.now().monthValue
    }.toMutableList()

    fun getMemosAll() = memoList

    fun getWeeklyStatData(memoList: List<TodoItemData>): StatisticsData.WeeklyStatData{

        val filteredData = memoList.filter { it.isCompleted && it.completeDate != null }
            .groupBy { changeToLocalDate(it.completeDate).dayOfWeek }
            .mapValues { it.value.size }

        val dayOfWeek = filteredData.maxByOrNull { it.value }?.key?.getDisplayName(TextStyle.FULL, Locale.KOREAN) ?: ""
        val completeCount = filteredData.maxByOrNull { it.value }?.value ?: 0
        val incompletedCount = memoList.filter { !it.isCompleted }.size

        return StatisticsData.WeeklyStatData(dayOfWeek,completeCount,incompletedCount)
    }

    fun getMonthlyStatData(){

    }


    fun getTotalyStatData(memoList: List<TodoItemData>): StatisticsData.TotallyStatData{
        val totalCompletedCount = memoList.filter { it.isCompleted }.size

        val completedByMonth = memoList
            .filter { it.isCompleted && it.completeDate != null }
            .groupBy { changeToLocalDate(it.completeDate)!!.withDayOfMonth(1) }
            .mapValues { it.value.size }

        val maxMonth = completedByMonth.maxByOrNull { it.value }
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월")

        val bestMonth = maxMonth?.key?.format(formatter) ?: ""
        val bestMonthCompletedCount = maxMonth?.value ?: 0

        return StatisticsData.TotallyStatData(totalCompletedCount,bestMonth,bestMonthCompletedCount)
    }

}