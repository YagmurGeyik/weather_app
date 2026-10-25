package com.example.hava.data.repository

import com.example.hava.data.model.CityDto

interface CityRepository {
    suspend fun search(query: String): List<CityDto>
}
