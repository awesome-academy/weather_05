package com.sunasterisk.weather.screen.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sunasterisk.weather.R
import kotlinx.android.synthetic.main.fragment_cities.*
import com.sunasterisk.weather.utils.setupToolbar

class CitiesFragment private constructor(): Fragment(), View.OnClickListener{

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
        (activity as? AppCompatActivity)?.setupToolbar(
            toolbarCities, getString(R.string.title_cities_manager))
        toolbarCities.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbarCities.setNavigationOnClickListener(this)
    }

    private fun initData() {}

    override fun onClick(v: View?) {
        activity?.supportFragmentManager?.popBackStack()
    }

    companion object {
        fun newInstance() = CitiesFragment()
    }
}
