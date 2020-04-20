package com.sunasterisk.weather.screen.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.sunasterisk.weather.R
import com.sunasterisk.weather.screen.weather.WeatherFragment
import com.sunasterisk.weather.utils.Constant
import com.sunasterisk.weather.utils.replaceFragmentToActivity
import com.sunasterisk.weather.utils.setupToolbar
import kotlinx.android.synthetic.main.fragment_cities.*

class CitiesFragment private constructor(): Fragment(), View.OnClickListener{
    private var latitude = 0.0
    private var longitude = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        (activity as AppCompatActivity?)?.setupToolbar(
            toolbarCities, getString(R.string.title_cities_manager))
        toolbarCities.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back_white_24dp)
        toolbarCities.setNavigationOnClickListener(this)
    }

    private fun initData() {
        arguments?.let {
            latitude = it.getDouble(Constant.LATITUDE_KEY)
            longitude = it.getDouble(Constant.LONGITUDE_KEY)
        }
    }

    override fun onClick(v: View?) {
        activity?.supportFragmentManager?.popBackStack()
    }

    companion object {
        fun newInstance(latitude: Double, longitude: Double) =
            CitiesFragment().apply {
                arguments = bundleOf(
                    Constant.LATITUDE_KEY to latitude,
                    Constant.LONGITUDE_KEY to longitude
                )
            }
    }
}
