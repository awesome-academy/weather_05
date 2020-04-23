package com.sunasterisk.weather.data.source

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.local.WeatherLocalDataSource
import com.sunasterisk.weather.data.source.remote.OnFetchDataJsonListener
import com.sunasterisk.weather.data.source.remote.WeatherRemoteDataSource

class WeatherRepository private constructor(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherDataSource.Local, WeatherDataSource.Remote {

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        listener: OnFetchDataJsonListener<Weather>
    ) {
        remoteDataSource.getWeather(latitude, longitude, listener)
    }

    override fun insertWeather(weather: Weather) {
        localDataSource.insertWeather(weather)
    }

    override fun getWeathers(): List<Weather>? {
        return localDataSource.getWeathers()
    }

    override fun deleteWeather(id: String) {
        localDataSource.deleteWeather(id)
    }

    companion object {
        private var INSTANCE: WeatherRepository? = null

        fun getInstance(local: WeatherLocalDataSource,
                        remote: WeatherRemoteDataSource) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: WeatherRepository(local, remote).also { INSTANCE = it }
        }
    }
}
