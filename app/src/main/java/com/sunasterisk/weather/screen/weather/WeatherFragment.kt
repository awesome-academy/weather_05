package com.sunasterisk.weather.screen.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.lang.Exception
import kotlin.math.roundToInt
import com.sunasterisk.weather.R
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.source.WeatherRepository
import com.sunasterisk.weather.utils.*
import kotlinx.android.synthetic.main.layout_footer_weather.*
import kotlinx.android.synthetic.main.layout_header_weather.*

class WeatherFragment : Fragment(), WeatherContract.View {

    private val presenter: WeatherPresenter by lazy {
        WeatherPresenter(WeatherRepository.instance)
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
       // TODO RECYCLERVIEW HOURLY, DAILY
    }

    private fun initData() {
        val argument = arguments
        presenter.setView(this)
        argument?.let {
           presenter.getWeather(it.getDouble(WeatherEntry.LATITUDE),
                                it.getDouble(WeatherEntry.LONGITUDE))
       }
    }

    override fun onProgressLoading(isLoading: Boolean) {}

    override fun onGetCurrentWeatherSuccess(weather: Weather) {
        progressBarHumidity.max = 100
        textLocation.text = weather.timeZone
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
                Bundle().apply {
                    putDouble(Constant.LATITUDE_KEY, latitude)
                    putDouble(Constant.LONGITUDE_KEY, longitude)
                    arguments = this
            }
        }
    }
}
