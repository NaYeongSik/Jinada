package com.youngsik.jinada.data.dataclass

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReverseGeocodeResponseDto(
    @Json(name = "status")
    val status: Status,
    @Json(name = "results")
    val results: List<GeocodeResultItem>
)

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "code")
    val code: Int,
    @Json(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
data class GeocodeResultItem(
    @Json(name = "name")
    val name: String, // "roadaddr" 또는 "addr" 같은 결과 종류
    @Json(name = "region")
    val region: RegionInfo, // 지역 정보
    @Json(name = "land")
    val land: LandInfo? // 도로명 주소 정보
)

@JsonClass(generateAdapter = true)
data class RegionInfo(
    @Json(name = "area1")
    val area1: AreaInfo, // 시/도
    @Json(name = "area2")
    val area2: AreaInfo, // 시/군/구
    @Json(name = "area3")
    val area3: AreaInfo, // 읍/면/동
    @Json(name = "area4")
    val area4: AreaInfo // 리
)
@JsonClass(generateAdapter = true)
data class AreaInfo(
    @Json(name = "name")
    val name: String, // 시/군/구/읍/면/동/리 이름
)

@JsonClass(generateAdapter = true)
data class LandInfo(
    @Json(name = "addition0")
    val addition0: AdditionInfo?, // 건물 정보
    @Json(name = "number1")
    val number1: String?, // 도로명 번지
    @Json(name = "number2")
    val number2: String?, // 도로명 혹은 지번 부번지
    @Json(name = "name")
    val name: String? // 도로명
)
@JsonClass(generateAdapter = true)
data class AdditionInfo(
    @Json(name = "value")
    val value: String?, // "롯데마트 맥스" 와 같은 실제 건물 이름
)