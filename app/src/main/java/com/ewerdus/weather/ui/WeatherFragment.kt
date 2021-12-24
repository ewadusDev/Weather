package com.ewerdus.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ewerdus.weather.R
import com.ewerdus.weather.adapter.HourlyAdapter
import com.ewerdus.weather.adapter.SevenDaysAdapter
import com.ewerdus.weather.data.onecall.Daily
import com.ewerdus.weather.data.onecall.Hourly
import com.ewerdus.weather.data.onecall.WeatherData
import com.ewerdus.weather.databinding.FragmentWeatherBinding
import com.ewerdus.weather.network.WeatherAPIClient
import com.ewerdus.weather.repo.WeatherRepository
import com.ewerdus.weather.ui.viewmodel.WeatherViewModel
import com.ewerdus.weather.ui.viewmodel.WeatherViewModelProviderFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sevenDaysAdapter: SevenDaysAdapter
    private lateinit var hourlyAdapter: HourlyAdapter

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

        weatherViewModel.sevenDaysResponse.observe(viewLifecycleOwner, Observer {
            setupForecastRecyclerView(it)
        })

        weatherViewModel.hourlyResponse.observe(viewLifecycleOwner, Observer {
            setupHourlyRecyclerView(it)
        })



        weatherViewModel.weatherCurrentResponse.observe(viewLifecycleOwner, Observer {
            val tempFormatted = String.format("%.0f", it.current.temp)
            binding.tvTempCurrent.text = "$tempFormatted°c"
            val imgFormatted =
                "https://openweathermap.org/img/wn/${it.current.weather[0].icon}@2x.png"
            Glide.with(this).load(imgFormatted).into(binding.imgWeatherCurrent)
            binding.tvImgDes.text = it.current.weather[0].description

            setImgBackground(it)


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

    private fun setImgBackground(responseWeather: WeatherData) {
        when (responseWeather.current.weather[0].icon) {
            "01d" -> binding.root.setBackgroundResource(
                R.drawable.clear_sky_day
            )
            "01n" -> binding.root.setBackgroundResource(
                R.drawable.clear_sky_night
            )
            "02d" -> binding.root.setBackgroundResource(
                R.drawable.few_clouds_day
            )
            "02n" -> binding.root.setBackgroundResource(
                R.drawable.few_clouds_night
            )
            "03d" -> binding.root.setBackgroundResource(
                R.drawable.scattered_clouds_day
            )
            "03n" -> binding.root.setBackgroundResource(
                R.drawable.scattered_clouds_night
            )
            "04d" -> binding.root.setBackgroundResource(
                R.drawable.broken_clouds_day
            )
            "04n" -> binding.root.setBackgroundResource(
                R.drawable.broken_clouds_night
            )
            "09d" -> binding.root.setBackgroundResource(
                R.drawable.shower_rain_day
            )
            "09n" -> binding.root.setBackgroundResource(
                R.drawable.shower_rain_night
            )
            "10d" -> binding.root.setBackgroundResource(
                R.drawable.rain_day
            )
            "10n" -> binding.root.setBackgroundResource(
                R.drawable.rain_night
            )
            "11d" -> binding.root.setBackgroundResource(
                R.drawable.thunderstorm_day
            )
            "11n" -> binding.root.setBackgroundResource(
                R.drawable.thunderstorm_night
            )
            "13d" -> binding.root.setBackgroundResource(
                R.drawable.snow_day
            )
            "13n" -> binding.root.setBackgroundResource(
                R.drawable.snow_night
            )
            "50d" -> binding.root.setBackgroundResource(
                R.drawable.mist_day
            )
            "50n" -> binding.root.setBackgroundResource(
                R.drawable.mist_night
            )
            else -> binding.root.setBackgroundColor(Color.GRAY)
        }
    }

    private fun setupHourlyRecyclerView(hourlyList: MutableList<Hourly>) {
        hourlyAdapter = HourlyAdapter(hourlyList)
        binding.recyclerviewHourly.apply {
            this.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            this.adapter = hourlyAdapter
        }

    }

    private fun setupForecastRecyclerView(sevenDaysList: MutableList<Daily>) {
        sevenDaysAdapter = SevenDaysAdapter(sevenDaysList)
        binding.recyclerviewSevenDays.apply {
            this.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = sevenDaysAdapter
        }

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
            weatherViewModel.getSevenDaysCurrent(it.latitude.toString(), it.longitude.toString())
            weatherViewModel.getHourlyWeather(it.latitude.toString(), it.longitude.toString())

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}