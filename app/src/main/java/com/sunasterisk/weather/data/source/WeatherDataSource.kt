package com.sunasterisk.weather.data.source

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.remote.OnFetchDataJsonListener

interface WeatherDataSource {

    interface Local {
        fun insertWeather(weather: Weather)
        fun getWeathers(): List<Weather>?
        fun deleteWeather(id: String)
    }

    interface Remote {
        fun getWeather(
            latitude: Double,
            longitude: Double,
            listener: OnFetchDataJsonListener<Weather>
        )
    }
}
