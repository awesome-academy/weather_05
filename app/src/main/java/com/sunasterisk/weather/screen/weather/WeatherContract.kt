package com.sunasterisk.weather.screen.weather

import java.lang.Exception
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.utils.BasePresenter

interface WeatherContract {

    interface View {
        fun onProgressLoading(isLoading: Boolean)
        fun onGetCurrentWeatherSuccess(weather: Weather)
        fun onInternetConnectionFailed()
        fun onError(exception: Exception)
    }

    interface Presenter : BasePresenter<View> {
        fun getWeather(latitude: Double, longitude: Double)
        fun getWeatherLocal()
    }
}
