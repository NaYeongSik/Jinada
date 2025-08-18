package com.youngsik.jinada.data.impl

import android.location.Location
import com.youngsik.shared.model.DataResourceResult
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.data.datasource.MemoDataSource
import com.youngsik.jinada.data.repository.MemoRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MemoRepositoryImpl(val memoDataSource: MemoDataSource) : MemoRepository {
    override suspend fun createMemo(todoItemData: TodoItemData,nickname: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.createMemo(todoItemData,nickname))
    }.catch { emit(DataResourceResult.Failure(it)) }


    override suspend fun updateMemo(todoItemData: TodoItemData,nickname: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.updateMemo(todoItemData,nickname))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun deleteMemo(memoId: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.deleteMemo(memoId))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoById(memoId: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoById(memoId))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoListBySelectedDate(date: String,nickname: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedDate(date,nickname))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String,nickname: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedStatTabMenu(selectedTabMenu,nickname))
    }

    override suspend fun getNearByMemoList(nickname: String,location: Location, range: Float) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getNearByMemoList(nickname,location,range))
    }.catch { emit(DataResourceResult.Failure(it)) }
}