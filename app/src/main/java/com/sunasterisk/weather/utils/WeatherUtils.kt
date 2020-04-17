package com.sunasterisk.weather.utils

import com.sunasterisk.weather.utils.SpeedUnit.KMH
import kotlin.math.roundToInt
import com.sunasterisk.weather.data.model.WeatherEntry
import java.text.SimpleDateFormat

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
        return when(speedUnit) {
            // 1000 = 1km
            // 3600 = 1h
            KMH -> {
                "${(data / 1000 * 3600).roundToInt()} " + speedUnit
            }
            else -> "${data.roundToInt()} $speedUnit"
        }
    }

    fun formatHumidity(data: Double): String {
        return String.format(Constant.FORMAT_PERCENT, data.let { (it * 100) })
    }

    fun formatTime(data: Long, tagObject: String): String {
        return when(tagObject) {
            WeatherEntry.HOURLY_OBJECT -> {
                SimpleDateFormat(Constant.FORMAT_TIME_HOURLY).format(data)
            }
            else -> SimpleDateFormat(Constant.FORMAT_TIME_DAILY).format(data)
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
