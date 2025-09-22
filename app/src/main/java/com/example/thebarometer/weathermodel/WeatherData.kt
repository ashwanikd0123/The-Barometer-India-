package com.example.thebarometer.weathermodel

data class WeatherData(val date: String, val temperature: String, val weatherCondition: String)

data class WeatherForecastData(val data: List<WeatherData>)