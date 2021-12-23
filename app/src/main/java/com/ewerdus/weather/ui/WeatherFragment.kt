package com.ewerdus.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ewerdus.weather.R
import com.ewerdus.weather.databinding.FragmentWeatherBinding
import com.ewerdus.weather.network.WeatherAPIClient
import com.ewerdus.weather.network.WeatherCurrentInterface
import com.ewerdus.weather.repo.WeatherRepository
import com.ewerdus.weather.ui.viewmodel.WeatherViewModel
import com.ewerdus.weather.ui.viewmodel.WeatherViewModelProviderFactory
import com.ewerdus.weather.util.Constants.Companion.BASE_URL_WEATHER
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.format


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        val weatherAPIClient = WeatherAPIClient.weatherAPI
        val weatherRepository = WeatherRepository(weatherAPIClient)
        weatherViewModel = ViewModelProvider(
            this, WeatherViewModelProviderFactory(weatherRepository)
        ).get(WeatherViewModel::class.java)

        currentLocation()

        weatherViewModel.weatherCurrentResponse.observe(viewLifecycleOwner, Observer {
            val tempFormatted = String.format("%.0f", it.current.temp)
            binding.tvTempCurrent.text = "$tempFormatted°C"
            val imgFormatted =
                "https://openweathermap.org/img/wn/${it.current.weather[0].icon}@2x.png"
            Glide.with(this).load(imgFormatted).into(binding.imgWeatherCurrent)
            binding.tvImgDes.text = it.current.weather[0].description.also {
            }



        })

        weatherViewModel.cityResponse.observe(viewLifecycleOwner, Observer {
            binding.tvCityName.text = it.name
            val tempHighFormatted = String.format("%.0f", it.main.temp_max)
            val tempLowFormatted = String.format("%.0f", it.main.temp_min)
            binding.tvHighTempCurrent.text = "H: $tempHighFormatted°"
            binding.tvLowTempCurrent.text = "L: $tempLowFormatted°"

        })

        weatherViewModel.weatherCurrentResponseErrorMsg.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        })



        return binding.root
    }

    private fun currentLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            weatherViewModel.getWeatherCurrent(it.latitude.toString(), it.longitude.toString())
            weatherViewModel.getCityName(it.latitude.toString(), it.longitude.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}