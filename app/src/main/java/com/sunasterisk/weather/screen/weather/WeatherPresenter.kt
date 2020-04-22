package com.sunasterisk.weather.screen.weather

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.WeatherRepository
import com.sunasterisk.weather.data.source.remote.OnFetchDataJsonListener

class WeatherPresenter(
    private val repository: WeatherRepository
) : WeatherContract.Presenter {
    private var view: WeatherContract.View? = null

    override fun setView(view: WeatherContract.View) {
        this.view = view
    }

    override fun onStart() {}

    override fun getWeather(latitude: Double, longitude: Double) {
        view?.onProgressLoading(true)
        repository.getWeather(
            latitude,
            longitude,
            object : OnFetchDataJsonListener<Weather> {
                override fun onSuccess(data: Weather?) {
                    view?.onProgressLoading(false)
                    data?.let {
                        view?.onGetCurrentWeatherSuccess(it)
                        repository.insertWeather(it)
                    }
                }

                override fun onError(e: Exception?) {
                    view?.onProgressLoading(false)
                    e?.let { view?.onError(e) }
                }
            }
        )
    }

    override fun getWeatherLocal() {
        repository.getWeathers()?.let {
            if (it.isNotEmpty()) {
                view?.onGetCurrentWeatherSuccess(it[0])
            }
        }
    }

    override fun onStop() {}
}
