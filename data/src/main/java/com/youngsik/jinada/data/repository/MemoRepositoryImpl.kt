package com.youngsik.jinada.data.repository

import com.google.type.LatLng
import com.youngsik.jinada.data.common.DataResourceResult
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.datasource.MemoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

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

    override suspend fun getMemoListBySelectedDate(date: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedDate(date))
    }.catch { emit(DataResourceResult.Failure(it)) }

    override suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getMemoListBySelectedStatTabMenu(selectedTabMenu))
    }

    override suspend fun getNearByMemoList(location: LatLng) = flow {
        emit(DataResourceResult.Loading)
        emit(memoDataSource.getNearByMemoList(location))
    }.catch { emit(DataResourceResult.Failure(it)) }
}