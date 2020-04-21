package com.sunasterisk.weather.data.anotation

import androidx.annotation.StringDef
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

@Retention(AnnotationRetention.SOURCE)
@StringDef(
   CLEAR_DAY,
   CLEAR_NIGHT,
   RAIN,
   SNOW,
   SLEET,
   WIND,
   FOG,
   CLOUDY,
   PARTLY_CLOUDY_DAY,
   PARTLY_CLOUDY_NIGHT,
   HAIL,
   THUNDERSTORM,
   TORNADO
)
annotation class Icon {
    companion object {
        const val CLEAR_DAY = "clear-day"
        const val CLEAR_NIGHT = "clear-night"
        const val RAIN = "rain"
        const val SNOW = "snow"
        const val SLEET = "sleet"
        const val WIND = "wind"
        const val FOG = "fog"
        const val CLOUDY = "cloudy"
        const val PARTLY_CLOUDY_DAY = "partly-cloudy-day"
        const val PARTLY_CLOUDY_NIGHT = "partly-cloudy-night"
        const val HAIL = "hail"
        const val THUNDERSTORM = "thunderstorm"
        const val TORNADO = "tornado"
    }
}
