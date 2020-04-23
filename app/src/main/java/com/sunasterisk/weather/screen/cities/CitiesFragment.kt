package com.sunasterisk.weather.screen.cities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sunasterisk.weather.R
import kotlinx.android.synthetic.main.fragment_cities.*
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.WeatherRepository
import com.sunasterisk.weather.data.source.local.WeatherLocalDataSource
import com.sunasterisk.weather.data.source.remote.WeatherRemoteDataSource
import com.sunasterisk.weather.screen.adapter.CitiesAdapter
import com.sunasterisk.weather.screen.weather.WeatherFragment
import com.sunasterisk.weather.utils.Constant
import com.sunasterisk.weather.utils.TemperatureUnit
import com.sunasterisk.weather.utils.listener.OnItemRecyclerViewClickListener
import com.sunasterisk.weather.utils.replaceFragmentToActivity

class CitiesFragment private constructor(): Fragment(), View.OnClickListener,
    CitiesContract.View, OnItemRecyclerViewClickListener<Weather> {

    private val citiesAdapter: CitiesAdapter by lazy {
        CitiesAdapter()
    }

    private val sharedPreferences by lazy {
        context?.getSharedPreferences(
            Constant.PREF_SPEED_AND_TEMPERATURE_UNIT,
            Context.MODE_PRIVATE
        )
    }

    private var repository: WeatherRepository? = null
    private var presenter: CitiesPresenter? = null
    private var temperatureUnit: String? = null
    private var listWeather: List<Weather>? = null

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
        toolbarCities.title = getString(R.string.title_cities_manager)
        toolbarCities.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbarCities.setNavigationOnClickListener(this)
        buttonAddCity.setOnClickListener(this)
        textCelsiusUnit.setOnClickListener(this)
        textFahrenheitUnit.setOnClickListener(this)
        citiesAdapter.setOnItemClickListener(this)
        // RecyclerView Cities
        recyclerViewCities.setHasFixedSize(true)
        recyclerViewCities.adapter = citiesAdapter
    }

    private fun initData() {
        repository = context?.let {
            WeatherRepository.getInstance(
                WeatherLocalDataSource.getInstance(it),
                WeatherRemoteDataSource.instance)
        }
        presenter = repository?.let { CitiesPresenter(it) }
        presenter?.setView(this)
        temperatureUnit =
            sharedPreferences?.getString(Constant.TEMPERATURE_UNIT_KEY, TemperatureUnit.CELSIUS)
        presenter?.getWeathersLocal()
    }

    override fun onWeatherSuccess(weatherList: List<Weather>?) {
        val unit = temperatureUnit?: TemperatureUnit.CELSIUS
        if (unit.equals(TemperatureUnit.CELSIUS)) {
            setColorTextUnit(Color.WHITE, Color.GRAY)
        } else {
            setColorTextUnit(Color.GRAY, Color.WHITE)
        }
        weatherList?.let {
            listWeather = it
            citiesAdapter.updateData(it, unit)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonAddCity -> { }
            R.id.textCelsiusUnit -> {
                setColorTextUnit(Color.WHITE, Color.GRAY)
                updateTemperatureUnit(TemperatureUnit.CELSIUS)
            }
            R.id.textFahrenheitUnit -> {
                setColorTextUnit(Color.GRAY, Color.WHITE)
                updateTemperatureUnit(TemperatureUnit.FAHRENHEIT)
            }
            else -> {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun setColorTextUnit(vararg colors: Int) {
        textCelsiusUnit.setTextColor(colors[0])
        textFahrenheitUnit.setTextColor(colors[1])
    }

    private fun updateTemperatureUnit(unit: String) {
        listWeather?.let {
            citiesAdapter.updateData(it, unit)
        }
        temperatureUnit = unit
        sharedPreferences?.edit()?.putString(
            Constant.TEMPERATURE_UNIT_KEY,
            temperatureUnit)?.apply()
    }

    override fun onClickItemRecyclerView(item: Weather?) {
        (activity as? AppCompatActivity)?.apply {
            item?.apply {
                if (longitude != null && latitude != null) {
                    replaceFragmentToActivity(
                        supportFragmentManager,
                        WeatherFragment.newInstance(latitude, longitude),
                        R.id.container
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences?.edit()?.putString(
            Constant.TEMPERATURE_UNIT_KEY,
            temperatureUnit)?.apply()
    }

    companion object {
        fun newInstance() = CitiesFragment()
    }
}
