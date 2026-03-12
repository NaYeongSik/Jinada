package com.youngsik.jinada.presentation.uistate

import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.presentation.common.StatTabMenu
import com.youngsik.domain.entity.CompleteRateData
import com.youngsik.domain.entity.StatisticsData
import com.youngsik.shared.utils.changeToStringDate
import java.time.LocalDate

data class MemoUiState(
    val isLoading: Boolean = false,
    val lastSuccessfulAction: String = "",
    val isFailure: Boolean = false,
    val memoList: List<TodoItemData> = emptyList(),
    val selectedDate: String = changeToStringDate(LocalDate.now()),
    val selectedTabMenu: StatTabMenu = StatTabMenu.WEEKLY,
    val completeRateData: CompleteRateData = CompleteRateData(0f,0,0),
    val statData: StatisticsData = StatisticsData.WeeklyStatData("",0,0,0),
    val memoListInSelectedTab: List<TodoItemData> = emptyList(),
    val nickname: String = ""
)