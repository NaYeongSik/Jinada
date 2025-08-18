package com.youngsik.jinada.data.mapper

import com.youngsik.domain.entity.PoiItem
import com.youngsik.jinada.data.dataclass.LocalSearchResponseDto
import com.youngsik.shared.utils.removeHtmlTags

fun LocalSearchResponseDto.toDomainModel(): List<PoiItem> =
    items.map { item ->
        PoiItem(
            buildingName = item.title.removeHtmlTags(),
            latitude = item.mapy.toDouble() / 1e7,
            longitude = item.mapx.toDouble() / 1e7,
            roadAddress = item.roadAddress
        )
    }
