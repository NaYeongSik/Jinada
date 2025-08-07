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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.youngsik.domain.manager.ActivityRecognitionManager
import com.youngsik.jinada.presentation.R
import com.youngsik.jinada.presentation.theme.JinadaDimens
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel
import com.youngsik.jinada.presentation.viewmodel.SettingsViewModel.Companion.SUCCESSFUL_SET_USER_INFO
import java.util.UUID


@Composable
fun OnboardingScreen(settingsViewModel: SettingsViewModel, activityRecognitionManager: ActivityRecognitionManager, onSuccessOnboarding: () -> Unit){
    val context = LocalContext.current
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    var isDonePermmisionRequest by remember { mutableStateOf(false) }
    var showBackgroundPermissionRationale by remember { mutableStateOf(false) }

    // GPS 설정 요청 결과 처리
    val settingResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode != RESULT_OK) {
                Toast.makeText(context, "GPS 활성화가 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

    fun checkGpsAndStartService() {
        checkPhoneGpsSettings(
            context = context,
            onGpsEnabled = {
                isDonePermmisionRequest = true
            },
            onGpsSettingResolve = { intentSenderRequest ->
                // GPS를 켜야 하는 경우, 다이얼로그 요청
                settingResultLauncher.launch(intentSenderRequest)
            }
        )
    }

    val backgroundPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                // 권한이 부여되었는지 확인
                if (permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false))
                {
                    checkGpsAndStartService()
                    checkAndStartActivityRecognition(context,activityRecognitionManager)
                } else {
                    isDonePermmisionRequest = true
                }
            }
        )

    // 권한 요청 결과 처리
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                // 권한이 부여되었는지 확인
                if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACTIVITY_RECOGNITION, false) ||
                    permissions.getOrDefault(Manifest.permission.POST_NOTIFICATIONS, false)
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        showBackgroundPermissionRationale = true
                    } else {
                        checkGpsAndStartService()
                        checkAndStartActivityRecognition(context,activityRecognitionManager)
                    }

                } else {
                    Toast.makeText(context, "정상적인 이용을 위해선 모든 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        )

    // 화면이 처음 렌더링될 때 권한 상태 확인 및 요청
    LaunchedEffect(Unit) {
        val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )

        val hasLocationPermission = locationPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        val hasActivityPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        val hasBackgroundPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        if (hasLocationPermission && hasActivityPermission && hasNotificationPermission && hasBackgroundPermission) {
            // 이미 권한이 있으면 GPS 확인 및 서비스 시작
            checkAndStartActivityRecognition(context,activityRecognitionManager)
            onSuccessOnboarding()
        } else {
            // 권한이 없으면 요청
            val permissionsToRequest = mutableListOf(*locationPermissions)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
            }
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }


    if (isDonePermmisionRequest) NicknameInputDialog{ nickname ->
        val uuid = UUID.randomUUID().toString()
        settingsViewModel.setUserInfo(nickname,uuid)
    }

    LaunchedEffect(settingsUiState.lastSuccessfulAction) {
        if (settingsUiState.lastSuccessfulAction == SUCCESSFUL_SET_USER_INFO){
            settingsViewModel.resetLastSuccessfulAction()
            onSuccessOnboarding()
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
                isDonePermmisionRequest = true
            }
        )
    }
}

@Composable
fun NicknameInputDialog(onSuccess: (String) -> Unit) {
    var nickname by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { /* 닫기 방지 */ }) {
        Card {
            Column(modifier = Modifier.padding(JinadaDimens.Padding.medium)) {
                Text(text= stringResource(R.string.request_nickname_title),style = MaterialTheme.typography.bodyMedium)
                TextField(value = nickname, onValueChange = { nickname = it }) // TODO: 닉네임 중복 체크 필요
                Row(
                    modifier = Modifier.padding(top = JinadaDimens.Padding.medium).align(Alignment.End)
                ) {
                    Button(onClick = { onSuccess(nickname) }) {
                        Text(text= stringResource(R.string.button_save), style = MaterialTheme.typography.bodyMedium) // TODO: 닉네임 중복이 아닐때만 활성화 필요
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

@RequiresPermission(anyOf = [Manifest.permission.ACTIVITY_RECOGNITION, "com.google.android.gms.permission.ACTIVITY_RECOGNITION"])
fun checkAndStartActivityRecognition(context: Context, activityRecognitionManager: ActivityRecognitionManager) {
    val permissionToCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACTIVITY_RECOGNITION else "com.google.android.gms.permission.ACTIVITY_RECOGNITION"

    if (ActivityCompat.checkSelfPermission(
            context,
            permissionToCheck
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        activityRecognitionManager.startActivityRecognition()
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