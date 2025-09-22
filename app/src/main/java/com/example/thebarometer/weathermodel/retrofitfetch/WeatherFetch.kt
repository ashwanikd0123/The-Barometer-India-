package com.example.thebarometer.weathermodel.retrofitfetch

class WeatherFetchRetrofit {
    suspend fun fetchWeather(city: String, apiKey: String): ForecastResponse {
        return RetrofitClient.instance.getThreeDayForecast(city = city, apiKey = apiKey)
    }
}