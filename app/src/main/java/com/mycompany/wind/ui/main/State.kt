package com.mycompany.wind.ui.main

import com.mycompany.wind.model.domain.City

sealed class CityState {
    object Empty : CityState()
    data class Success(val cities: List<City>) : CityState()
    data class Error(val exception: Throwable) : CityState()
}

sealed class WeatherState {
    object Empty : WeatherState()
    data class Success(val city: City) : WeatherState()
    object Wait : WeatherState()
    data class Error(val exception: Throwable) : WeatherState()
}