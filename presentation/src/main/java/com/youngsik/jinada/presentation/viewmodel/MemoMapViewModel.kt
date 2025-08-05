package com.youngsik.jinada.presentation.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.impl.NaverRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.utils.toLocation
import com.youngsik.jinada.presentation.BuildConfig
import com.youngsik.jinada.presentation.uistate.MapUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoMapViewModel(application: Application, private val repository: MemoRepository, private val locationRepository: CurrentLocationRepository, private val naverRepository: NaverRepository) : AndroidViewModel(application) {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState get() = _mapUiState.asStateFlow()

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
                        getMemosNearby(location)
                    }
                }
        }
    }

    fun getTargetLocationInfo(todoItemData: TodoItemData){
        viewModelScope.launch {
            val locationName = naverRepository.getAddressFromCoordinates(
                BuildConfig.NAVER_MAP_CLIENT_ID,
                BuildConfig.NAVER_MAP_CLIENT_SECRET,
                "${todoItemData.longitude}, ${todoItemData.latitude}")
            _mapUiState.update { it.copy(targetLocationInfo = todoItemData.copy(locationName = locationName?:"")) }
            Log.d("jinada_test", "getTargetLocationInfo: $locationName")
        }
    }

    fun getMemosNearby(myLocation: Location) {
        viewModelScope.launch {
            repository.getNearByMemoList(myLocation).collect { result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        val nearByMemoList = result.data.filter { todoItemData -> !todoItemData.isCompleted }
                        _mapUiState.update { it.copy(isLoading = false, isSuccessful = true, nearByMemoList = nearByMemoList) }
                        Log.d("jinada_test", "getMemosNearby: ${nearByMemoList.size}")
                    }
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
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!.toLocation())
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
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!.toLocation())
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}