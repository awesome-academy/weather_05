package com.sunasterisk.weather.data.source

import com.sunasterisk.weather.data.source.local.WeatherLocalDataSource
import com.sunasterisk.weather.data.source.remote.WeatherRemoteDataSource

class WeatherRepository private constructor(
    localDataSource: WeatherLocalDataSource,
    remoteDataSource: WeatherRemoteDataSource
) : WeatherDataSource.Local, WeatherDataSource.Remote {

    private object HOLDER {
        val INSTANCE = WeatherRepository(localDataSource = WeatherLocalDataSource.instance,
                                         remoteDataSource = WeatherRemoteDataSource.instance)
    }

    companion object {
        val instance: WeatherRepository by lazy { HOLDER.INSTANCE }
    }
}
