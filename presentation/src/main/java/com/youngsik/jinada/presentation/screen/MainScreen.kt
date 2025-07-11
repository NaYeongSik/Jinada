package com.youngsik.jinada.presentation.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.naver.maps.geometry.LatLng
import com.youngsik.jinada.presentation.MemoMockData
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.component.CommonLazyColumnCard
import com.youngsik.jinada.presentation.component.MapSearchBar
import com.youngsik.jinada.presentation.component.MyLocationButton
import com.youngsik.jinada.presentation.map.NaverMapView
import com.youngsik.jinada.presentation.map.rememberMapController
import com.youngsik.jinada.presentation.theme.JinadaDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onCreateMemoClick: (String)-> Unit, onMemoUpdateClick: (Int)-> Unit){
    val memoList = remember { mutableStateListOf(*MemoMockData.getMemosNearby().toTypedArray()) }
    var inputText by remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState()
    var mapController = rememberMapController()

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
                    CommonLazyColumnCard(modifier = Modifier.weight(1f).fillMaxWidth(0.9f), memoList = memoList, onCheckChange = {
                            item, isChecked -> // TODO: 해당 메모 데이터 완료 처리 필요
                        val index = memoList.indexOf(item)
                        if (index != -1) {
                            memoList[index] = item.copy(isCompleted = isChecked)
                        }
                    }, { onMemoUpdateClick(1) /*TODO: 메모 생성화면으로 해당 데이터 가지고 이동 */ }, { item -> memoList.remove(item)  /*TODO: 메모 데이터 삭제 처리 필요*/ } )
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            MapSearchBar(Modifier.align(Alignment.TopCenter)
                .padding(JinadaDimens.Padding.medium)
                .fillMaxWidth(0.9f)
                ,inputText,{ it -> inputText = it})
            NaverMapView(mapController,memoList,onMapLongClick = onCreateMemoClick)

            MyLocationButton(Modifier
                .align(Alignment.BottomEnd)
                .padding(JinadaDimens.Padding.medium)
                ,{ mapController.moveToTargetLocation(LatLng(37.472336,126.895997))})
        }
    }

}