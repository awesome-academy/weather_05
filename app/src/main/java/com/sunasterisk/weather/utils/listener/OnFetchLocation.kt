package com.sunasterisk.weather.utils.listener

import android.location.Location

interface OnFetchLocation {
    fun onDataLocation(location: Location?)
}
