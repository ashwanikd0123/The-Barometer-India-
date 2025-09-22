package com.example.thebarometer.weathermodel.retrofitfetch

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("forecast")
    suspend fun getThreeDayForecast(
        @Query("q") city: String,            // e.g. "Lucknow,IN"
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): ForecastResponse
}