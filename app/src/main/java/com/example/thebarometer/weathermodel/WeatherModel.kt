package com.example.thebarometer.weathermodel

import android.content.Context
import android.util.Log
import com.example.thebarometer.weathermodel.retrofitfetch.ForecastResponse
import com.example.thebarometer.weathermodel.retrofitfetch.WeatherFetchRetrofit
import com.example.thebarometer.weathermodel.retrofitfetch.WeatherItem
import com.example.thebarometer.weathermodel.roomfetch.WeatherDataCity
import com.example.thebarometer.weathermodel.roomfetch.WeatherModelRoom
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherModel() {

    lateinit var cityList: List<CityItem>

    val weatherFetchRetroFit = WeatherFetchRetrofit()
    lateinit var weatherFetchRoom: WeatherModelRoom

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun readCityListJson(context: Context): String {
        return context.assets.open("city_india.json").bufferedReader().use { it.readText() }
    }

    // loads city list from json file
    fun loadCityList(context: Context) {
        cityList = Gson().fromJson(readCityListJson(context), object : TypeToken<List<CityItem>>() {}.type)
        weatherFetchRoom = WeatherModelRoom(context)
    }

    fun queryCity(cityName: String): List<CityItem> {
        return cityList.filter { it.name.contains(cityName, ignoreCase = true) }
    }

    // convert response of http to three day forecast WeatherForecastData
    fun toThreeDayForecast(apiResp: ForecastResponse): WeatherForecastData {
        for (wd: WeatherItem in apiResp.list) {
            Log.d("WeatherModel", " " +  wd.dt)
        }

        val now = System.currentTimeMillis() / 1000
        val threeDaysLater = now + 72 * 3600



        val threeDayItems = apiResp.list
            .filter { it.dt in now..threeDaysLater }
            .groupBy { sdf.format(Date(it.dt * 1000)) }

        val result = threeDayItems.entries.take(3).map { (date, items) ->
            val avgTemp = items.map { it.main.temp }.average()
            val condition = items.first().weather.first().description
            WeatherData(
                date = date,
                temperature = String.format("%.1f", avgTemp),
                weatherCondition = condition.capitalize(Locale.getDefault())
            )
        }

        return WeatherForecastData(result)
    }

    suspend fun fetchWeatherData(cityName: String, apiKey: String): WeatherForecastData? {
        try {
            // first check in room database
            val todayDate = sdf.format(Date())
            val data = weatherFetchRoom.getData(cityName)
            if (data.isNotEmpty() && data[0].startDate == todayDate) {
                return Gson().fromJson(data[0].json, object : TypeToken<WeatherForecastData>() {}.type)
            }

            // download from api
            val result: WeatherForecastData = toThreeDayForecast(weatherFetchRetroFit.fetchWeather(cityName, apiKey))

            // insert data in room database
            if (data.isNotEmpty()) {
                weatherFetchRoom.updateData(WeatherDataCity().apply {
                    city = cityName
                    startDate = todayDate
                    json = Gson().toJson(result)
                })
            } else {
                weatherFetchRoom.addData(WeatherDataCity().apply {
                    city = cityName
                    startDate = todayDate
                    json = Gson().toJson(result)
                })
            }

            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}