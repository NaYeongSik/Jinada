package com.youngsik.jinada.presentation.screen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.youngsik.jinada.presentation.uistate.SettingsUiState
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel.Companion.SUCCESSFUL_CHECK_NICKNAME
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel.Companion.SUCCESSFUL_CREATE_USER_INFO
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel.Companion.SUCCESSFUL_INIT_SETTINGS
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel.Companion.SUCCESSFUL_SET_USER_INFO
import com.youngsik.shared.R
import com.youngsik.shared.theme.JinadaDimens
import kotlinx.coroutines.delay
import java.util.UUID


@Composable
fun OnboardingScreen(settingsViewModel: SettingsViewModel, onSuccessOnboarding: () -> Unit){
    val context = LocalContext.current
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    var needInputNickname by remember { mutableStateOf(false) }
    var showBackgroundPermissionRationale by remember { mutableStateOf(false) }

    val settingResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(context, "GPS 활성화가 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    fun checkGpsAndStartService() {
        checkPhoneGpsSettings(
            context = context,
            onGpsEnabled = { needInputNickname = true },
            onGpsSettingResolve = { settingResultLauncher.launch(it) }
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val locationGranted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            val activityGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                perms[Manifest.permission.ACTIVITY_RECOGNITION] == true
            } else true
            val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                perms[Manifest.permission.POST_NOTIFICATIONS] == true
            } else true

            if (locationGranted && activityGranted && notificationGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) showBackgroundPermissionRationale = true
                else {
                    if (settingsUiState.nickname.isNotBlank()){
                        checkGpsAndStartService()
                        checkAndStartActivityRecognition(context, settingsViewModel)
                    }
                    else needInputNickname = true
                }
            } else {
                Toast.makeText(context, "정상적인 이용을 위해 모든 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    val backgroundPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            if (perms[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true) {
                checkGpsAndStartService()
                checkAndStartActivityRecognition(context, settingsViewModel)
            } else {
                needInputNickname = true
            }
        }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.jinada_logo),
            contentDescription = "",
            modifier = Modifier.size(JinadaDimens.Common.xxxLarge)
        )

        if (needInputNickname) {
            NicknameInputDialog(settingsViewModel, settingsUiState) { nickname ->
                val uuid = UUID.randomUUID().toString()
                settingsViewModel.createUserInfoInFirestore(nickname, uuid)
            }
        }

        LaunchedEffect(settingsUiState.lastSuccessfulAction) {
            when (settingsUiState.lastSuccessfulAction) {
                SUCCESSFUL_INIT_SETTINGS -> {
                    val permissions = mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) permissions.add(Manifest.permission.POST_NOTIFICATIONS)

                    val granted = permissions.all {
                        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                    }

                    if (granted){
                        if (settingsUiState.nickname.isNotBlank()) {
                            checkAndStartActivityRecognition(context, settingsViewModel)
                            settingsViewModel.resetLastSuccessfulAction()
                            onSuccessOnboarding()
                        } else {
                            needInputNickname = true
                        }
                    }
                    else permissionLauncher.launch(permissions.toTypedArray())

                }
                SUCCESSFUL_CREATE_USER_INFO -> {
                    settingsViewModel.setUserInfo(settingsUiState.nickname, settingsUiState.uuid)
                }
                SUCCESSFUL_SET_USER_INFO -> {
                    settingsViewModel.resetLastSuccessfulAction()
                    onSuccessOnboarding()
                }
            }
        }

        if (showBackgroundPermissionRationale) {
            PermissionRationaleDialog(
                onConfirm = {
                    showBackgroundPermissionRationale = false
                    backgroundPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                },
                onDismiss = {
                    showBackgroundPermissionRationale = false
                    if(settingsUiState.nickname.isNotBlank()) {
                        checkAndStartActivityRecognition(context, settingsViewModel)
                        settingsViewModel.resetLastSuccessfulAction()
                        onSuccessOnboarding()
                    }
                    else needInputNickname = true
                }
            )
        }
    }

}

@Composable
fun NicknameInputDialog(settingsViewModel: SettingsViewModel,settingsUiState: SettingsUiState,onSuccess: (String) -> Unit) {
    var nickname by remember { mutableStateOf("") }

    LaunchedEffect(nickname) {
        if (nickname.isNotBlank()) {
            delay(300L)
            settingsViewModel.checkNicknameExists(nickname.trim())
        } else {
            settingsViewModel.resetNicknameAvailable()
        }
    }

    Dialog(onDismissRequest = { /* 닫기 방지 */ }) {
        Card {
            Column(modifier = Modifier.padding(JinadaDimens.Padding.medium)) {
                Text(text= stringResource(R.string.request_nickname_title),style = MaterialTheme.typography.bodyMedium)
                TextField(value = nickname, onValueChange = { nickname = it })
                if (settingsUiState.lastSuccessfulAction == SUCCESSFUL_CHECK_NICKNAME){
                    if (settingsUiState.isNicknameAvailable) Text(text= stringResource(R.string.nickname_is_not_exists), style = MaterialTheme.typography.bodyMedium)
                    else Text(text= stringResource(R.string.nickname_already_exists), style = MaterialTheme.typography.bodyMedium)
                }

                Row(
                    modifier = Modifier.padding(top = JinadaDimens.Padding.medium).align(Alignment.End)
                ) {
                    Button(onClick = { onSuccess(nickname) }, enabled = settingsUiState.isNicknameAvailable && !settingsUiState.isLoading) {
                        Text(text= stringResource(R.string.button_save), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}


fun checkPhoneGpsSettings(
    context: Context,
    onGpsEnabled: () -> Unit,
    onGpsSettingResolve: (IntentSenderRequest) -> Unit
) {
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
        1000).build()

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client: SettingsClient = LocationServices.getSettingsClient(context)

    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener {
        onGpsEnabled()
    }

    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                onGpsSettingResolve(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }
}

@RequiresPermission(Manifest.permission.ACTIVITY_RECOGNITION)
fun checkAndStartActivityRecognition(context: Context, settingsViewModel: SettingsViewModel) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) settingsViewModel.startActivityRecognition()
    } else {
        settingsViewModel.startActivityRecognition()
    }
}

@Composable
fun PermissionRationaleDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.background_location_permission_title),style = MaterialTheme.typography.bodyMedium) },
        text = { Text(stringResource(R.string.background_location_permission_description),style = MaterialTheme.typography.bodyMedium) },
        confirmButton = { Button(onClick = onConfirm) { Text(stringResource(R.string.move_to_settings)) } },
        dismissButton = { Button(onClick = onDismiss) { Text(stringResource(R.string.move_to_main)) } }
    )
}