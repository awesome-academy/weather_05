package com.sunasterisk.weather.screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunasterisk.weather.R
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.utils.TemperatureUnit
import com.sunasterisk.weather.utils.WeatherUtils
import com.sunasterisk.weather.utils.listener.OnItemRecyclerViewClickListener
import kotlinx.android.synthetic.main.layout_item_city.view.*

class CitiesAdapter : RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    private val weatherList = mutableListOf<Weather>()
    private var onItemClickListener: OnItemRecyclerViewClickListener<Weather>? = null
    private var temperatureUnit: String? = null

    fun updateData(list: List<Weather>, unit: String) {
        temperatureUnit = unit
        weatherList.clear()
        weatherList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemRecyclerViewClickListener<Weather>?) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_item_city,
                    parent,
                    false
            ),
            onItemClickListener
        )
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: CitiesAdapter.ViewHolder, position: Int) {
        holder.bindData(weatherList[position])
    }

    inner class ViewHolder(
        itemView: View,
        private val listener: OnItemRecyclerViewClickListener<Weather>?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var weather: Weather? = null
        var onItemClick: OnItemRecyclerViewClickListener<Weather>? = null

        fun bindData(data: Weather) {
            weather = data
            val unit = temperatureUnit?: TemperatureUnit.CELSIUS
            itemView.apply {
                WeatherUtils.apply {
                        weather?.apply {
                       val latitude = weather?.latitude?: 0.0
                       val longitude = weather?.longitude?: 0.0
                        textLocationName.text = formatNameLocation(context, latitude, longitude)
                        weatherCurrent?.apply {
                            temperature?.let {
                                textTemperatureLocation.text =
                                    formatTemperature(temperatureUnit(it, unit))
                            }
                            textSummaryLocation.text = summary
                        }
                    }
                }
                itemView.setOnClickListener(this@ViewHolder)
            }
            onItemClick = listener
        }

        override fun onClick(v: View?) {
            onItemClick?.onClickItemRecyclerView(weather)
        }
    }
}
