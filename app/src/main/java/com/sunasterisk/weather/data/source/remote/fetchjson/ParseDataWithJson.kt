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
                        dataWeatherList.add(
                            parseJsonElementWeather(weatherDataJson, WeatherEntry.DAILY_OBJECT)
                        )
                    }
                }
                WeatherEntry.HOURLY_OBJECT -> {
                    for (i in 0 until jsonArray.length()) {
                    val weatherDataJson = jsonArray.getJSONObject(i)
                        dataWeatherList.add(
                            parseJsonElementWeather(weatherDataJson, WeatherEntry.HOURLY_OBJECT)
                        )
                    }
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return dataWeatherList
    }

    fun parseJsonElementWeather(jsonObject: JSONObject, tagObject: String): WeatherStatistics {
        return when(tagObject) {
            WeatherEntry.DAILY_OBJECT -> {
                 WeatherStatistics(jsonObject.getLong(WeatherStatisticsEntry.TIME),
                    null,
                    jsonObject.getString(WeatherStatisticsEntry.ICON),
                    null,
                    jsonObject.getDouble(WeatherStatisticsEntry.TEMPERATURE_MIN),
                    jsonObject.getDouble(WeatherStatisticsEntry.TEMPERATURE_MAX),
                    null,
                    null)
            } else -> {
                WeatherStatistics(jsonObject.getLong(WeatherStatisticsEntry.TIME),
                    null,
                    jsonObject.getString(WeatherStatisticsEntry.ICON),
                    jsonObject.getDouble(WeatherStatisticsEntry.TEMPERATURE),
                    null,
                    null,
                    null,
                    null)
            }
        }
    }

    private fun parseJsonToCurrent(
        jsonObject: JSONObject,
        temperatureMin: Double?,
        temperatureMax: Double?
    ): WeatherStatistics? {
        val jsonObjectCurrent = jsonObject.getJSONObject(WeatherEntry.CURRENTLY_OBJECT)
        return parseJsonToObject(jsonObjectCurrent, temperatureMin, temperatureMax)
    }

    fun parseJsonToObject(
        jsonObject: JSONObject,
        temperatureMin: Double?, temperatureMax: Double?): WeatherStatistics? {
        try {
            return WeatherStatistics(jsonObject.getLong(WeatherStatisticsEntry.TIME),
                jsonObject.getString(WeatherStatisticsEntry.SUMMARY),
                jsonObject.getString(WeatherStatisticsEntry.ICON),
                jsonObject.getDouble(WeatherStatisticsEntry.TEMPERATURE),
                temperatureMin, temperatureMax,
                jsonObject.getDouble(WeatherStatisticsEntry.HUMIDITY),
                Wind(jsonObject.getInt(WindEntry.WIND_DIRECTION),
                    jsonObject.getDouble(WindEntry.WIND_SPEED)))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getMinMaxTemperature(jsonObject: JSONObject): HashMap<String, Double> {
        val dailyWeather =
            parseJsonToDataWeather(jsonObject.getJSONObject(WeatherEntry.DAILY_OBJECT),
                WeatherEntry.DAILY_OBJECT)
        val hashMap = HashMap<String, Double>()
        val temperatureMin = dailyWeather?.get(0)?.temperatureMin ?: 0.0
        val temperatureMax = dailyWeather?.get(0)?.temperatureMax ?: 0.0
        hashMap.apply {
            put(WeatherStatisticsEntry.TEMPERATURE_MIN, temperatureMin)
            put(WeatherStatisticsEntry.TEMPERATURE_MAX, temperatureMax)
        }
        return hashMap
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
            weather = Weather(jsonObject.getDouble(WeatherEntry.LATITUDE),
                    jsonObject.getDouble(WeatherEntry.LONGITUDE),
                    jsonObject.getString(WeatherEntry.TIMEZONE),
                    parseJsonToCurrent(jsonObject,
                        getMinMaxTemperature(jsonObject)[WeatherStatisticsEntry.TEMPERATURE_MIN],
                        getMinMaxTemperature(jsonObject)[WeatherStatisticsEntry.TEMPERATURE_MAX]),
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
