package com.example.hava.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val GEO_BASE_URL = "https://geocoding-api.open-meteo.com/"
    private const val WEATHER_BASE_URL = "https://api.open-meteo.com/"

    // OkHttp loglama: tüm istek/yanıt body'sini Logcat'e yazar (tag: OkHttp)
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        android.util.Log.d("OkHttp", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val cityApi: CityApi by lazy {
        Retrofit.Builder()
            .baseUrl(GEO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CityApi::class.java)
    }

    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}
