package com.sunasterisk.weather.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.sunasterisk.weather.R
import com.sunasterisk.weather.screen.weather.WeatherFragment

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                       .add(R.id.container, WeatherFragment.newInstance())
                       .commit()
    }

    private fun initData() {}
}
