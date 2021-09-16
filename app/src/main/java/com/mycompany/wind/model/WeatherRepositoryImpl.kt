package com.mycompany.wind.model

import com.mycompany.wind.api.YandexWeatherApi
import com.mycompany.wind.model.domain.City
import com.mycompany.wind.model.domain.Weather
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApi: YandexWeatherApi) :
    WeatherRepository {
    override suspend fun getWeather(city: City): Weather {
        return city.run { weatherApi.getWeather(lat, lon).body()!! }
    }
}