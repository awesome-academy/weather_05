package com.sunasterisk.weather.screen.cities

import com.sunasterisk.weather.data.source.WeatherRepository

class CitiesPresenter(
    private val repository: WeatherRepository
) : CitiesContract.Presenter {
    private var view: CitiesContract.View? = null

    override fun getWeathersLocal() {
        view?.onWeatherSuccess(repository.getWeathers())
    }

    override fun deleteWeather(id: String) {
        repository.deleteWeather(id)
    }

    override fun onStart() {}

    override fun onStop() {}

    override fun setView(view: CitiesContract.View) {
        this.view = view
    }
}
