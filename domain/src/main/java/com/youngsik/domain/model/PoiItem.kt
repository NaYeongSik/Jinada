package com.youngsik.domain.model

data class PoiItem(
    val buildingName: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val roadAddress: String = ""
)