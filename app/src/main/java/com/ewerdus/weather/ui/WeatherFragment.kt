package com.ewerdus.weather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ewerdus.weather.R
import com.ewerdus.weather.databinding.FragmentWeatherBinding


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeatherBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}