package com.sunasterisk.weather.screen.cities

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.local.database.DBHelper
import com.sunasterisk.weather.utils.BasePresenter

interface CitiesContract {
    interface View {
        fun onWeatherSuccess(weatherList: List<Weather>?)
    }

    interface Presenter : BasePresenter<View> {
        fun getWeathersLocal()
        fun deleteWeather(id: String)
    }
}
