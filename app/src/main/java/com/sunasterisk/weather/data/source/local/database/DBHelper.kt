package com.sunasterisk.weather.data.source.local.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.model.WeatherEntry
import com.sunasterisk.weather.data.model.entity.WeatherStatistics
import com.sunasterisk.weather.data.model.entity.WeatherStatisticsEntry
import com.sunasterisk.weather.data.model.entity.WindEntry
import com.sunasterisk.weather.data.source.remote.fetchjson.ParseDataWithJson
import com.sunasterisk.weather.utils.WeatherUtils
import org.json.JSONException
import org.json.JSONObject

class DBHelper(
    private val context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.apply {
            execSQL(SQL_CREATE_TABLE_WEATHER)
            execSQL(SQL_CREATE_TABLE_CURRENT)
            execSQL(SQL_CREATE_TABLE_HOURLY)
            execSQL(SQL_CREATE_TABLE_DAILY)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.apply {
            execSQL(SQL_DROP_TABLE_CURRENT)
            execSQL(SQL_DROP_TABLE_HOURLY)
            execSQL(SQL_DROP_TABLE_DAILY)
            execSQL(SQL_DROP_TABLE_WEATHER)
            onCreate(this)
        }
    }

    private fun createJsonObject(vararg any: Any?): String {
        val jsonObject = JSONObject()
        try {
            jsonObject.apply {
                put(WeatherStatisticsEntry.TIME, any[0])
                put(WeatherStatisticsEntry.SUMMARY, any[1])
                put(WeatherStatisticsEntry.ICON, any[2])
                put(WeatherStatisticsEntry.TEMPERATURE, any[3])
                put(WeatherStatisticsEntry.TEMPERATURE_MIN, any[4])
                put(WeatherStatisticsEntry.TEMPERATURE_MAX, any[5])
                put(WeatherStatisticsEntry.HUMIDITY, any[6])
                put(WindEntry.WIND_SPEED, any[7])
                put(WindEntry.WIND_DIRECTION, any[8])
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    fun insertWeather(weather: Weather) {
        val latitude = weather.latitude?: 0.0
        val longitude = weather.longitude?:0.0
        val idWeather =
            WeatherUtils.formatNameLocation(context, latitude, longitude)?: ""
        val cursorWeather = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_WEATHER WHERE $COLUMN_ID_WEATHER = '$idWeather'",
            null)
        cursorWeather.apply {
            if (moveToFirst() && count > 0) {
                deleteWeather(idWeather)
            }
        }
        ContentValues().apply {
            put(COLUMN_ID_WEATHER, idWeather)
            put(WeatherEntry.LATITUDE, weather.latitude)
            put(WeatherEntry.LONGITUDE, weather.longitude)
            writableDatabase.insert(TABLE_WEATHER, null, this)
        }
        insertCurrently(weather.weatherCurrent, idWeather, TABLE_CURRENT)
        weather.weatherHourlyList?.let {
            insertData(it, idWeather, TABLE_HOURLY)
        }
        weather.weatherDailyList?.let {
            insertData(it, idWeather, TABLE_DAILY)
        }
    }

    private fun insertData(
        listData: List<WeatherStatistics>,
        idWeather: String,
        tableName: String
    ) {
        listData.forEach { element ->
            insertCurrently(element, idWeather, tableName)
        }
    }

    private fun insertCurrently(
        weatherStatistics: WeatherStatistics?,
        idWeather: String,
        tableName: String
    ) {
        weatherStatistics?.apply {
            ContentValues().apply {
                put(COLUMN_VALUE, createJsonObject(
                    time, summary, icon, temperature, temperatureMin, temperatureMax,
                    humidity, wind?.windSpeed, wind?.windDirection))
                put(COLUMN_ID_WEATHER, idWeather)
                writableDatabase.insert(tableName, null, this)
            }
        }
    }

    fun  getAllData(): List<Weather> {
        val weatherList = mutableListOf<Weather>()
        val weatherCursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_WEATHER",
            null
        )
        weatherCursor.apply {
            moveToFirst()
            while (!isAfterLast) {
                val idWeather = weatherCursor.getString(getColumnIndex(COLUMN_ID_WEATHER))
                weatherList.add(Weather(getDouble(getColumnIndex(WeatherEntry.LATITUDE)),
                getDouble(getColumnIndex(WeatherEntry.LONGITUDE)),
                null,
                getWeatherElement(TABLE_CURRENT, idWeather) as WeatherStatistics,
                getWeatherElement(TABLE_HOURLY, idWeather) as List<WeatherStatistics>,
                getWeatherElement(TABLE_DAILY, idWeather) as List<WeatherStatistics>
                ))
                moveToNext()
            }
        }
        return weatherList
    }

    private fun getWeatherElement(tableName: String, idWeather: String): Any? {
        val listData = mutableListOf<WeatherStatistics>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM $tableName WHERE $COLUMN_ID_WEATHER = '$idWeather'",
            null)
        cursor.apply {
            when(tableName) {
                TABLE_CURRENT -> {
                    moveToFirst()
                    while (!isAfterLast) {
                        val value = JSONObject(getString(getColumnIndex(COLUMN_VALUE)))
                        val temperatureMin =
                            value.getDouble(WeatherStatisticsEntry.TEMPERATURE_MIN)
                        val temperatureMax =
                            value.getDouble(WeatherStatisticsEntry.TEMPERATURE_MAX)
                        moveToNext()
                        return ParseDataWithJson().parseJsonToObject(
                            value, temperatureMin, temperatureMax
                        )
                    }
                }
                TABLE_HOURLY -> {
                    moveToFirst()
                    while (!isAfterLast) {
                        val value = JSONObject(getString(getColumnIndex(COLUMN_VALUE)))
                        listData.add(ParseDataWithJson().parseJsonElementWeather(
                            value, WeatherEntry.HOURLY_OBJECT))
                        moveToNext()
                    }
                    return listData
                }
                else -> {
                    moveToFirst()
                    while (!isAfterLast) {
                        val value = JSONObject(getString(getColumnIndex(COLUMN_VALUE)))
                        listData.add(ParseDataWithJson().parseJsonElementWeather(
                            value, WeatherEntry.DAILY_OBJECT))
                        moveToNext()
                    }
                    return listData
                }
            }
        }
        return null
    }

    fun deleteWeather(idWeather: String) {
        val writeDatabase = writableDatabase
        val listCurrentCursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_CURRENT WHERE $COLUMN_ID_WEATHER = '$idWeather'",
            null
        )
        val listHourlyCursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_HOURLY WHERE $COLUMN_ID_WEATHER = '$idWeather'",
            null
        )
        val listDailyCursor = readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_DAILY WHERE $COLUMN_ID_WEATHER = '$idWeather'",
            null
        )
        deleteWeatherElement(listCurrentCursor, idWeather, TABLE_CURRENT)
        deleteWeatherElement(listHourlyCursor, idWeather, TABLE_HOURLY)
        deleteWeatherElement(listDailyCursor, idWeather, TABLE_DAILY)
        writeDatabase.delete(TABLE_WEATHER, "id_weather = '$idWeather'", null)
    }

    private fun deleteWeatherElement(cursor: Cursor, idWeather: String, tableName: String) {
        cursor.apply {
            moveToFirst()
            while (!isAfterLast) {
                writableDatabase.delete(
                    tableName, "$COLUMN_ID_WEATHER = '$idWeather'", null)
                moveToNext()
            }
        }
    }

    companion object {
        const val DATABASE_NAME = "weather.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_WEATHER = "Weather"
        private const val TABLE_CURRENT = "Currently"
        private const val TABLE_HOURLY = "Hourly"
        private const val TABLE_DAILY = "Daily"
        private const val COLUMN_ID_WEATHER = "id_weather"
        private const val COLUMN_VALUE = "value"
        // Create table
        private const val SQL_CREATE_TABLE_WEATHER = "CREATE TABLE $TABLE_WEATHER(" +
                "$COLUMN_ID_WEATHER TEXT PRIMARY KEY, " +
                "${WeatherEntry.LATITUDE} REAL, " +
                "${WeatherEntry.LONGITUDE} REAL)"
        private const val SQL_CREATE_TABLE_BODY =
                    "$COLUMN_VALUE TEXT, " +
                    "$COLUMN_ID_WEATHER TEXT, " +
                    "FOREIGN KEY($COLUMN_ID_WEATHER) REFERENCES $TABLE_WEATHER($COLUMN_ID_WEATHER)"
        private const val SQL_CREATE_TABLE_CURRENT =
            "CREATE TABLE $TABLE_CURRENT($SQL_CREATE_TABLE_BODY)"
        private const val SQL_CREATE_TABLE_HOURLY =
            "CREATE TABLE $TABLE_HOURLY($SQL_CREATE_TABLE_BODY)"
        private const val SQL_CREATE_TABLE_DAILY =
            "CREATE TABLE $TABLE_DAILY($SQL_CREATE_TABLE_BODY)"
        private const val SQL_DROP_TABLE_CURRENT = "DROP TABLE IF EXISTS $TABLE_CURRENT"
        private const val SQL_DROP_TABLE_HOURLY = "DROP TABLE IF EXISTS $TABLE_HOURLY"
        private const val SQL_DROP_TABLE_DAILY = "DROP TABLE IF EXISTS $TABLE_DAILY"
        private const val SQL_DROP_TABLE_WEATHER = "DROP TABLE IF EXISTS $TABLE_WEATHER"

        fun getInstance(context: Context?) = DBHelper(context)
    }
}
