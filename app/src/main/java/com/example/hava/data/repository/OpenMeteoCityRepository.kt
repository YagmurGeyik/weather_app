package com.example.hava.data.repository

import com.example.hava.data.model.CityDto
import com.example.hava.data.remote.CityApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenMeteoCityRepository(
    private val api: CityApi
) : CityRepository {
    override suspend fun search(query: String): List<CityDto> {
        return withContext(Dispatchers.IO) {
            val response = api.search(query.trim())
            response.results.orEmpty()
        }
    }
}
