package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.CLOSER_MEMO_NOTI_RANGE
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.CLOSER_MEMO_SEARCHING_RANGE
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.CLOSER_NOTIFICATION_ENABLED
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.DAILY_NOTIFICATION_ENABLED
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.NICKNAME
import com.youngsik.jinada.data.datasource.local.DataStoreDataSourceImpl.Companion.UUID
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.presentation.uistate.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreRepository: DataStoreRepository): ViewModel() {

    companion object{
        const val NONE = "NONE"
        const val SUCCESSFUL_GET_USER_INFO = "SUCCESSFUL_GET_USER_INFO"
        const val SUCCESSFUL_SET_USER_INFO = "SUCCESSFUL_SET_USER_INFO"
        const val SUCCESSFUL_GET_NOTIFICATION_ENABLED = "SUCCESSFUL_GET_NOTIFICATION_ENABLED"
        const val SUCCESSFUL_SET_NOTIFICATION_ENABLED = "SUCCESSFUL_SET_NOTIFICATION_ENABLED"
        const val SUCCESSFUL_GET_RANGE_OPTION = "SUCCESSFUL_GET_RANGE_OPTION"
        const val SUCCESSFUL_SET_RANGE_OPTION = "SUCCESSFUL_SET_RANGE_OPTION"
    }
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState get() = _settingsUiState.asStateFlow()

    init {
        getUserInfo()
        getNotificationEnabled()
        getRangeOption()
    }

    fun resetLastSuccessfulAction(){
        _settingsUiState.update { it.copy(lastSuccessfulAction = NONE) }
    }

    fun getUserInfo(){
        viewModelScope.launch {
            dataStoreRepository.getUserInfo().collect{ result ->
                when(result){
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_USER_INFO,nickname = result.data.getOrDefault(NICKNAME,""), uuid = result.data.getOrDefault(UUID,"")) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun getNotificationEnabled(){
        viewModelScope.launch {
            dataStoreRepository.getNotificationEnabled().collect{ result ->
                when(result){
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_NOTIFICATION_ENABLED, closerNotiEnabled = result.data.getOrDefault(CLOSER_NOTIFICATION_ENABLED,false), dailyNotiEnabled = result.data.getOrDefault(DAILY_NOTIFICATION_ENABLED,false)) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }

            }
        }
    }

    fun getRangeOption() {
        viewModelScope.launch {
            dataStoreRepository.getRangeOption().collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_RANGE_OPTION, closerMemoSearchingRange = result.data.getOrDefault(CLOSER_MEMO_SEARCHING_RANGE, 0.3f),closerMemoNotiRange = result.data.getOrDefault(CLOSER_MEMO_NOTI_RANGE, 0.3f)) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true)}
                }
            }
        }
    }


    fun setUserInfo(nickname: String, uuid: String){
        viewModelScope.launch {
            dataStoreRepository.setUserInfo(nickname,uuid).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_SET_USER_INFO, nickname = nickname, uuid = uuid) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun setNotificationEnabled(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean){
        viewModelScope.launch {
            dataStoreRepository.setNotificationEnabled(isCheckedCloserNoti,isCheckedDailyNoti).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_SET_NOTIFICATION_ENABLED,closerNotiEnabled = isCheckedCloserNoti, dailyNotiEnabled = isCheckedDailyNoti) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float){
        viewModelScope.launch {
            dataStoreRepository.setRangeOption(closerMemoSearchingRange,closerMemoNotiRange).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_SET_RANGE_OPTION,closerMemoSearchingRange = closerMemoSearchingRange, closerMemoNotiRange = closerMemoNotiRange)}
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }


}