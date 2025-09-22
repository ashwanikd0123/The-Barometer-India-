package com.example.thebarometer.weathermodel.retrofitfetch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFetchRetrofit {
    suspend fun fetchWeather(city: String, apiKey: String, callback: (ForecastResponse?) -> Unit) {
        val response = RetrofitClient.instance.getThreeDayForecast(city = city, apiKey = apiKey)
        callback(response)
    }
}