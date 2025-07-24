package com.youngsik.jinada.presentation.uistate

import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.utils.changeToStringDate
import java.time.LocalDate

data class MemoUiState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isFailure: Boolean = false,
    val memoList: List<TodoItemData> = emptyList(),
    val selectedDate: String = changeToStringDate(LocalDate.now())
)