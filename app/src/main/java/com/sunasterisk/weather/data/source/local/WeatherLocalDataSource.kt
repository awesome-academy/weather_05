package com.sunasterisk.weather.data.source.local

import android.content.Context
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.WeatherDataSource
import com.sunasterisk.weather.data.source.local.database.DBHelper

class WeatherLocalDataSource private constructor(
    private val context: Context?
) : WeatherDataSource.Local {

    private val dbHelper: DBHelper by lazy { DBHelper.getInstance(context) }

    override fun insertWeather(weather: Weather) {
        dbHelper.insertWeather(weather)
    }

    override fun getWeathers(): List<Weather>? {
        return dbHelper.getAllData()
    }

    override fun deleteWeather(id: String) {
        dbHelper.deleteWeather(id)
    }

    companion object {
        private var INSTANCE: WeatherLocalDataSource? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: WeatherLocalDataSource(context).also { INSTANCE = it }
        }
    }
}
