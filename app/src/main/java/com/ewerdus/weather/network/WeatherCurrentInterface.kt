package com.ewerdus.weather.network

import com.ewerdus.weather.data.current.WeatherCurrent
import com.ewerdus.weather.data.onecall.WeatherData
import com.ewerdus.weather.util.Constants.Companion.WEATHER_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherCurrentInterface {
    @GET("data/2.5/onecall")
     suspend fun getCurrentWeather(
        @Query("lat")
        lat: String ,
        @Query("lon")
        lon: String ,
        @Query("units")
        units:String = "metric",
        @Query("appid")
        appid: String = WEATHER_API_KEY
    ): Response<WeatherData>

     @GET("data/2.5/weather")
     suspend fun getCityName(
         @Query("lat")
         lat: String ,
         @Query("lon")
         lon: String ,
         @Query("units")
         units:String = "metric",
         @Query("appid")
         appid: String = WEATHER_API_KEY
     ): Response<WeatherCurrent>
}