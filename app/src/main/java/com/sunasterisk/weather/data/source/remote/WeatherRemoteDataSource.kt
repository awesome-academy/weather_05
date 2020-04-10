package com.sunasterisk.weather.data.source.remote

import com.sunasterisk.weather.data.source.WeatherDataSource

class WeatherRemoteDataSource private constructor(): WeatherDataSource.Remote {

    private object HOLDER {
        val INSTANCE = WeatherRemoteDataSource()
    }

    companion object {
        val instance: WeatherRemoteDataSource by lazy { HOLDER.INSTANCE }
    }
}
