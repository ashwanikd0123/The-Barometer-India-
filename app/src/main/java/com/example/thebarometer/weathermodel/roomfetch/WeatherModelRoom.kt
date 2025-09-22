package com.example.thebarometer.weathermodel.roomfetch

import android.content.Context
import androidx.room.Room

class WeatherModelRoom(context: Context) {
    private val db = Room.databaseBuilder(context, WeatherDatabase::class.java, "weather-db").build()
    private val dao = db.getWeatherDao()

    fun getData(city: String): List<WeatherDataCity> {
        return dao.findData(city)
    }

    fun addData(data: WeatherDataCity) {
        dao.addData(data)
    }

    fun updateData(data: WeatherDataCity) {
        dao.updateData(data)
    }
}