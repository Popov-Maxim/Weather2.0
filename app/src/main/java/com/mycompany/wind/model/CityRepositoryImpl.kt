package com.mycompany.wind.model

import com.mycompany.wind.model.domain.City
import com.mycompany.wind.room.CityDao
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val cityDao: CityDao) : CityRepository {
    override suspend fun getCities(): List<City> {
        return cityDao.getAll()
    }

    override suspend fun getCity(name: String): City {
        return cityDao.getByName(name)
    }
}