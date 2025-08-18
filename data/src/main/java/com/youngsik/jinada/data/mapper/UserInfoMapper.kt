package com.youngsik.jinada.data.mapper

import com.youngsik.domain.entity.UserInfo
import com.youngsik.jinada.data.dataclass.UserInfoDto

fun UserInfo.toDto(): UserInfoDto = UserInfoDto(this.uuid,this.nickname)
fun UserInfoDto.toDomainModel(): UserInfo = UserInfo(this.uuid,this.nickname)