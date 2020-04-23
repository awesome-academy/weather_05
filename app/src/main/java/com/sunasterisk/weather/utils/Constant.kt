package com.sunasterisk.weather.utils

import com.sunasterisk.weather.BuildConfig

object Constant {
    const val BASE_URL = "https://api.darksky.net/forecast/"
    const val BASE_API_KEY = BuildConfig.WEATHER_API_KEY + "/"
    const val COMMA = ","
    const val LATITUDE_KEY = "LATITUDE"
    const val LONGITUDE_KEY = "LONGITUDE"
    const val SPEED_UNIT_KEY = "SPEED_UNIT"
    const val TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY"
    const val TAG_LAST_UPDATE = "UPDATE"
    const val PREF_SPEED_AND_TEMPERATURE_UNIT = "SPEED_AND_TEMPERATURE_UNIT"
    const val FORMAT_PERCENT = "%.0f%%"
    const val FORMAT_TIME_HOURLY = "HH:00"
    const val FORMAT_TIME_DAILY = "EEE, MMM dd, yyyy"
    const val FORMAT_TIME_LAST_UPDATED = "HH:mm"
}
