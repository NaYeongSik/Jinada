package com.youngsik.jinada.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.youngsik.jinada.presentation.databinding.MapContainerLayoutBinding

@Composable
fun NaverMapView(onMapLongClick: (String)-> Unit){
    var cameraPosition by remember { mutableStateOf(CameraPosition(LatLng(37.472336,126.895997),16.0)) }
    var myPosition by remember { mutableStateOf(LatLng(37.472336,126.895997)) }
    var mapView: MapView? by remember { mutableStateOf(null) }

    if (mapView != null) ManageMapViewLifecycle(mapView)

    AndroidViewBinding(
        modifier = Modifier,
        factory = { inflater, parent, attachToParent ->
            MapContainerLayoutBinding.inflate(inflater, parent, attachToParent)
        },
        update = {
            if (mapView == null) mapView = this.mapView

            mapView!!.getMapAsync { naverMap ->

                val locationOverlay = naverMap.locationOverlay
                locationOverlay.position = myPosition
                locationOverlay.isVisible = true

                naverMap.cameraPosition = cameraPosition

                naverMap.setOnMapLongClickListener { point, coord -> // point = 화면 좌표, coord = 위치정보
                    onMapLongClick("주소 반환") // TODO: 위치정보 같이 넘겨주기
                }

            }
        }
    )
}

@Composable
fun ManageMapViewLifecycle(mapView: MapView?) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                mapView!!.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView!!.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView!!.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView!!.onStop()
            }
        }

        mapView!!.onCreate(null)
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            mapView.onDestroy()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}