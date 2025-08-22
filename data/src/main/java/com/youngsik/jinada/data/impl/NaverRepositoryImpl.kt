package com.youngsik.jinada.data.impl

import com.youngsik.domain.entity.PoiItem
import com.youngsik.jinada.data.mapper.toDomainModel
import com.youngsik.jinada.data.repository.NaverApiRepository
import com.youngsik.jinada.data.repository.NaverRepository
import javax.inject.Inject
import javax.inject.Named

class NaverRepositoryImpl @Inject constructor(@Named("mapApi") private val mapApi: NaverApiRepository, @Named("searchApi") private val searchApi: NaverApiRepository): NaverRepository {
    
    companion object{
        const val ROADADDR = "roadaddr" // 도로명 주소
        const val ADDR = "addr" // 지번 주소
    }

    override suspend fun getAddressFromCoordinates(
        clientId: String,
        clientSecret: String,
        coords: String
    ): String? {
        val response = mapApi.getAddressFromCoordinates(clientId, clientSecret, coords)
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
        val response = searchApi.getPoiFromInputString(clientId, clientSecret, query)

        return response.toDomainModel()
    }
}