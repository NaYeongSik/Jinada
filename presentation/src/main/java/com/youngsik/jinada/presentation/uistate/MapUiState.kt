package com.youngsik.jinada.presentation.uistate

import com.naver.maps.geometry.LatLng
import com.youngsik.domain.model.PoiItem
import com.youngsik.domain.model.TodoItemData

data class MapUiState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isFailure: Boolean = false,
    val nearByMemoList: List<TodoItemData> = emptyList(),
    val myLocation: LatLng? = null,
    val cameraPosition: LatLng? = null,
    val targetLocationInfo: TodoItemData? = null,
    val searchPoiList: List<PoiItem> = emptyList(),
    val closerMemoSearchingRange: Float = 0.3f,
    val nickname: String = ""
)