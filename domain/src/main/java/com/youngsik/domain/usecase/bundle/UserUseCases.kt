package com.youngsik.domain.usecase.bundle

import com.youngsik.domain.usecase.CheckNicknameUseCase
import com.youngsik.domain.usecase.InitSettingsUseCase
import com.youngsik.domain.usecase.SaveUserInfoUseCase
import com.youngsik.domain.usecase.UpdateNotificationSettingsUseCase
import com.youngsik.domain.usecase.UpdateRangeSettingsUseCase
import javax.inject.Inject

/**
 * 사용자 정보 및 앱 설정 관련 모든 유즈케이스를 하나로 묶어주는 번들 클래스입니다.
 */
class UserUseCases @Inject constructor(
    val checkNickname: CheckNicknameUseCase,
    val saveUserInfo: SaveUserInfoUseCase,
    val initSettings: InitSettingsUseCase,
    val updateNotificationSettings: UpdateNotificationSettingsUseCase,
    val updateRangeSettings: UpdateRangeSettingsUseCase
)
