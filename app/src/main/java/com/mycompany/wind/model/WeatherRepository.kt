package com.mycompany.wind.model

import com.mycompany.wind.model.domain.City
import com.mycompany.wind.model.domain.Weather

interface WeatherRepository {
    suspend fun getWeather(city: City): Weather
}