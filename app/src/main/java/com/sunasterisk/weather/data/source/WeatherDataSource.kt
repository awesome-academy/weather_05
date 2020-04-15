package com.sunasterisk.weather.data.source

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.remote.OnFetchDataJsonListener

interface WeatherDataSource {

    interface Local

    interface Remote {
        fun getWeather(
            latitude: Double,
            longitude: Double,
            listener: OnFetchDataJsonListener<Weather>
        )
    }
}
