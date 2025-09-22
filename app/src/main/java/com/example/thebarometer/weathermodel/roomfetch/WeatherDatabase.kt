package com.example.thebarometer.weathermodel.roomfetch

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataCity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao() : WeatherDao
}