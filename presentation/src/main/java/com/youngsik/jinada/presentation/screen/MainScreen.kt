package com.youngsik.jinada.presentation.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.data.dataclass.TodoItemData
import com.youngsik.jinada.data.service.CurrentLocationService
import com.youngsik.jinada.data.utils.changeToStringDate
import com.youngsik.jinada.presentation.MemoMockData
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.component.CommonLazyColumnCard
import com.youngsik.jinada.presentation.component.MapSearchBar
import com.youngsik.jinada.presentation.component.MyLocationButton
import com.youngsik.jinada.presentation.map.NaverMapView
import com.youngsik.jinada.presentation.map.rememberMapController
import com.youngsik.jinada.presentation.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.MemoMapViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(memoMapViewModel: MemoMapViewModel, onCreateMemoClick: (TodoItemData)-> Unit, onMemoUpdateClick: (TodoItemData)-> Unit){
    val context = LocalContext.current
    val mapUiState by memoMapViewModel.mapUiState.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState()
    var mapController = rememberMapController()

    LaunchedEffect(Unit) {
        startLocationService(context)
        memoMapViewModel.observeCurrentLocation()
    }

    LaunchedEffect(mapUiState.cameraPosition) {
        mapController.setCameraPosition(mapUiState.cameraPosition)
    }

    DisposableEffect(Unit) {
        onDispose {
            context.stopService(Intent(context, CurrentLocationService::class.java))
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = JinadaDimens.Common.xxLarge,
        sheetContent = {
            Column(modifier = Modifier.fillMaxHeight(0.7f) ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(R.string.nearby_memos_title),
                        modifier = Modifier.align(Alignment.Start).padding(start = JinadaDimens.Padding.large, bottom = JinadaDimens.Padding.xSmall),
                        style = MaterialTheme.typography.titleMedium
                    )
                    CommonLazyColumnCard(modifier = Modifier.weight(1f).fillMaxWidth(0.9f), memoList = mapUiState.nearByMemoList, onCheckChange = {
                            item, isChecked ->
                        val index = mapUiState.nearByMemoList.indexOf(item)
                        if (index != -1) {
                            memoMapViewModel.updateMemo(item.copy(isCompleted = isChecked, completeDate = if (isChecked) changeToStringDate(LocalDate.now()) else null))
                        }
                    }, { todoItemData ->  onMemoUpdateClick(todoItemData) }, { item -> memoMapViewModel.deleteMemo(item.memoId) } )
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){

            NaverMapView(mapController,mapUiState,onMapLongClick = onCreateMemoClick)

            MapSearchBar(Modifier.align(Alignment.TopCenter)
                .padding(JinadaDimens.Padding.medium)
                .fillMaxWidth(0.9f)
                ,inputText,{ it -> inputText = it})

            MyLocationButton(Modifier
                .align(Alignment.BottomEnd)
                .padding(JinadaDimens.Padding.medium)
                ,{ mapUiState.myLocation?.let { mapController.moveToTargetLocation(it) } })
        }
    }

}

fun startLocationService(context: Context) {
    val target = Intent(context, CurrentLocationService::class.java)
    context.startForegroundService(target)
}