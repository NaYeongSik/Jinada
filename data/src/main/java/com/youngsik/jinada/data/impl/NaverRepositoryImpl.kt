package com.youngsik.jinada.data.impl

import com.youngsik.domain.entity.PoiItem
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.datasource.remote.NaverApiClient
import com.youngsik.jinada.data.mapper.toDomainModel

class NaverRepositoryImpl(): NaverRepository {
    
    companion object{
        const val ROADADDR = "roadaddr" // 도로명 주소
        const val ADDR = "addr" // 지번 주소
    }
    val MapApiClient = NaverApiClient.naverMapApi
    val SearchApiClient = NaverApiClient.naverSearchApi

    override suspend fun getAddressFromCoordinates(
        clientId: String,
        clientSecret: String,
        coords: String
    ): String? {
        val response = MapApiClient.getAddressFromCoordinates(clientId, clientSecret, coords)
        if (response.status.code != 0 || response.results.isEmpty()) {
            return null
        }
        val roadAddressResult = response.results.firstOrNull { it.name == ROADADDR }

        var finalAddress: String? = null

        if (roadAddressResult != null){
            val region = roadAddressResult.region
            val land = roadAddressResult.land

            val roadAddressName = "${region.area1.name} ${region.area2.name} ${region.area3.name} ${region.area4.name} ${land?.number1}"
            val buildingName = land?.addition0?.value

            finalAddress = if (!buildingName.isNullOrBlank()) {
                buildingName
            } else {
                roadAddressName
            }
        } else {
            val jibunAddressResult = response.results.firstOrNull { it.name == ADDR }

            finalAddress = jibunAddressResult?.region?.let {
                "${it.area1.name} ${it.area2.name} ${it.area3.name} ${it.area4.name} ${jibunAddressResult.land?.number1}"
            }
        }

        return finalAddress
    }

    override suspend fun getPoiFromInputString(
        clientId: String,
        clientSecret: String,
        query: String
    ): List<PoiItem> {
        val response = SearchApiClient.getPoiFromInputString(clientId, clientSecret, query)

        return response.toDomainModel()
    }
}