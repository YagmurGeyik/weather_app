package com.example.hava.data.remote

import com.example.hava.data.model.CitySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("v1/search")
    suspend fun search(
        @Query("name") name: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "tr"
    ): CitySearchResponse
}
