package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.data.utils.getCompleteRateData
import com.youngsik.jinada.data.utils.getTotalyStatData
import com.youngsik.jinada.data.utils.getWeeklyStatData
import com.youngsik.jinada.presentation.common.StatTabMenu
import com.youngsik.jinada.presentation.uistate.MemoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoViewModel(private val repository: MemoRepository) : ViewModel(){
    companion object{
        const val NONE = "NONE"
        const val SUCCESSFUL_GET_MEMO = "SUCCESSFUL_GET_MEMO"
        const val SUCCESSFUL_CREATE_MEMO = "SUCCESSFUL_CREATE_MEMO"
        const val SUCCESSFUL_UPDATE_MEMO = "SUCCESSFUL_UPDATE_MEMO"
        const val SUCCESSFUL_DELETE_MEMO = "SUCCESSFUL_DELETE_MEMO"
        const val SUCCESSFUL_GET_STATISTICS = "SUCCESSFUL_GET_STATISTICS"
    }
    private val _memoUiState = MutableStateFlow(MemoUiState())
    val memoUiState get() = _memoUiState.asStateFlow()

    fun changeSelectedDate(date: String){
        _memoUiState.update { it.copy(selectedDate = date) }
        getMemoListBySelectedDate(_memoUiState.value.selectedDate)
    }

    fun changeSelectedTab(tabMenu: StatTabMenu){
        _memoUiState.update { it.copy(selectedTabMenu = tabMenu) }
        getMemoListBySelectedStatTabMenu(_memoUiState.value.selectedTabMenu.name)
    }

    fun resetLastSuccessfulAction(){
        _memoUiState.update { it.copy(lastSuccessfulAction = NONE) }
    }

    fun createMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            repository.createMemo(todoItemData).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_CREATE_MEMO) }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun updateMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            repository.updateMemo(todoItemData).collectLatest{ result ->
            when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_UPDATE_MEMO) }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun deleteMemo(memoId: String){
        viewModelScope.launch {
            repository.deleteMemo(memoId).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_DELETE_MEMO) }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun getMemoListBySelectedDate(date: String){
        viewModelScope.launch {
            repository.getMemoListBySelectedDate(date).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_MEMO, memoList = result.data) }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String){
        viewModelScope.launch {
            repository.getMemoListBySelectedStatTabMenu(selectedTabMenu).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true, lastSuccessfulAction = NONE) }
                    is DataResourceResult.Success -> {
                        when(selectedTabMenu){
                            StatTabMenu.WEEKLY.name -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_STATISTICS, memoListInSelectedTab = result.data, completeRateData = getCompleteRateData(result.data), statData = getWeeklyStatData(result.data)) }
                            StatTabMenu.MONTHLY.name -> {}
                            StatTabMenu.TOTALLY.name -> _memoUiState.update { it.copy(isLoading = false, lastSuccessfulAction = SUCCESSFUL_GET_STATISTICS, memoListInSelectedTab = result.data, completeRateData = getCompleteRateData(result.data), statData = getTotalyStatData(result.data)) }
                        }
                    }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}