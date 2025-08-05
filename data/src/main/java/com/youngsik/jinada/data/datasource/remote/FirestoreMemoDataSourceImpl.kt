package com.youngsik.jinada.data.datasource.remote

import android.location.Location
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.TodoItemData
import com.youngsik.jinada.data.dataclass.TodoItemDto
import com.youngsik.jinada.data.datasource.MemoDataSource
import com.youngsik.jinada.data.mapper.toDomainModel
import com.youngsik.jinada.data.mapper.toDto
import com.youngsik.jinada.data.utils.changeToLocalDate
import com.youngsik.jinada.data.utils.toTimestamp
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class FirestoreMemoDataSourceImpl : MemoDataSource {
    private val memoCollection = Firebase.firestore.collection("memo")
    private val userId = "EgQ5odyd7yj0yxcactCv" // TODO: sharedPreference에 저장된 uuid 가져오기

    override suspend fun createMemo(todoItemData: TodoItemData): DataResourceResult<Unit> = runCatching {
        val location = GeoLocation(todoItemData.latitude, todoItemData.longitude)
        val geohash = GeoFireUtils.getGeoHashForLocation(location)
        memoCollection.add(todoItemData.toDto(userId,geohash)).await()

        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun updateMemo(todoItemData: TodoItemData): DataResourceResult<Unit> = runCatching {
        val location = GeoLocation(todoItemData.latitude, todoItemData.longitude)
        val geohash = GeoFireUtils.getGeoHashForLocation(location)
        memoCollection.document(todoItemData.memoId).set(todoItemData.toDto(userId,geohash)).await()
        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun deleteMemo(memoId: String): DataResourceResult<Unit> = runCatching {
        memoCollection.document(memoId).delete().await()
        DataResourceResult.Success(Unit)
    }.getOrElse {
        DataResourceResult.Failure(it)
    }

    override suspend fun getMemoById(memoId: String): DataResourceResult<TodoItemData> = runCatching {
        val todoItem = memoCollection.document(memoId).get().await()
        val dto = todoItem.toObject(TodoItemDto::class.java)
        if (dto != null){
            DataResourceResult.Success(dto.toDomainModel(todoItem.id))
        } else {
            DataResourceResult.Failure(Exception("dto is null"))
        }
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getMemoListBySelectedDate(date: String): DataResourceResult<List<TodoItemData>> = runCatching {
        val snapShot = memoCollection.whereEqualTo("uuid", userId)
            .whereGreaterThanOrEqualTo("deadline_date", changeToLocalDate(date).toTimestamp())
            .get().await()

        val memoList = snapShot.documents.mapNotNull { document ->
            document.toObject(TodoItemDto::class.java)?.toDomainModel(document.id)
        }

        val filteredList = memoList.filter { memo ->
            if (memo.isCompleted) changeToLocalDate(memo.completeDate!!) >= changeToLocalDate(date)
            else true
        }
        DataResourceResult.Success(filteredList)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getMemoListBySelectedStatTabMenu(selectedTabMenu: String): DataResourceResult<List<TodoItemData>> = runCatching {
        val query = memoCollection.whereEqualTo("uuid", userId)

        val finalQuery = when (selectedTabMenu) {
            "WEEKLY" -> {
                val startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                query.whereGreaterThanOrEqualTo("deadline_date", startOfWeek.toTimestamp())
            }
            "MONTHLY" -> {
                val startOfMonth = LocalDate.now().withDayOfMonth(1)
                query.whereGreaterThanOrEqualTo("deadline_date", startOfMonth.toTimestamp())
            }
            "TOTALLY" -> {
                query
            }
            else -> query
        }

        val snapshot = finalQuery.get().await()
        val memoListResult = snapshot.documents.mapNotNull { document ->
            document.toObject(TodoItemDto::class.java)?.toDomainModel(document.id)
        }

        DataResourceResult.Success(memoListResult)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getNearByMemoList(location: Location): DataResourceResult<List<TodoItemData>> = runCatching {
        val targetLocation = GeoLocation(location.latitude, location.longitude)
        val rangeDistanceInMeters = 1500.0 // TODO: SharedPreference에 저장된 거리 가져오기
        val queryBounds = GeoFireUtils.getGeoHashQueryBounds(targetLocation, rangeDistanceInMeters)

        val tasks = queryBounds
            .map { bound ->
                memoCollection
                    .orderBy("geohash")
                    .startAt(bound.startHash)
                    .endAt(bound.endHash)
                    .get()
            }
        Tasks.whenAllComplete(tasks).await()

        val nearbyMemos = tasks
            .filter { it.isSuccessful }
            .mapNotNull { it.result }
            .flatMap { it.documents }
            .mapNotNull { doc ->
                val dto = doc.toObject(TodoItemDto::class.java)
                dto?.locationInfo?.let { locationInfo ->
                    val docLocation = GeoLocation(locationInfo.latitude, locationInfo.longitude)
                    val distanceInMeters = GeoFireUtils.getDistanceBetween(docLocation, targetLocation)
                    if (distanceInMeters <= rangeDistanceInMeters) {
                        dto.toDomainModel(doc.id, distanceInMeters)
                    } else null
                }
            }

        DataResourceResult.Success(nearbyMemos)
    }.getOrElse { DataResourceResult.Failure(it) }
}