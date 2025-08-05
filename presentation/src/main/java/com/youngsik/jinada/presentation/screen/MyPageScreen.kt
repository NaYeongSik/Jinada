package com.youngsik.jinada.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.common.SettingDialogState
import com.youngsik.jinada.presentation.component.DialogView
import com.youngsik.jinada.presentation.component.SettingsSection
import com.youngsik.jinada.presentation.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel


@Composable
fun MyPageScreen(settingsViewModel: SettingsViewModel){
    var dialogRoute by remember { mutableStateOf(SettingDialogState.NONE) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        DialogView(dialogRoute,{ newRoute -> dialogRoute = newRoute })

        ProfileSection()

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsSection({newRoute -> dialogRoute = newRoute})
        }
    }
}


@Composable
fun ProfileSection(){
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.profile_sample),
            contentDescription = stringResource(R.string.profile_image_content_description),
            modifier = Modifier.size(JinadaDimens.Common.xLarge)
        )
        Text(text = stringResource(R.string.profile_name_format,"김메모","723552"), // TODO: 유저 정보 가져오기
            modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge)
    }
}


