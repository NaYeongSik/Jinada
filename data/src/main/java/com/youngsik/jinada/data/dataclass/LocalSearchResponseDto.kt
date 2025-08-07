package com.youngsik.jinada.data.dataclass

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalSearchResponseDto(
    @Json(name = "items")
    val items: List<LocalSearchItem> // 검색 결과 아이템 목록
)

@JsonClass(generateAdapter = true)
data class LocalSearchItem(
    @Json(name = "title")
    val title: String, // 장소 이름 (HTML 태그 포함될 수 있음)

    @Json(name = "category")
    val category: String, // 장소 카테고리

    @Json(name = "address")
    val address: String, // 주소

    @Json(name = "roadAddress")
    val roadAddress: String, // 도로명 주소

    @Json(name = "mapx")
    val mapx: String,

    @Json(name = "mapy")
    val mapy: String
)