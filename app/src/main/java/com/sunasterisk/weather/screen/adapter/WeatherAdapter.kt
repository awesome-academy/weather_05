package com.sunasterisk.weather.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import com.sunasterisk.weather.utils.OnItemRecyclerViewClickListener
import com.sunasterisk.weather.utils.WeatherUtils
import kotlinx.android.synthetic.main.layout_item_daily.view.*
import kotlinx.android.synthetic.main.layout_item_hourly.view.*

class WeatherAdapter(
    private val layoutResource: Int,
    private val tagObject: String
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private val listWeathers by lazy { mutableListOf<WeatherStatistics>() }
    private var temperatureUnit: String? = null

    fun updateData(listData: MutableList<WeatherStatistics>, unit: String) {
        temperatureUnit = unit
        listWeathers.clear()
        listWeathers.addAll(listData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutResource, parent, false))
    }

    override fun getItemCount() = listWeathers.size

    override fun onBindViewHolder(holder: WeatherAdapter.ViewHolder, position: Int) {
        when(tagObject) {
            WeatherEntry.HOURLY_OBJECT -> holder.bindDataHourly(listWeathers[position])
            else -> holder.bindDataDaily(listWeathers[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindDataHourly(data: WeatherStatistics) {
            data.time?.let { time ->
                itemView.textHourlyTime.text =
                    WeatherUtils.formatTime(time, WeatherEntry.HOURLY_OBJECT)
            }
            temperatureUnit?.let {
                data.temperature?.let { temperature ->
                    itemView.textHourlyTemperature.text =
                        WeatherUtils.formatTemperature(
                            WeatherUtils.temperatureUnit(temperature, it))
                }
            }
        }

        fun bindDataDaily(data: WeatherStatistics) {
            data.time?.let {
                itemView.textDailyTime.text =
                    WeatherUtils.formatTime(it, WeatherEntry.DAILY_OBJECT)
            }
            temperatureUnit?.let {
                if (data.temperatureMin != null && data.temperatureMax != null) {
                    itemView.textDailyMinMaxTemperature.text =  WeatherUtils.formatTemperature(
                        WeatherUtils.temperatureUnit(data.temperatureMin, it),
                        WeatherUtils.temperatureUnit(data.temperatureMax, it))
                }
            }
        }
    }
}
