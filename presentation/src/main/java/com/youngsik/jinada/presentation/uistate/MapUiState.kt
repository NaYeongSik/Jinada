package com.youngsik.jinada.presentation.uistate

import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.dataclass.TodoItemData

data class MapUiState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isFailure: Boolean = false,
    val nearByMemoList: List<TodoItemData> = emptyList(),
    val myLocation: LatLng? = null
)