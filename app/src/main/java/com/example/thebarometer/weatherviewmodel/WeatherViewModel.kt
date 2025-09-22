package com.example.thebarometer.weatherviewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thebarometer.weathermodel.WeatherData
import com.example.thebarometer.weathermodel.WeatherForecastData
import com.example.thebarometer.weathermodel.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel() : ViewModel() {
    val weather_data : MutableLiveData<WeatherForecastData> = MutableLiveData<WeatherForecastData>()
    val city_name : MutableLiveData<String> = MutableLiveData<String>()
    val city_list_loading : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val fetching_data : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val city_found: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val data_load_error: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val weatherModel: WeatherModel = WeatherModel()

    init {
        setInvalidData()
    }

    fun loadCityData(context: Context) {
        city_list_loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            weatherModel.loadCityList(context)
            city_list_loading.postValue(false)
        }
    }

    private fun setInvalidData() {
        weather_data.postValue(WeatherForecastData(listOf(
            WeatherData("-", "-", "-"),
            WeatherData("-", "-", "-"),
            WeatherData("-", "-", "-"))))
        city_name.postValue("-")
    }

    fun updateCity(cityName: String, apiKey: String) {
        fetching_data.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val names = weatherModel.queryCity(cityName)
            if (names.isEmpty()) {
                fetching_data.postValue(false)
                city_found.postValue(false)
                data_load_error.postValue(true)
                setInvalidData()
                return@launch
            }

            city_found.postValue(true)

            val name = names[0].name + ",IN"
            val data = weatherModel.fetchWeatherData(name, apiKey)
            if (data == null || data.data.isEmpty()) {
                data_load_error.postValue(true)
                fetching_data.postValue(false)
                setInvalidData()
                return@launch
            }

            city_name.postValue(name)
            weather_data.postValue(data!!)
            fetching_data.postValue(false)
        }
    }
}