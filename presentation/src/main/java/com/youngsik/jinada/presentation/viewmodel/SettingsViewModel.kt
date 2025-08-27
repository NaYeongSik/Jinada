package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsik.domain.entity.UserInfo
import com.youngsik.domain.manager.ActivityRecognitionManager
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.data.repository.UserRepository
import com.youngsik.jinada.presentation.uistate.SettingsUiState
import com.youngsik.shared.model.DataResourceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val userRepository: UserRepository, private val dataStoreRepository: DataStoreRepository, private val activityRecognitionManager: ActivityRecognitionManager): ViewModel() {
    companion object{
        const val NONE = "NONE"
        const val SUCCESSFUL_INIT_SETTINGS = "SUCCESSFUL_INIT_SETTINGS"
        const val SUCCESSFUL_SET_USER_INFO = "SUCCESSFUL_SET_USER_INFO"
        const val SUCCESSFUL_CREATE_USER_INFO = "SUCCESSFUL_CREATE_USER_INFO"
        const val SUCCESSFUL_SET_NOTIFICATION_ENABLED = "SUCCESSFUL_SET_NOTIFICATION_ENABLED"
        const val SUCCESSFUL_SET_RANGE_OPTION = "SUCCESSFUL_SET_RANGE_OPTION"
        const val SUCCESSFUL_CHECK_NICKNAME = "SUCCESSFUL_CHECK_NICKNAME"
    }
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState get() = _settingsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val userInfo = dataStoreRepository.userInfo.first()
            val userSettings = dataStoreRepository.userSettings.first()

            _settingsUiState.value = SettingsUiState(
                nickname = userInfo.nickname,
                uuid = userInfo.uuid,
                closerNotiEnabled = userSettings.closerNotificationEnabled,
                dailyNotiEnabled = userSettings.dailyNotificationEnabled,
                closerMemoSearchingRange = userSettings.closerMemoSearchingRange,
                closerMemoNotiRange = userSettings.closerMemoNotiRange,
                lastSuccessfulAction = SUCCESSFUL_INIT_SETTINGS
            )

            combine(
                dataStoreRepository.userInfo,
                dataStoreRepository.userSettings
            ) { userInfo, userSettings ->
                SettingsUiState(
                    nickname = userInfo.nickname,
                    uuid = userInfo.uuid,
                    closerNotiEnabled = userSettings.closerNotificationEnabled,
                    dailyNotiEnabled = userSettings.dailyNotificationEnabled,
                    closerMemoSearchingRange = userSettings.closerMemoSearchingRange,
                    closerMemoNotiRange = userSettings.closerMemoNotiRange
                )
            }.collect { newState ->
                _settingsUiState.value = newState
            }
        }
    }

    fun startActivityRecognition(){
        if (_settingsUiState.value.closerNotiEnabled) activityRecognitionManager.startActivityRecognition()
    }

    fun stopActivityRecognition(){
        activityRecognitionManager.stopActivityRecognition()
    }


    fun resetLastSuccessfulAction(){
        _settingsUiState.update { it.copy(lastSuccessfulAction = NONE) }
    }

    fun resetNicknameAvailable(){
        _settingsUiState.update { it.copy(lastSuccessfulAction = NONE, isNicknameAvailable = false) }
    }

    fun checkNicknameExists(nickname: String){
        viewModelScope.launch {
            userRepository.isNicknameAvailable(nickname).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_CHECK_NICKNAME,isNicknameAvailable = result.data) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
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

    fun createUserInfoInFirestore(nickname: String, uuid: String){
        viewModelScope.launch {
            userRepository.createUserInfo(UserInfo(uuid,nickname)).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_CREATE_USER_INFO, nickname = nickname, uuid = uuid) }
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