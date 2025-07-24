package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.presentation.uistate.MapUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemoMapViewModel(private val repository: MemoRepository) : ViewModel() {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState get() = _mapUiState.asStateFlow()

    fun getMemosNearby(myLocation: LatLng) {
        viewModelScope.launch {
            // TODO: 인근 메모들 가져오기
        }
    }

}