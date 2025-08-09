package com.youngsik.domain.manager

import com.youngsik.domain.model.TodoItemData

interface GeoFencingManager {
    fun updateGeoPencing(memoList: List<TodoItemData>, notiRange: Float)
    fun removeGeoPencing()
}