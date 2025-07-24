package com.youngsik.jinada.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.repository.MemoRepository
import com.youngsik.jinada.presentation.uistate.MemoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MemoViewModel(private val repository: MemoRepository) : ViewModel(){
    private val _memoUiState = MutableStateFlow(MemoUiState())
    val memoUiState get() = _memoUiState.asStateFlow()

    init {
        getMemoListBySelectedDate(_memoUiState.value.selectedDate)
    }

    fun changeSelectedDate(date: String){
        _memoUiState.update { it.copy(selectedDate = date) }
        getMemoListBySelectedDate(_memoUiState.value.selectedDate)
    }

    fun createMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            repository.createMemo(todoItemData).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemoListBySelectedDate(_memoUiState.value.selectedDate)
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun updateMemo(todoItemData: TodoItemData){
        viewModelScope.launch {
            repository.updateMemo(todoItemData).collectLatest{ result ->
            when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemoListBySelectedDate(_memoUiState.value.selectedDate)
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun deleteMemo(memoId: String){
        viewModelScope.launch {
            repository.deleteMemo(memoId).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> getMemoListBySelectedDate(_memoUiState.value.selectedDate)
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

    fun getMemoListBySelectedDate(date: String){
        viewModelScope.launch {
            repository.getMemoListBySelectedDate(date).collectLatest{ result ->
                when(result){
                    is DataResourceResult.Loading -> _memoUiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> _memoUiState.update { it.copy(isLoading = false, isSuccessful = true, memoList = result.data) }
                    is DataResourceResult.Failure -> _memoUiState.update { it.copy(isLoading = false, isFailure = true) }
                }
            }
        }
    }

}