package com.youngsik.jinada.data.impl

import android.location.Location
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.datasource.MemoDataSource
import com.youngsik.jinada.data.repository.MemoRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MemoRepositoryImpl(val memoDataSource: MemoDataSource) : MemoRepository {
    override suspend fun createMemo(todoItemData: TodoItemData) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.createMemo(todoItemData))
    }.catch { emit(DataResourceResult.Failure(it)) }


    override suspend fun updateMemo(todoItemData: TodoItemData) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.updateMemo(todoItemData))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun deleteMemo(memoId: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.deleteMemo(memoId))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoById(memoId: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoById(memoId))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoListBySelectedDate(date: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedDate(date))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedStatTabMenu(selectedTabMenu))
    }

    override suspend fun getNearByMemoList(location: Location, range: Float) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getNearByMemoList(location,range))
    }.catch { emit(DataResourceResult.Failure(it)) }
}