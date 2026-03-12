package com.youngsik.jinada.data.impl

import android.location.Location
import com.youngsik.domain.entity.DataResourceResult
import com.youngsik.domain.entity.LocationEntity
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.domain.repository.MemoRepository
import com.youngsik.jinada.data.datasource.MemoDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(val memoDataSource: MemoDataSource) : MemoRepository {
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

    override suspend fun getNearByMemoList(nickname: String, location: LocationEntity, range: Float) = flow {
        emit(DataResourceResult.Loading)
        val androidLocation = Location("").apply {
            latitude = location.latitude
            longitude = location.longitude
        }
        emit(memoDataSource.getNearByMemoList(nickname, androidLocation, range))
    }.catch { emit(DataResourceResult.Failure(it)) }
}