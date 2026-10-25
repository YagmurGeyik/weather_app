package com.example.hava.data.repository

import com.example.hava.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse
}
