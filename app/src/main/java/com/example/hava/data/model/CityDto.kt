package com.example.hava.data.model

import com.google.gson.annotations.SerializedName

data class CitySearchResponse(
    val results: List<City>?
)

data class City(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("country_code")
    val countryCode: String? = null,
    val country: String? = null,
    val admin1: String? = null
) {
    val subtitle: String
        get() = listOfNotNull(admin1, country).distinct().joinToString(", ")
}

typealias CityDto = City
