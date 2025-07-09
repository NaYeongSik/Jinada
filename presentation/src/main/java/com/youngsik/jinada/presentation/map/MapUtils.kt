package com.youngsik.jinada.presentation.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.shared.data.TodoItemData

fun showMyLocationOverlay(naverMap: NaverMap, myPosition: LatLng){
    val locationOverlay = naverMap.locationOverlay
    locationOverlay.position = myPosition
    locationOverlay.isVisible = true
}

fun moveToLocation(naverMap: NaverMap, position: LatLng)= naverMap.moveCamera(CameraUpdate.scrollTo(position))

fun updateMemoMarkers(naverMap: NaverMap, memoList: List<TodoItemData>, markers: MutableList<Marker>,onMarkerClick: (Int) -> Unit){
    markers.forEach { maker ->
        maker.map = null
    }
    markers.clear()

    memoList.forEach { memo ->
        if (memo.latitude != 0.0 && memo.longitude != 0.0){
            val newMaker = Marker().apply {
                icon = OverlayImage.fromResource(R.drawable.jinada_map_marker)
                position = LatLng(memo.latitude,memo.longitude)
                map = naverMap
                tag = memo.id
                width = 225
                height = 240
                setOnClickListener {
                    onMarkerClick(memo.id)
                    true
                }
            }
            markers.add(newMaker)
        }
    }
}

