package com.youngsik.domain.manager

import com.youngsik.domain.entity.TodoItemData

interface GeoFencingManager {
    fun updateGeoFencing(memoList: List<TodoItemData>, notiRange: Float)
    fun removeGeoFencing()
}