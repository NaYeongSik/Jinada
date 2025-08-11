package com.youngsik.jinada.data.dataclass

import com.google.firebase.firestore.PropertyName

data class UserInfoDto(
    @get:PropertyName("uuid") @set:PropertyName("uuid")
    var uuid: String = "",
    @get:PropertyName("nickname") @set:PropertyName("nickname")
    var nickname: String = "",
)