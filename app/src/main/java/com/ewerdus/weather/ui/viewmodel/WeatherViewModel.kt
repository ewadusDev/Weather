package com.ewerdus.weather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ewerdus.weather.data.current.WeatherCurrent
import com.ewerdus.weather.data.onecall.Current
import com.ewerdus.weather.data.onecall.WeatherData
import com.ewerdus.weather.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private val repository: WeatherRepository):ViewModel() {

    private var _weatherCurrentResponse = MutableLiveData<WeatherData>()
            val weatherCurrentResponse:LiveData<WeatherData> get() = _weatherCurrentResponse

    private var _weatherCurrentResponseErrorMsg = MutableLiveData<String>()
    val weatherCurrentResponseErrorMsg: LiveData<String> get() = _weatherCurrentResponseErrorMsg

    private var _cityResponse = MutableLiveData<WeatherCurrent>()
    val cityResponse:LiveData<WeatherCurrent> get() = _cityResponse


    fun getWeatherCurrent(lat:String,lon:String) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(lat, lon)
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    _weatherCurrentResponse.value = response.body()
                    Log.i("response","${response.body()}")
                }else{
                    _weatherCurrentResponseErrorMsg.value = response.message()
                    Log.i("responseError","${response.message()}")
                }
            }
        }
    }


    fun getCityName(lat: String,lon: String) {
        viewModelScope.launch {
            val response = repository.getCityName(lat, lon)
            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    _cityResponse.value = response.body()
                }else{
                    _weatherCurrentResponseErrorMsg.value = response.message()
                }
            }
        }

    }
}