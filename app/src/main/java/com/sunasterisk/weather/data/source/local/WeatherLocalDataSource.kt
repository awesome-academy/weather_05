package com.sunasterisk.weather.data.source.local

import com.sunasterisk.weather.data.source.WeatherDataSource

class WeatherLocalDataSource private constructor(): WeatherDataSource.Local {

    private object HOLDER {
        val INSTANCE = WeatherLocalDataSource()
    }

    companion object {
        val instance: WeatherLocalDataSource by lazy { HOLDER.INSTANCE }
    }
}
