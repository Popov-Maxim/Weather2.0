package com.mycompany.wind.api

import com.mycompany.wind.model.domain.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface YandexWeatherApi {
    @Headers("X-Yandex-API-Key: 2aaf6307-4662-40d1-a590-edc3524b5d1e")
    @GET("v2/informers")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<Weather>

    companion object Factory {
        const val BASE_URL = "https://api.weather.yandex.ru"
    }
}