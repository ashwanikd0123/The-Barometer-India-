package com.example.thebarometer.weathermodel.roomfetch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherdatacity WHERE city = :city")
    fun findData(city: String): List<WeatherDataCity>

    @Insert()
    fun addData(data: WeatherDataCity)

    @Update()
    fun updateData(data: WeatherDataCity)
}