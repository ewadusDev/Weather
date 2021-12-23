package com.ewerdus.weather.data.onecall

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)