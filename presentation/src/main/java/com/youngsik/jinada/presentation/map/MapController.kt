package com.youngsik.jinada.presentation.map

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.LocationOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.youngsik.jinada.data.TodoItemData
import com.youngsik.jinada.presentation.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MapController(private val scope: CoroutineScope){
    private var context: Context? = null
    private var mapView: NaverMap? = null
    private var markers: MutableList<Marker> = mutableListOf()
    private var infoWindow: InfoWindow = InfoWindow()
    private var locationOverlay: LocationOverlay? = null

    internal fun initMapController(naverMap: NaverMap,context: Context){
        this.mapView = naverMap
        this.context = context
        this.locationOverlay = mapView?.locationOverlay
    }

    fun updateMyLocationOverlay(myPosition: LatLng){
        locationOverlay?.position = myPosition
        locationOverlay?.isVisible = true
    }

    fun moveToTargetLocation(location: LatLng){
        scope.launch {
            mapView?.moveCamera(CameraUpdate.scrollTo(location).animate(CameraAnimation.Easing))
        }
    }

    fun setCameraPosition(latLng: LatLng){
        mapView?.cameraPosition = CameraPosition(latLng,16.0)
    }

    fun setMapLongClickListener(onMapLongClick: (String)-> Unit){
        mapView?.setOnMapLongClickListener { point, coord -> // point = 화면 좌표, coord = 위치정보
            onMapLongClick("주소 반환") // TODO: 위치정보 같이 넘겨주기
        }
    }

    fun updateMemoMarkers(memoList: List<TodoItemData>,onMarkerClick: (Int) -> Unit){
        markers.forEach { maker ->
            maker.map = null
        }
        markers.clear()

        memoList.forEach { memo ->
            if (memo.latitude != 0.0 && memo.longitude != 0.0){
                val newMaker = Marker().apply {
                    icon = OverlayImage.fromResource(R.drawable.jinada_map_marker)
                    position = LatLng(memo.latitude,memo.longitude)
                    map = mapView
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

    fun showInfoWindow(memoList: List<TodoItemData>,selectedMarkerId: Int){
        if (selectedMarkerId == -1) {
            infoWindow.close()
        } else {
            val selectedMarker = markers.find { it.tag == selectedMarkerId }
            val selectedMemo = memoList.find { it.id == selectedMarkerId }

            if (selectedMarker != null && selectedMemo != null && context != null) {
                infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(context!!){
                    override fun getContentView(infoWindow: InfoWindow): View {
                        return TextView(context).apply {
                            text = selectedMemo.title
                        }
                    }

                }
                infoWindow.open(selectedMarker)
            } else {
                infoWindow.close()
            }
        }
    }
}

@Composable
fun rememberMapController(): MapController{
    val scope = rememberCoroutineScope()
    return remember {
        MapController(scope)
    }
}