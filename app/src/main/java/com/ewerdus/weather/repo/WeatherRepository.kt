package com.ewerdus.weather.repo

import com.ewerdus.weather.network.WeatherCurrentInterface

class WeatherRepository(private val weatherCurrentAPIInterface: WeatherCurrentInterface) {

    suspend fun getCurrentWeather(lat: String, lon: String) =
        weatherCurrentAPIInterface.getCurrentWeather(lat, lon)

    suspend fun getCityName(lat: String, lon: String) =
        weatherCurrentAPIInterface.getCityName(lat, lon)

}