package com.example.thebarometer.weathermodel.roomfetch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WeatherDataCity {
    @PrimaryKey
    var city: String = ""
    @ColumnInfo(name = "date")
    var startDate: String = ""
    @ColumnInfo(name = "json")
    var json: String = ""
}