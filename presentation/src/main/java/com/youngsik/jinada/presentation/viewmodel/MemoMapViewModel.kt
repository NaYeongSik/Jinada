package com.youngsik.jinada.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.entity.LocationEntity
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.manager.LocationServiceManager
import com.youngsik.domain.repository.CurrentLocationRepository
import com.youngsik.domain.repository.DataStoreRepository
import com.youngsik.domain.usecase.bundle.MapUseCases
import com.youngsik.domain.usecase.bundle.MemoUseCases
import com.youngsik.jinada.presentation.BuildConfig
import com.youngsik.jinada.presentation.uistate.MapUiState
import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.shared.utils.toLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoMapViewModel @Inject constructor(
    private val locationRepository: CurrentLocationRepository, 
    private val dataStoreRepository: DataStoreRepository, 
    private val locationServiceManager: LocationServiceManager,
    private val memoUseCases: MemoUseCases,
    private val mapUseCases: MapUseCases
) : ViewModel() {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState get() = _mapUiState.asStateFlow()


    init {
        viewModelScope.launch {
            dataStoreRepository.userSettings.collectLatest { settings ->
                _mapUiState.update { it.copy(closerMemoSearchingRange = settings.closerMemoSearchingRange) }
            }
        }

        viewModelScope.launch {
            dataStoreRepository.userInfo.collectLatest { userInfo ->
                _mapUiState.update { it.copy(nickname = userInfo.nickname) }
            }
        }

        viewModelScope.launch {
            locationRepository.latestLocationState.collectLatest { location ->
                if (location != null){
                    _mapUiState.update { currentState ->
                        val updated = currentState.copy(myLocation = LatLng(location.latitude, location.longitude))
                        if (currentState.cameraPosition == null) {
                            updated.copy(cameraPosition = updated.myLocation)
                        } else updated
                    }
                    val androidLocation = Location("").apply {
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                    getMemosNearby(androidLocation, _mapUiState.value.closerMemoSearchingRange)
                }
            }
        }
    }

    fun startLocationTracking(){
        locationServiceManager.startLocationTracking()
    }

    fun stopLocationTracking(){
        locationServiceManager.stopLocationTracking()
    }

    fun getTargetLocationInfo(todoItemData: TodoItemData){
        viewModelScope.launch {
            val locationName = mapUseCases.getAddress(
                BuildConfig.NAVER_MAP_CLIENT_ID,
                BuildConfig.NAVER_MAP_CLIENT_SECRET,
                todoItemData.latitude,
                todoItemData.longitude)
            _mapUiState.update { it.copy(targetLocationInfo = todoItemData.copy(locationName = locationName?:"")) }
        }
    }

    fun getSearchPoi(query: String) {
        viewModelScope.launch {
            val poiList = mapUseCases.searchPoi(
                BuildConfig.X_NAVER_CLIENT_ID,
                BuildConfig.X_NAVER_CLIENT_SECRET,
                query
            )
            _mapUiState.update { it.copy(searchPoiList = poiList) }
        }
    }

    fun getMemosNearby(myLocation: Location, range: Float) {
        viewModelScope.launch {
            val locationEntity = LocationEntity(myLocation.latitude, myLocation.longitude)
            memoUseCases.getNearbyMemos(_mapUiState.value.nickname, locationEntity, range).collect { result ->
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
            memoUseCases.updateMemo(todoItemData,_mapUiState.value.nickname).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!.toLocation(),_mapUiState.value.closerMemoSearchingRange)
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun deleteMemo(memoId: String){
        viewModelScope.launch {
            memoUseCases.deleteMemo(memoId).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!.toLocation(),_mapUiState.value.closerMemoSearchingRange)
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}