package com.mycompany.wind.model

import com.mycompany.wind.model.domain.City

interface CityRepository {
    suspend fun getCities(): List<City>
    suspend fun getCity(name: String): City
}