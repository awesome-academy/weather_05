package com.sunasterisk.weather.screen.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sunasterisk.weather.R

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        initData()
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    private fun initView() {
        // TODO UPDATE RECYCLER VIEW DAILY, HOURLY
    }

    private fun initData() {}

    companion object {
        fun newInstance() = WeatherFragment()
    }
}
