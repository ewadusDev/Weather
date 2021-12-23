package com.ewerdus.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ewerdus.weather.R
import com.ewerdus.weather.data.onecall.Daily
import kotlinx.android.synthetic.main.itemview_weather_forecast.view.*
import java.text.SimpleDateFormat

class SevenDaysAdapter(private val sevenDaysList: MutableList<Daily>) :
    RecyclerView.Adapter<SevenDaysAdapter.SevenDaysViewHolder>() {

    class SevenDaysViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SevenDaysViewHolder {
        return SevenDaysViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_weather_forecast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SevenDaysViewHolder, position: Int) {
        val imgListUrl =
            "https://openweathermap.org/img/wn/" + sevenDaysList[position].weather[0].icon + "@2x.png"
        val tempFormatted = String.format("%.0f", sevenDaysList[position].temp.day)
        val dateFormatter = SimpleDateFormat("EEE")
        holder.itemView.apply {
            tv_time.text =  dateFormatter.format(sevenDaysList[position].dt * 1000L)
            tv_temp.text = tempFormatted + "°"
            Glide.with(this).load(imgListUrl).into(img_view)
            Log.i("dateformate",dateFormatter.format(sevenDaysList[position].dt* 1000L).toString())


        }
    }

    override fun getItemCount(): Int {
        return sevenDaysList.size
    }
}