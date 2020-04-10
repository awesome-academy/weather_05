package com.sunasterisk.weather.utils

interface BasePresenter<T> {

    fun onStart()

    fun onStop()

    fun setView(view: T)
}
