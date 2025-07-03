package com.youngsik.jinada.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context).apply { } }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }

        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            mapView.onDestroy()
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun NaverMapView(onMapLongClick: (String)-> Unit){
    val mapView = rememberMapViewWithLifecycle()

    // TODO: 사용자 위치 수집해서 기본값 설정하도록 수정 필요
    var cameraPosition by remember { mutableStateOf(CameraPosition(LatLng(37.472336,126.895997),16.0)) }
    var myPosition by remember { mutableStateOf(LatLng(37.472336,126.895997)) }

    AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize()){ view ->
        view.getMapAsync { naverMap ->
            val locationOverlay = naverMap.locationOverlay
            locationOverlay.position = myPosition
            locationOverlay.isVisible = true

            naverMap.cameraPosition = cameraPosition

            //TODO: 메모 위치 마커 생성 필요

            naverMap.setOnMapLongClickListener { point, coord -> // point = 화면 좌표, coord = 위치정보
                onMapLongClick("주소 반환") // TODO: 위치정보 같이 넘겨주기
            }
        }
    }
}