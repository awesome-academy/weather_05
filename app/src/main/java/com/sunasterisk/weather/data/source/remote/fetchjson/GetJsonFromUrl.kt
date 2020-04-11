package com.sunasterisk.weather.data.source.remote.fetchjson

import android.os.AsyncTask
import com.sunasterisk.weather.data.model.Weather
import com.sunasterisk.weather.data.source.remote.OnFetchDataJsonListener
import org.json.JSONObject

class GetJsonFromUrl(private val listener: OnFetchDataJsonListener<Weather>) :
    AsyncTask<String, Void, String>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg strings: String?): String? {
        var data: String? = null
        try {
            val parseDataWithJson = ParseDataWithJson()
            data = parseDataWithJson.getJsonFromUrl(strings[0]).toString()
        } catch (e: Exception) {
            exception = e
        }
        return data
    }

    override fun onPostExecute(data: String?) {
        super.onPostExecute(data)
        data?.let { listener.onSuccess(ParseDataWithJson().parseJsonToWeather(JSONObject(it))) }
            ?: exception?.let(listener::onError)
    }
}
