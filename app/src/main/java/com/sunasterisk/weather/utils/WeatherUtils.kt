package com.sunasterisk.weather.utils

import android.content.Context
import android.location.Geocoder
import com.sunasterisk.weather.R
import com.sunasterisk.weather.data.anotation.Icon
import com.sunasterisk.weather.data.anotation.Icon.Companion.CLEAR_DAY
import com.sunasterisk.weather.data.anotation.Icon.Companion.CLEAR_NIGHT
import com.sunasterisk.weather.data.anotation.Icon.Companion.CLOUDY
import com.sunasterisk.weather.data.anotation.Icon.Companion.FOG
import com.sunasterisk.weather.data.anotation.Icon.Companion.HAIL
import com.sunasterisk.weather.data.anotation.Icon.Companion.PARTLY_CLOUDY_DAY
import com.sunasterisk.weather.data.anotation.Icon.Companion.PARTLY_CLOUDY_NIGHT
import com.sunasterisk.weather.data.anotation.Icon.Companion.RAIN
import com.sunasterisk.weather.data.anotation.Icon.Companion.SLEET
import com.sunasterisk.weather.data.anotation.Icon.Companion.SNOW
import com.sunasterisk.weather.data.anotation.Icon.Companion.THUNDERSTORM
import com.sunasterisk.weather.data.anotation.Icon.Companion.TORNADO
import com.sunasterisk.weather.data.anotation.Icon.Companion.WIND
import com.sunasterisk.weather.utils.SpeedUnit.KMH
import kotlin.math.roundToInt
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.utils.SpeedUnit.MS
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object WeatherUtils {

    fun formatTemperature(vararg temperatures: Int): String {
        return when (temperatures.size) {
            1 -> "${temperatures[0]}°"
            else -> "${temperatures[0]}°/${temperatures[1]}°"
        }
    }

    fun temperatureUnit(data: Double, temperatureUnit: String): Int {
        return when(temperatureUnit) {
            // CELSIUS = (FAHRENHEIT - 32) / 1.8)
            TemperatureUnit.CELSIUS -> {
                val roundToInt = ((data - 32) / 1.8).roundToInt()
                roundToInt
            }
            else -> data.roundToInt()
        }
    }

    fun formatWindDirection(data: Int): String {
        return when(data) {
            // 23 -> 67 Wind direction is NORTHEAST
            in 23..67 -> Direction.NORTHEAST
            // 68 -> 112 Wind direction is EAST
            in 68..112 -> Direction.EAST
            // 113 -> 157 Wind direction is SOUTHEAST
            in 113..157 -> Direction.SOUTHEAST
            // 158 -> 202 Wind direction is SOUTH
            in 158..202 -> Direction.SOUTH
            // 203 -> 247 Wind direction is SOUTHWEST
            in 203..247 -> Direction.SOUTHWEST
            // 248 -> 292 Wind direction is WEST
            in 248..292 -> Direction.WEST
            // 293 -> 337 Wind direction is NORTHWEST
            in 293..337 -> Direction.NORTHWEST
            // 338 -> 349, -> 0 -> 22 Wind direction is NORTH
            else -> Direction.NORTH
        }
    }

    fun formatWindSpeed(data: Double, speedUnit: String): String {
        return when (speedUnit) {
            // 1000 = 1km
            // 3600 = 1h
            KMH -> { "${(data / 1000 * 3600).roundToInt()} " + speedUnit }
            else -> "${data.roundToInt()} $speedUnit"
        }
    }

    fun formatHumidity(data: Double) =
        String.format(Constant.FORMAT_PERCENT, data.let { (it * 100) })

    fun formatTime(context: Context?, data: Long, tagObject: String): String {
        return when(tagObject) {
            WeatherEntry.HOURLY_OBJECT -> {
                SimpleDateFormat(Constant.FORMAT_TIME_HOURLY).format(data * 1000)
            }
            WeatherEntry.DAILY_OBJECT -> {
                SimpleDateFormat(Constant.FORMAT_TIME_DAILY).format(data * 1000)
            }
            else -> "${context?.getString(R.string.message_update_data)} " +
                    "${SimpleDateFormat(Constant.FORMAT_TIME_LAST_UPDATED).format(data)}"
        }
    }

    fun getIcon(@Icon icon: String) = getIcons()[icon]

    private fun getIcons() = hashMapOf(
        CLEAR_DAY to  R.drawable.ic_clear_day_50dp,
        CLEAR_NIGHT to R.drawable.ic_clear_night_50dp,
        RAIN to R.drawable.ic_rain_50dp,
        SNOW to R.drawable.ic_snow_50dp,
        SLEET to R.drawable.ic_sleet_50dp,
        WIND to R.drawable.ic_wind_50dp,
        FOG to R.drawable.ic_fog_50dp,
        CLOUDY to R.drawable.ic_cloudy_50dp,
        PARTLY_CLOUDY_DAY to R.drawable.ic_partly_cloud_day_50dp,
        PARTLY_CLOUDY_NIGHT to R.drawable.ic_partly_cloud_night_50dp,
        HAIL to R.drawable.ic_hail_50dp,
        THUNDERSTORM to R.drawable.ic_thunder_storm_50dp,
        TORNADO to R.drawable.ic_tornado_50dp
    )

    fun changeSpeedUnit(speedUnit: String) =
        when(speedUnit) {
            KMH -> MS
            else -> KMH
        }

    fun formatNameLocation(context: Context?, latitude: Double, longitude: Double): String {
        Geocoder(context, Locale.ENGLISH)
            .getFromLocation(latitude, longitude, 1)[0].apply {
            return StringBuilder(adminArea).toString()
        }
    }
}

object Direction {
    const val NORTH = "North"
    const val NORTHEAST = "Northeast"
    const val EAST = "East"
    const val SOUTHEAST = "Southeast"
    const val SOUTH = "South"
    const val SOUTHWEST = "Southwest"
    const val WEST = "West"
    const val NORTHWEST = "Northwest"
}

object SpeedUnit {
    const val KMH = "km/h"
    const val MS = "m/s"
}

object TemperatureUnit {
    const val CELSIUS = "\u2103"
    const val FAHRENHEIT = "\u2109"
}

