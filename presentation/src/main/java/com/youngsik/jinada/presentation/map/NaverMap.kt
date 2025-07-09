package com.youngsik.jinada.presentation.map

import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.youngsik.jinada.presentation.component.MapSearchBar
import com.youngsik.jinada.presentation.component.MyLocationButton
import com.youngsik.jinada.presentation.databinding.MapContainerLayoutBinding
import com.youngsik.jinada.shared.data.TodoItemData
import com.youngsik.jinada.shared.theme.JinadaDimens

@Composable
fun NaverMapView(modifier: Modifier, memoList: List<TodoItemData>, onMapLongClick: (String)-> Unit){
    var inputText by remember { mutableStateOf("") }
    var cameraPosition by remember { mutableStateOf(CameraPosition(LatLng(37.472336,126.895997),16.0)) }
    var myPosition by remember { mutableStateOf(LatLng(37.472336,126.895997)) }
    var mapView: MapView? by remember { mutableStateOf(null) }
    var naverMap: NaverMap? by remember { mutableStateOf(null) }
    val markers = remember { mutableListOf<Marker>() }
    val infoWindow = remember { InfoWindow() }
    var selectedMarkerId by remember { mutableIntStateOf(-1) }

    if (mapView != null) ManageMapViewLifecycle(mapView, cameraPosition,onMapLongClick,{ naverMapInstance -> naverMap = naverMapInstance })

    val currentNaverMap = naverMap

    Box(modifier = modifier){
        AndroidViewBinding(
            modifier = Modifier,
            factory = { inflater, parent, attachToParent ->
                MapContainerLayoutBinding.inflate(inflater, parent, attachToParent)
            },
            update = {
                if (mapView == null) mapView = this.mapView

                if (currentNaverMap != null){
                    showMyLocationOverlay(currentNaverMap,myPosition)
                    updateMemoMarkers(currentNaverMap,memoList,markers,{ newSelectedMarkerId -> selectedMarkerId = newSelectedMarkerId})
                }

                if (selectedMarkerId == -1) {
                    infoWindow.close()
                } else {
                    val selectedMarker = markers.find { it.tag == selectedMarkerId }
                    val selectedMemo = memoList.find { it.id == selectedMarkerId }

                    if (selectedMarker != null && selectedMemo != null) {
                        infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(this@AndroidViewBinding.root.context){
                            override fun getContentView(infoWindow: InfoWindow): View {
                                return TextView(this@AndroidViewBinding.root.context).apply {
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
        )

        MapSearchBar(Modifier.align(Alignment.TopCenter)
            .padding(JinadaDimens.Padding.medium)
            .fillMaxWidth(0.9f)
            ,inputText,{ it -> inputText = it})

        MyLocationButton(Modifier
            .align(Alignment.BottomEnd)
            .padding(JinadaDimens.Padding.medium)
            ,{ if (currentNaverMap != null) moveToLocation(currentNaverMap,myPosition) })
    }

}

@Composable
fun ManageMapViewLifecycle(mapView: MapView?, cameraPosition: CameraPosition,onMapLongClick: (String)-> Unit, onMapReady: (NaverMap) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                mapView?.let { mapView ->
                    mapView.onCreate(null)
                    mapView.getMapAsync { naverMap ->
                        naverMap.cameraPosition = cameraPosition

                        naverMap.setOnMapLongClickListener { point, coord -> // point = 화면 좌표, coord = 위치정보
                            onMapLongClick("주소 반환") // TODO: 위치정보 같이 넘겨주기
                        }
                        showMyLocationOverlay(naverMap, LatLng(37.472336,126.895997))
                        onMapReady(naverMap)
                    }
                }

            }

            override fun onStart(owner: LifecycleOwner) {
                mapView?.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView?.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView?.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView?.onStop()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            mapView?.onDestroy()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}