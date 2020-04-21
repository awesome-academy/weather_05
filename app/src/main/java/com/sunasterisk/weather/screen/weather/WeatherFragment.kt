package com.sunasterisk.weather.screen.weather

import android.app.ProgressDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.roundToInt
import com.sunasterisk.weather.R
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import com.sunasterisk.weather.data.source.WeatherRepository
import com.sunasterisk.weather.screen.adapter.WeatherAdapter
import com.sunasterisk.weather.screen.cities.CitiesFragment
import com.sunasterisk.weather.utils.*
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.layout_body_weather.*
import kotlinx.android.synthetic.main.layout_footer_weather.*
import kotlinx.android.synthetic.main.layout_header_weather.*

class WeatherFragment private constructor() : Fragment(), WeatherContract.View,
    SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private val presenter: WeatherPresenter by lazy {
        WeatherPresenter(WeatherRepository.instance)
    }

    private val hourlyAdapter: WeatherAdapter by lazy {
        WeatherAdapter(R.layout.layout_item_hourly, WeatherEntry.HOURLY_OBJECT)
    }

    private val dailyAdapter: WeatherAdapter by lazy {
        WeatherAdapter(R.layout.layout_item_daily, WeatherEntry.DAILY_OBJECT)
    }
    private val progressBarLoading: ProgressDialog by lazy {
        ProgressDialog(context)
    }

    private val sharedPreferences by lazy {
        context?.getSharedPreferences(Constant.PREF_SPEED_AND_TEMPERATURE_UNIT, MODE_PRIVATE)
    }

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var speedUnit: String? = null
    private var speedValue: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        (activity as? AppCompatActivity)?.setupToolbar(toolbarWeather, getString(R.string.app_name))
        // Hourly RecyclerView
        recyclerViewHourly.setHasFixedSize(true)
        recyclerViewHourly.adapter = hourlyAdapter
        // Daily RecyclerView
        recyclerViewDaily.setHasFixedSize(true)
        recyclerViewDaily.adapter = dailyAdapter
        textWindSpeedUnit.setOnClickListener(this)
        swipeRefreshWeather.setOnRefreshListener(this)
    }

    private fun initData() {
        presenter.setView(this)
        arguments?.let {
            latitude = it.getDouble(Constant.LATITUDE_KEY)
            longitude = it.getDouble(Constant.LONGITUDE_KEY)
            presenter.getWeather(latitude, longitude)
        }
        speedUnit = sharedPreferences?.getString(Constant.SPEED_UNIT_KEY, SpeedUnit.MS)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_weather_screen, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_cities) {
            activity?.let {
                (it as AppCompatActivity).addFragmentToActivity(
                    it.supportFragmentManager,
                    CitiesFragment.newInstance(),
                    R.id.container)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onProgressLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBarLoading.show()
            progressBarLoading.setContentView(R.layout.layout_loading_weather)
        } else {
            progressBarLoading.dismiss()
        }
    }

    override fun onGetCurrentWeatherSuccess(weather: Weather) {
        progressBarHumidity.max = 100
        textLocation.text = WeatherUtils.formatNameLocation(context, latitude, longitude)
        weather.apply {
            weatherHourlyList?.let {
                hourlyAdapter.updateData(it as MutableList<WeatherStatistics>, TemperatureUnit.CELSIUS)
            }
            weatherDailyList?.let {
                dailyAdapter.updateData(it as MutableList<WeatherStatistics>, TemperatureUnit.CELSIUS)
            }
            weatherCurrent?.let {
                it.time?.let { time ->
                    textTimeUpdate.text =
                        WeatherUtils.formatTime(context, time, Constant.TAG_LAST_UPDATE)
                }
                textSummary.text = it.summary
                textTemperature.text = WeatherUtils.formatTemperature(
                    getTemperature(it.temperature, TemperatureUnit.CELSIUS))
                textMinMaxTemperature.text = WeatherUtils.formatTemperature(
                    getTemperature(it.temperatureMin, TemperatureUnit.CELSIUS),
                    getTemperature(it.temperatureMax, TemperatureUnit.CELSIUS))
                it.wind?.let { wind ->
                    textWindDirectionValue.text = wind.windDirection?.let { windDirection ->
                        WeatherUtils.formatWindDirection(windDirection)
                    }
                    textWindSpeedValue.text = wind.windSpeed?.let { windSpeed ->
                        speedValue = windSpeed
                        speedUnit?.let { unit ->
                            speedUnit = WeatherUtils.changeSpeedUnit(unit)
                            WeatherUtils.formatWindSpeed(windSpeed, unit)
                        }
                    }
                }
                textWindSpeedUnit.text = StringBuilder(" - ").append(speedUnit)
                it.humidity?.let { humidity ->
                    progressBarHumidity.progress = humidity.times(100).roundToInt()
                    textPercentHumidity.text = WeatherUtils.formatHumidity(humidity)
                }
            }
        }
    }

    private fun getTemperature(temperature: Double?, unit: String): Int {
        temperature?.let {
            return WeatherUtils.temperatureUnit(it, unit)
        }
        return 0
    }

    override fun onError(exception: Exception) {
        Toast.makeText(context, exception.message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        presenter.getWeather(latitude, longitude)
        swipeRefreshWeather.isRefreshing = false
    }

    override fun onClick(v: View?) {
        speedUnit?.let {
            speedUnit = WeatherUtils.changeSpeedUnit(it)
            textWindSpeedValue.text =
                WeatherUtils.formatWindSpeed(speedValue, WeatherUtils.changeSpeedUnit(it))
            textWindSpeedUnit.text = StringBuilder(" - ").append(it)
        }
        sharedPreferences?.edit()?.putString(Constant.SPEED_UNIT_KEY, speedUnit)?.apply()
    }

    companion object {
        fun newInstance(latitude: Double, longitude: Double) =
            WeatherFragment().apply {
                arguments = bundleOf(
                    Constant.LATITUDE_KEY to latitude,
                    Constant.LONGITUDE_KEY to longitude
                )
            }
    }
}
