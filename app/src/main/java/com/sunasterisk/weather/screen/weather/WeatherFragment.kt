package com.sunasterisk.weather.screen.weather

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import java.lang.Exception
import kotlin.math.roundToInt
import com.sunasterisk.weather.R
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import com.sunasterisk.weather.data.source.WeatherRepository
import com.sunasterisk.weather.screen.adapter.WeatherAdapter
import com.sunasterisk.weather.utils.*
import kotlinx.android.synthetic.main.layout_body_weather.*
import kotlinx.android.synthetic.main.layout_footer_weather.*
import kotlinx.android.synthetic.main.layout_header_weather.*

class WeatherFragment : Fragment(), WeatherContract.View {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        // Hourly RecyclerView
        recyclerViewHourly.setHasFixedSize(true)
        recyclerViewHourly.adapter = hourlyAdapter
        // Daily RecyclerView
        recyclerViewDaily.setHasFixedSize(true)
        recyclerViewDaily.adapter = dailyAdapter
    }

    private fun initData() {
        presenter.setView(this)
        arguments?.let {
           presenter.getWeather(
               it.getDouble(Constant.LATITUDE_KEY),
               it.getDouble(Constant.LONGITUDE_KEY)
           )
       }
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
        textLocation.text = weather.timeZone
        weather.weatherHourlyList?.let {
            hourlyAdapter.updateData(it as MutableList<WeatherStatistics>, TemperatureUnit.CELSIUS)
        }
        weather.weatherDailyList?.let {
            dailyAdapter.updateData(it as MutableList<WeatherStatistics>, TemperatureUnit.CELSIUS)
        }
        weather.weatherCurrent?.let {
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
                    WeatherUtils.formatWindSpeed(windSpeed, SpeedUnit.MS)
                }
            }
            it.humidity?.let { humidity ->
                progressBarHumidity.progress = humidity.times(100).roundToInt()
                textPercentHumidity.text = WeatherUtils.formatHumidity(humidity)
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
