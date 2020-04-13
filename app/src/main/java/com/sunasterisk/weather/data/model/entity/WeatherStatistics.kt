package com.sunasterisk.weather.data.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherStatistics(
    val time: Long? = 0,
    val summary: String? = "",
    val icon: String? = "",
    val temperature: Double? = 0.0,
    val temperatureMin: Double? = 0.0,
    val temperatureMax: Double? = 0.0,
    val humidity: Double? = 0.0,
    val wind: Wind? = null
) : Parcelable

object WeatherStatisticsEntry {
    const val WEATHER = "data"
    const val TIME = "time"
    const val ICON = "icon"
    const val SUMMARY = "summary"
    const val HUMIDITY = "humidity"
    const val TEMPERATURE = "apparentTemperature"
    const val TEMPERATURE_MIN = "apparentTemperatureMin"
    const val TEMPERATURE_MAX = "apparentTemperatureMax"
}
