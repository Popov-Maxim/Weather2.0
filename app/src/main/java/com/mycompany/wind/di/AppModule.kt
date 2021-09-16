package com.mycompany.wind.di

import android.content.Context
import androidx.room.Room
import com.mycompany.wind.api.YandexWeatherApi
import com.mycompany.wind.room.AppDatabase
import com.mycompany.wind.room.CityDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(var applicationContext: Context) {

    @Singleton
    @Provides
    fun provideYandexWeatherApi(okHttpClient: OkHttpClient): YandexWeatherApi {
        val retrofit = retrofit2.Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(YandexWeatherApi.BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofit.create(YandexWeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient()
    }


    @Singleton
    @Provides
    fun provideCityDao(): CityDao {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME_DATABASE
        )
            .build().cityDao()
    }
}