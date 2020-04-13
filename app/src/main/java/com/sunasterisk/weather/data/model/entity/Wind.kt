package com.sunasterisk.weather.data.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wind(
    val windDirection: Int? = 0,
    val windSpeed: Double? = 0.0,
    val unit: String? = ""
) : Parcelable

object WindEntry {
    const val WIND_DIRECTION = "windBearing"
    const val WIND_SPEED = "windSpeed"
}
