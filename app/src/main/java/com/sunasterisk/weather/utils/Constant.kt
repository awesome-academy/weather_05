package com.sunasterisk.weather.utils

import com.sunasterisk.weather.BuildConfig

object Constant {
    const val BASE_URL = "https://api.darksky.net/forecast/"
    const val BASE_API_KEY = BuildConfig.WEATHER_API_KEY + "/"
    const val COMMA = ","
}
