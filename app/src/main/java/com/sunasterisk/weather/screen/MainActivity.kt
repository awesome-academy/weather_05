package com.sunasterisk.weather.screen

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sunasterisk.weather.R
import com.sunasterisk.weather.screen.cities.CitiesFragment
import com.sunasterisk.weather.screen.weather.WeatherFragment
import com.sunasterisk.weather.utils.*
import com.sunasterisk.weather.utils.listener.OnFetchLocation

class MainActivity : AppCompatActivity(), OnFetchLocation {
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionUtils.requestPermissions(this)
    }

    override fun onResume() {
        super.onResume()
        PermissionUtils.getLastLocation(
            this,
            this,
            PermissionUtils.isLocationEnabled(this))
        initView()
    }

    private fun initView() {
        location?.let {
            ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                WeatherFragment.newInstance(it.latitude, it.longitude),
                R.id.container)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionUtils.onRequestPermissionResult(requestCode,
            permissions, grantResults, this)
    }

    override fun onRestart() {
        super.onRestart()
        PermissionUtils.requestPermissions(this)
    }

    override fun onDataLocation(location: Location?) {
        this.location = location
    }
}
