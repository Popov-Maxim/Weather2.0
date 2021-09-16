package com.mycompany.wind.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mycompany.wind.model.domain.City

@Database(entities = [City::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao

    companion object {
        const val NAME_DATABASE: String = "database.db"
    }
}
