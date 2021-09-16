package com.mycompany.wind.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mycompany.wind.model.CityRepository
import com.mycompany.wind.model.CityRepositoryImpl
import com.mycompany.wind.model.WeatherRepository
import com.mycompany.wind.model.WeatherRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityRepository: CityRepository
) : ViewModel() {
    @Singleton
    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val weatherRepository: WeatherRepositoryImpl,
        private val cityRepository: CityRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(weatherRepository, cityRepository) as T
        }
    }

    private var _cityState: MutableStateFlow<CityState> =
        MutableStateFlow(CityState.Empty)
    val cityState: StateFlow<CityState> = _cityState

    private var _weatherState: MutableStateFlow<WeatherState> =
        MutableStateFlow(WeatherState.Empty)
    val weatherState: StateFlow<WeatherState> = _weatherState


    init {
        getCityNames()
    }

    fun getWeatherFromCity(name: String) = viewModelScope.launch {
        _weatherState.value = WeatherState.Wait
        cityRepository.getCity(name)
            .also { it.weather = weatherRepository.getWeather(it) }
            .let {
                _weatherState.value = WeatherState.Success(it)
            }
    }

    fun getCityNames() =
        viewModelScope.launch {
            cityRepository.getCities()
                .let { cities -> _cityState.value = CityState.Success(cities) }
        }

}