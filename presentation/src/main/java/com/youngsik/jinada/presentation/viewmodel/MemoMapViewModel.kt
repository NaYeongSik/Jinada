package com.youngsik.jinada.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.common.LocationRepositoryProvider
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.presentation.uistate.MapUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoMapViewModel(application: Application, private val repository: MemoRepository) : AndroidViewModel(application) {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState get() = _mapUiState.asStateFlow()

    private val locationRepository: CurrentLocationRepository by lazy {
        LocationRepositoryProvider.getInstance(application.applicationContext)
    }

    fun observeCurrentLocation() {
        viewModelScope.launch {
            (locationRepository as CurrentLocationRepositoryImpl).latestLocationState
                .collect { location ->
                    if (location != null) {
                        _mapUiState.update { currentState ->
                            if (_mapUiState.value.cameraPosition == null) {
                                currentState.copy(
                                    myLocation = LatLng(location.latitude, location.longitude),
                                    cameraPosition = LatLng(location.latitude, location.longitude)
                                )
                            } else {
                                currentState.copy(
                                    myLocation = LatLng(location.latitude, location.longitude)
                                )
                            }
                        }
                    }
                }
        }
    }

    fun getMemosNearby(myLocation: LatLng) {
        viewModelScope.launch {
            repository.getNearByMemoList(myLocation).collect { result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _mapUiState.update { it.copy(isLoading = false, isSuccessful = true, nearByMemoList = result.data) }
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun updateMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            repository.updateMemo(todoItemData).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!)
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun deleteMemo(memoId: String){
        viewModelScope.launch {
            repository.deleteMemo(memoId).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!)
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}