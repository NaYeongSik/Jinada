package com.youngsik.jinada.data.impl

import android.util.Log
import com.youngsik.jinada.data.repository.NaverRepository
import com.youngsik.jinada.data.datasource.remote.NaverApiClient

class NaverRepositoryImpl(): NaverRepository {
    val apiClient = NaverApiClient.naverApi

    override suspend fun getAddressFromCoordinates(
        clientId: String,
        clientSecret: String,
        coords: String
    ): String? {
        val response = apiClient.getAddressFromCoordinates(clientId, clientSecret, coords)
        Log.d("jinada_test", "getAddressFromCoordinates Status Code: ${response.status.code}")
        if (response.status.code != 0 || response.results.isEmpty()) {
            return null
        }
        Log.d("jinada_test", "getAddressFromCoordinates Response: ${response.results.size}")
        val roadAddressResult = response.results.firstOrNull { it.name == "roadaddr" }

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
            val jibunAddressResult = response.results.firstOrNull { it.name == "addr" }

            finalAddress = jibunAddressResult?.region?.let {
                "${it.area1.name} ${it.area2.name} ${it.area3.name} ${it.area4.name} ${jibunAddressResult.land?.number1}"
            }
        }

        return finalAddress
    }
}