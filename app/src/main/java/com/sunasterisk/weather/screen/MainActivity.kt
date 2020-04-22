package com.sunasterisk.weather.screen

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunasterisk.weather.R
import com.sunasterisk.weather.screen.weather.WeatherFragment
import com.sunasterisk.weather.utils.*
import com.sunasterisk.weather.utils.listener.OnFetchLocation


class MainActivity : AppCompatActivity(), OnFetchLocation{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PermissionUtils.requestPermissions(this)
    }

    override fun onResume() {
        super.onResume()
        val fragment =
            supportFragmentManager.findFragmentByTag(WeatherFragment::class.java.simpleName)
        if (fragment == null) {
            PermissionUtils.getLastLocation(
                this,
                this,
                PermissionUtils.isLocationEnabled(this)
            )
        }
    }

    private fun initView(location: Location?) {
        location?.let {
            addFragmentToActivity(
                supportFragmentManager,
                WeatherFragment.newInstance(it.latitude, it.longitude),
                R.id.container
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        PermissionUtils.onRequestPermissionResult(
            requestCode,
            permissions, grantResults, this
        )
    }

    override fun onRestart() {
        super.onRestart()
        PermissionUtils.requestPermissions(this)
    }

    override fun onDataLocation(location: Location?) {
        initView(location)
    }
}
