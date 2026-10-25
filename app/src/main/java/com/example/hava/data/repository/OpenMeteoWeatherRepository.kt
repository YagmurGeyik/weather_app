package com.example.hava.data.repository

import com.example.hava.data.model.WeatherResponse
import com.example.hava.data.remote.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenMeteoWeatherRepository(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            api.getWeather(latitude, longitude)
        }
    }
}
