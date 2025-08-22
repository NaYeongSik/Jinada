package com.youngsik.jinada.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.manager.LocationServiceManager
import com.youngsik.jinada.data.impl.CurrentLocationRepositoryImpl
import com.youngsik.jinada.data.repository.CurrentLocationRepository
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.presentation.BuildConfig
import com.youngsik.jinada.presentation.uistate.MapUiState
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.shared.utils.toLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoMapViewModel @Inject constructor(private val memoRepository: MemoRepository, private val locationRepository: CurrentLocationRepository, private val naverRepository: NaverRepository, private val dataStoreRepository: DataStoreRepository, private val locationServiceManager: LocationServiceManager) : ViewModel() {
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
            (locationRepository as CurrentLocationRepositoryImpl).latestLocationState.collectLatest { location ->
                if (location != null){
                    _mapUiState.update { currentState ->
                        val updated = currentState.copy(myLocation = LatLng(location.latitude, location.longitude))
                        if (currentState.cameraPosition == null) {
                            updated.copy(cameraPosition = updated.myLocation)
                        } else updated
                    }
                    getMemosNearby(location, _mapUiState.value.closerMemoSearchingRange)
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
            val locationName = naverRepository.getAddressFromCoordinates(
                BuildConfig.NAVER_MAP_CLIENT_ID,
                BuildConfig.NAVER_MAP_CLIENT_SECRET,
                "${todoItemData.longitude}, ${todoItemData.latitude}")
            _mapUiState.update { it.copy(targetLocationInfo = todoItemData.copy(locationName = locationName?:"")) }
        }
    }

    fun getSearchPoi(query: String) {
        viewModelScope.launch {
            val poiList = naverRepository.getPoiFromInputString(
                BuildConfig.X_NAVER_CLIENT_ID,
                BuildConfig.X_NAVER_CLIENT_SECRET,
                query
            )
            _mapUiState.update { it.copy(searchPoiList = poiList) }
        }
    }

    fun getMemosNearby(myLocation: Location, range: Float) {
        viewModelScope.launch {
            memoRepository.getNearByMemoList(_mapUiState.value.nickname,myLocation,range).collect { result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _mapUiState.update { it.copy(isLoading = false, isSuccessful = true, nearByMemoList = result.data.sortedBy { it -> it.distance }) }
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun updateMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            memoRepository.updateMemo(todoItemData,_mapUiState.value.nickname).collectLatest{ result ->
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
            memoRepository.deleteMemo(memoId).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _mapUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemosNearby(_mapUiState.value.myLocation!!.toLocation(),_mapUiState.value.closerMemoSearchingRange)
                    is DataResourceResult.Failure -> _mapUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}