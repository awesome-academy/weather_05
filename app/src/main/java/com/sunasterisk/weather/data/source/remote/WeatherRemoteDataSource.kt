package com.sunasterisk.weather.data.source.remote

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.WeatherDataSource
import com.sunasterisk.weather.data.source.remote.fetchjson.GetJsonFromUrl
import com.sunasterisk.weather.utils.Constant

class WeatherRemoteDataSource private constructor(): WeatherDataSource.Remote {

    private object HOLDER {
        val INSTANCE = WeatherRemoteDataSource()
    }

    companion object {
        val instance: WeatherRemoteDataSource by lazy { HOLDER.INSTANCE }
    }

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        listener: OnFetchDataJsonListener<Weather>
    ) {
        val url = (Constant.BASE_URL
                        + Constant.BASE_API_KEY
                        + latitude
                        + Constant.COMMA
                        + longitude)
        GetJsonFromUrl(listener).execute(url)
    }
}
