package com.ewerdus.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ewerdus.weather.R
import com.ewerdus.weather.data.onecall.Hourly
import kotlinx.android.synthetic.main.itemview_weather_forecast.view.*
import java.text.SimpleDateFormat

class HourlyAdapter(private val hourlyList: MutableList<Hourly>):RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    class HourlyViewHolder(itemview: View) :RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return HourlyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.itemview_weather_forecast,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val formatter = SimpleDateFormat("k:mm")
        val imgUrl =  "https://openweathermap.org/img/wn/" + hourlyList[position].weather[0].icon + "@2x.png"
        holder.itemView.apply {
            tv_temp.text = hourlyList[position].temp.toString()
            tv_time.text = formatter.format(hourlyList[position].dt * 1000L)
            Glide.with(this).load(imgUrl).into(img_view)
        }
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}