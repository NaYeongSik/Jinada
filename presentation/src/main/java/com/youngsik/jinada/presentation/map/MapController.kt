package com.youngsik.jinada.presentation.map

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.youngsik.domain.entity.PoiItem
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.data.utils.changeToStringDate
import com.youngsik.shared.R
import java.time.LocalDate

class MapController(private val context: Context){
    private var mapView: NaverMap? = null
    private var memoMarkers: MutableList<Marker> = mutableListOf() // 근처 메모에 대한 마커 리스트
    private var memoInfoWindow: InfoWindow = InfoWindow() // 근처 메모에 대한 인포윈도우
    private var locationOverlay: LocationOverlay? = null // 사용자 위치 오버레이

    private var tempMarker: Marker? = null // 사용자 롱클릭 이벤트 지점에 대한 마커
    private var tempInfoWindow: InfoWindow = InfoWindow() // 사용자 롱클릭 이벤트 지점에 대한 인포윈도우

    private var searchMarkers: MutableList<Marker> = mutableListOf() // 검색 결과에 대한 마커 리스트

    internal fun initMapController(naverMap: NaverMap){
        this.mapView = naverMap
        this.locationOverlay = mapView?.locationOverlay
    }

    fun updateMyLocationOverlay(myPosition: LatLng?){
        if (myPosition != null) {
            locationOverlay?.position = myPosition
            locationOverlay?.isVisible = true
        }
    }

    fun moveToTargetLocation(location: LatLng){
        mapView?.moveCamera(CameraUpdate.scrollTo(location).animate(CameraAnimation.Easing))
    }

    fun setCameraPosition(latLng: LatLng?){
        latLng?.let { mapView?.cameraPosition = CameraPosition(it,16.0) }
    }

    fun setMapLongClickListener(onMapLongClick: (TodoItemData)-> Unit){
        mapView?.setOnMapLongClickListener { point, coord -> // point = 화면 좌표, coord = 위치정보
            onMapLongClick(TodoItemData(latitude = coord.latitude, longitude = coord.longitude, deadlineDate = changeToStringDate(
                LocalDate.now())))
        }
    }

    fun updateMemoMarkers(memoList: List<TodoItemData>,onMarkerClick: (String) -> Unit){
        clearMemoMarkers()

        memoList.forEach { memo ->
            if (memo.latitude != 0.0 && memo.longitude != 0.0){
                val newMaker = Marker().apply {
                    icon = OverlayImage.fromResource(R.drawable.jinada_map_marker)
                    position = LatLng(memo.latitude,memo.longitude)
                    map = mapView
                    tag = memo.memoId
                    width = 225
                    height = 240
                    setOnClickListener {
                        onMarkerClick(memo.memoId)
                        true
                    }
                }
                memoMarkers.add(newMaker)
            }
        }
    }

    fun showInfoWindow(memoList: List<TodoItemData>,selectedMarkerId: String){
        if (selectedMarkerId.isBlank()) {
            memoInfoWindow.close()
        } else {
            val selectedMarker = memoMarkers.find { it.tag == selectedMarkerId }
            val selectedMemo = memoList.find { it.memoId == selectedMarkerId }

            if (selectedMarker != null && selectedMemo != null) {
                memoInfoWindow.adapter = object : InfoWindow.DefaultViewAdapter(context){
                    override fun getContentView(infoWindow: InfoWindow): View {
                        return TextView(context).apply {
                            text = selectedMemo.content
                        }
                    }

                }
                memoInfoWindow.open(selectedMarker)
            } else {
                memoInfoWindow.close()
            }
        }
    }

    fun showTemporaryMarker(todoItemData: TodoItemData,onCreateMemo: (TodoItemData) -> Unit) {
        clearTemporaryMarker()
        clearSearchMarkers()

        val targetLocation = LatLng(todoItemData.latitude, todoItemData.longitude)

        tempMarker = Marker().apply {
            position = targetLocation
            map = mapView
            icon = OverlayImage.fromResource(R.drawable.jinada_temp_marker)
            width = 225
            height = 240
            onClickListener = Overlay.OnClickListener {
                onCreateMemo(todoItemData)
                true
            }
        }

        tempInfoWindow.adapter = object : InfoWindow.DefaultViewAdapter(context) {
            override fun getContentView(infoWindow: InfoWindow): View {
                return TextView(context).apply { text = todoItemData.locationName }
            }
        }
        tempInfoWindow.open(tempMarker!!)
        moveToTargetLocation(targetLocation)
    }

    fun showSearchItemMarkers(poiItemList: List<PoiItem>, onCreateMemo: (TodoItemData) -> Unit) {
        clearTemporaryMarker()
        clearSearchMarkers()

        poiItemList.forEach { poiData ->
            if (poiData.latitude != 0.0 && poiData.longitude != 0.0){
                val newMaker = Marker().apply {
                    icon = OverlayImage.fromResource(R.drawable.jinada_temp_marker)
                    position = LatLng(poiData.latitude,poiData.longitude)
                    map = mapView
                    tag = poiData.buildingName
                    width = 225
                    height = 240
                    captionText = poiData.buildingName.ifBlank { poiData.roadAddress }
                    captionTextSize = 14f
                    captionColor = Color.BLACK
                    captionHaloColor = Color.WHITE
                    setOnClickListener {
                        onCreateMemo(TodoItemData(latitude = poiData.latitude, longitude = poiData.longitude, locationName = poiData.buildingName, deadlineDate = changeToStringDate(LocalDate.now())))
                        true
                    }
                }
                searchMarkers.add(newMaker)
            }
        }
    }

    fun clearMemoMarkers(){
        memoMarkers.forEach { maker ->
            maker.map = null
        }
        memoInfoWindow.close()
        memoMarkers.clear()
    }


    fun clearTemporaryMarker() {
        tempMarker?.map = null
        tempInfoWindow.close()
        tempMarker = null
    }

    fun clearSearchMarkers(){
        searchMarkers.forEach { maker ->
            maker.map = null
        }
        searchMarkers.clear()
    }

}

@Composable
fun rememberMapController(context: Context): MapController{
    return remember {
        MapController(context)
    }
}