package com.sunasterisk.weather.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.sunasterisk.weather.R
import com.sunasterisk.weather.utils.listener.MyLocationListener
import com.sunasterisk.weather.utils.listener.OnFetchLocation

object PermissionUtils {
    private const val PERMISSION_LOCATION_ID = 44

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_LOCATION_ID
        )
    }

    fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER)
    }

    fun getLastLocation(activity: Activity, onFetchLocation: OnFetchLocation, boolean: Boolean) {
        if (boolean) {
            val locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_COARSE
            criteria.isCostAllowed = false
            val provider = locationManager.getBestProvider(criteria, false)
            try {
                val listener = MyLocationListener(onFetchLocation)
                var location = locationManager.getLastKnownLocation(provider)
                location?.let { listener.onLocationChanged(location) }
                // location updates: at least 1 meter and 500 milli seconds change
                locationManager.requestLocationUpdates(provider, 200, 1f, listener)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity
    ) {
        if (requestCode == PERMISSION_LOCATION_ID) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (!isLocationEnabled(activity)) { turnOnLocation(activity) }
            }
        }
    }

    fun checkPermissions(activity: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) { return true }
        return false
    }

    private fun turnOnLocation(activity: Activity) {
        Toast.makeText(activity,
            activity.getString(R.string.message_turn_on_location),
            Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }
}
