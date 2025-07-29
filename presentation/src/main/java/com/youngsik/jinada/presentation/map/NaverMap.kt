package com.youngsik.jinada.presentation.map

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.presentation.databinding.MapContainerLayoutBinding
import com.youngsik.jinada.presentation.uistate.MapUiState

@Composable
fun NaverMapView(mapController: MapController, mapUiState: MapUiState, onMapLongClick: (TodoItemData)-> Unit){
    val context = LocalContext.current
    var mapView: MapView? by remember { mutableStateOf(null) }
    var selectedMarkerId by remember { mutableStateOf("") }

    if (mapView != null) MapLifecycleEffect(context,mapView, mapUiState.myLocation,mapUiState.nearByMemoList,mapController,onMapLongClick,{ newSelectedMarkerId -> selectedMarkerId = newSelectedMarkerId })

    AndroidViewBinding(
        modifier = Modifier,
        factory = { inflater, parent, attachToParent ->
            MapContainerLayoutBinding.inflate(inflater, parent, attachToParent)
        },
        update = {
            if (mapView == null) {
                mapView = this.mapView
            } else {
                mapController.updateMyLocationOverlay(mapUiState.myLocation)
                mapController.updateMemoMarkers(mapUiState.nearByMemoList,{ newSelectedMarkerId -> selectedMarkerId = newSelectedMarkerId })
                mapController.showInfoWindow(mapUiState.nearByMemoList,selectedMarkerId)
            }

        }
    )

}

@Composable
fun MapLifecycleEffect(context: Context, mapView: MapView?, myPosition: LatLng?, memoList: List<TodoItemData>, mapController: MapController, onMapLongClick: (TodoItemData)-> Unit, onMarkerClick: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                mapView?.let { mapView ->
                    mapView.onCreate(null)
                    mapView.getMapAsync { naverMap ->
                        mapController.initMapController(naverMap,context)
                        mapController.updateMyLocationOverlay(myPosition)
                        mapController.setMapLongClickListener(onMapLongClick)
                        mapController.updateMemoMarkers(memoList, onMarkerClick)
                    }
                }

            }

            override fun onStart(owner: LifecycleOwner) {
                mapView?.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView?.let { mapView ->
                    mapView.onResume()
                    mapController.setCameraPosition(myPosition)
                }
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