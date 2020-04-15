package com.sunasterisk.weather.data.source.remote.fetchjson

import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import com.sunasterisk.weather.data.model.entity.WeatherStatisticsEntry
import com.sunasterisk.weather.data.model.entity.Wind
import com.sunasterisk.weather.data.model.entity.WindEntry
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ParseDataWithJson {

    @Throws(Exception::class)
    fun getJsonFromUrl(urlString: String?): String? {
        val url = URL(urlString)
        val httpURLConnection =
            url.openConnection() as HttpURLConnection
        httpURLConnection.connectTimeout = TIME_OUT
        httpURLConnection.readTimeout = TIME_OUT
        httpURLConnection.requestMethod = METHOD_GET
        httpURLConnection.doOutput = true
        httpURLConnection.connect()
        val bufferedReader =
            BufferedReader(InputStreamReader(url.openStream()))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        httpURLConnection.disconnect()
        return stringBuilder.toString()
    }

    private fun parseJsonToDataWeather(
        jsonObject: JSONObject,
        parameterObject: String
    ): MutableList<WeatherStatistics>? {
        val dataWeatherList = mutableListOf<WeatherStatistics>()
        try {
            val jsonArray = jsonObject.getJSONArray(WeatherStatisticsEntry.WEATHER)
            when(parameterObject) {
                WeatherEntry.DAILY_OBJECT -> {
                    for (i in 0 until jsonArray.length()) {
                        val weatherDataJson = jsonArray.getJSONObject(i)
                        val weatherData =
                            WeatherStatistics(weatherDataJson.getLong(WeatherStatisticsEntry.TIME),
                                null,
                                weatherDataJson.getString(WeatherStatisticsEntry.ICON),
                                null,
                                weatherDataJson.getDouble(WeatherStatisticsEntry.TEMPERATURE_MIN),
                                weatherDataJson.getDouble(WeatherStatisticsEntry.TEMPERATURE_MAX),
                                null,
                                null)
                        dataWeatherList.add(weatherData)
                    }
                }
                WeatherEntry.HOURLY_OBJECT -> {
                    for (i in 0 until jsonArray.length()) {
                    val weatherDataJson = jsonArray.getJSONObject(i)
                    val weatherData =
                        WeatherStatistics(weatherDataJson.getLong(WeatherStatisticsEntry.TIME),
                            null,
                            weatherDataJson.getString(WeatherStatisticsEntry.ICON),
                            weatherDataJson.getDouble(WeatherStatisticsEntry.TEMPERATURE),
                            null,
                            null,
                            null,
                            null)
                        dataWeatherList.add(weatherData)
                    }
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return dataWeatherList
    }

    private fun parseJsonToObject(jsonObject: JSONObject): WeatherStatistics? {
        var weatherStatistics: WeatherStatistics? = null
        val dailyWeather =
            parseJsonToDataWeather(jsonObject.getJSONObject(WeatherEntry.DAILY_OBJECT),
                                    WeatherEntry.DAILY_OBJECT)
        val jsonObjectCurrent = jsonObject.getJSONObject(WeatherEntry.CURRENTLY_OBJECT)
        try {
            weatherStatistics =
                WeatherStatistics(jsonObjectCurrent.getLong(WeatherStatisticsEntry.TIME),
                    jsonObjectCurrent.getString(WeatherStatisticsEntry.SUMMARY),
                    jsonObjectCurrent.getString(WeatherStatisticsEntry.ICON),
                    jsonObjectCurrent.getDouble(WeatherStatisticsEntry.TEMPERATURE),
                    dailyWeather?.get(0)?.temperatureMin,
                    dailyWeather?.get(0)?.temperatureMax,
                    jsonObjectCurrent.getDouble(WeatherStatisticsEntry.HUMIDITY),
                    Wind(jsonObjectCurrent.getInt(WindEntry.WIND_DIRECTION),
                         jsonObjectCurrent.getDouble(WindEntry.WIND_SPEED)))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return weatherStatistics
    }

    fun parseJsonToWeather(jsonObject: JSONObject): Weather? {
        var weather: Weather? = null
        try {
            val dailyWeatherList =
                parseJsonToDataWeather(jsonObject.getJSONObject(WeatherEntry.DAILY_OBJECT),
                                        WeatherEntry.DAILY_OBJECT)
            val hourlyWeatherList =
                parseJsonToDataWeather(jsonObject.getJSONObject(WeatherEntry.HOURLY_OBJECT),
                    WeatherEntry.HOURLY_OBJECT)
            weather =
                Weather(jsonObject.getDouble(WeatherEntry.LATITUDE),
                        jsonObject.getDouble(WeatherEntry.LONGITUDE),
                        jsonObject.getString(WeatherEntry.TIMEZONE),
                        parseJsonToObject(jsonObject),
                        hourlyWeatherList,
                        dailyWeatherList)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return weather
    }

    companion object {
        private const val TIME_OUT = 15000
        private const val METHOD_GET = "GET"
    }
}
