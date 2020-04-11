package com.sunasterisk.weather.data.model

import android.os.Parcelable
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val timeZone: String? = "",
    val weatherCurrent: WeatherStatistics?,
    val weatherHourlyList: List<WeatherStatistics>?,
    val weatherDailyList: List<WeatherStatistics>?
) : Parcelable

object WeatherEntry {
    const val CURRENTLY_OBJECT = "currently"
    const val HOURLY_OBJECT = "hourly"
    const val DAILY_OBJECT = "daily"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val TIMEZONE = "timezone"
}
