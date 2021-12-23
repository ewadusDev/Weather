package com.ewerdus.weather.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ewerdus.weather.repo.WeatherRepository

class WeatherViewModelProviderFactory(private val repository: WeatherRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            WeatherViewModel(this.repository) as T
        }else{
            throw IllegalAccessException("ViewModel Is Not Founded")
        }
    }

}