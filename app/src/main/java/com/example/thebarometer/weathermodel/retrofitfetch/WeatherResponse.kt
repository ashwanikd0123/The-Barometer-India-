package com.example.thebarometer.weathermodel.retrofitfetch

data class ForecastResponse(
    val list: List<WeatherItem>
)

data class WeatherItem(
    val dt: Long,
    val main: MainData,
    val weather: List<WeatherDesc>
)

data class MainData(
    val temp: Double,
    val feels_like: Double
)

data class WeatherDesc(
    val description: String,
    val icon: String
)