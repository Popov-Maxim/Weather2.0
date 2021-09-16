package com.mycompany.wind.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mycompany.wind.model.domain.City

@Dao
interface CityDao {
    @Insert
    suspend fun insert(vararg city: City)

    @Delete
    suspend fun delete(city: City)

    @Query("SELECT * From City")
    suspend fun getAll(): List<City>

    @Query("SELECT * FROM City WHERE name LIKE :name")
    suspend fun getByName(name: String): City
}