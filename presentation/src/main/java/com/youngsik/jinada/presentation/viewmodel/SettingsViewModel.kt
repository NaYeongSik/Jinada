package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.repository.DataStoreRepository
import com.youngsik.jinada.presentation.uistate.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreRepository: DataStoreRepository): ViewModel() {
    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState get() = _settingsUiState.asStateFlow()

    init {
        getNickName()
        getNotificationEnabled()
    }

    fun getNickName(){
        viewModelScope.launch {
            dataStoreRepository.getNickname().collect{ result ->
                when(result){
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(nickname = result.data) }
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
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(notificationEnabled = result.data) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }

            }
        }
    }


    fun saveNickName(nickName: String){
        viewModelScope.launch {
            dataStoreRepository.setNickname(nickName).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _settingsUiState.update { it.copy(isLoading = false, isSuccessful = true, nickname = nickName) }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun saveNotificationEnabled(enabled: Boolean){
        viewModelScope.launch {
            dataStoreRepository.setNotificationEnabled(enabled).collect { result ->
                when (result) {
                    is DataResourceResult.Loading -> _settingsUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        _settingsUiState.update { it.copy(isLoading = false, isSuccessful = true,notificationEnabled = enabled) }
                    }
                    is DataResourceResult.Failure -> _settingsUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}