package com.youngsik.jinada.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.map.MapView
import com.youngsik.domain.entity.TodoItemData
import com.youngsik.jinada.presentation.uistate.MapUiState


@Composable
fun NaverMapView(
    mapView: MapView,
    mapController: MapController,
    mapUiState: MapUiState,
    onMapLongClick: (TodoItemData) -> Unit,
) {
    var selectedMarkerId by remember { mutableStateOf("") }

    AndroidView(
        factory = {
            mapView.getMapAsync { naverMap ->
                with(mapController) {
                    initMapController(naverMap)
                    updateMyLocationOverlay(mapUiState.myLocation)
                    setCameraPosition(mapUiState.myLocation)
                    setMapLongClickListener(onMapLongClick)
                    updateMemoMarkers(
                        mapUiState.nearByMemoList,
                        { newSelectedMarkerId -> selectedMarkerId = newSelectedMarkerId })
                }
            }
            mapView
        },
        modifier = Modifier,
        update = {
            with(mapController) {
                updateMyLocationOverlay(mapUiState.myLocation)
                updateMemoMarkers(mapUiState.nearByMemoList) { newSelectedMarkerId ->
                    selectedMarkerId = newSelectedMarkerId
                }
                showInfoWindow(mapUiState.nearByMemoList, selectedMarkerId)
            }
        }
    )
}
