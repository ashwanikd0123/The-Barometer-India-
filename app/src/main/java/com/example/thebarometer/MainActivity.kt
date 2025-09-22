package com.example.thebarometer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thebarometer.ui.theme.TheBarometerTheme
import com.example.thebarometer.weathermodel.WeatherForecastData
import com.example.thebarometer.weatherviewmodel.WeatherViewModel

lateinit var API_KEY : String

class MainActivity : ComponentActivity() {
    val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.loadCityData(this)
        setContent {
            TheBarometerTheme {
                Greeting(viewModel)
            }
        }

        API_KEY = resources.getString(R.string.api_key)

        viewModel.data_load_error.observe(this, {
            if (it) {
                if (viewModel.city_found.value!!) {
                    Toast.makeText(this, "City not found in database", Toast.LENGTH_SHORT).show()
                    return@observe
                }
                Toast.makeText(this, "Error Loading Data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

@Composable
fun Greeting(viewModel: WeatherViewModel) {
    val weatherData : WeatherForecastData? by viewModel.weather_data.observeAsState()
    val citiesLoading : Boolean? by viewModel.city_list_loading.observeAsState()
    val fetchingData : Boolean? by viewModel.fetching_data.observeAsState()

    if (fetchingData!!) {
        Column(Modifier
            .padding(8.dp)
            .padding(top = 24.dp)
            .fillMaxWidth(1f)
            .fillMaxSize(1f),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "Fetching Data...",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(0.3f)
                .align(Alignment.CenterHorizontally))
        }

        return
    }

    if (citiesLoading!!) {
        Column(Modifier
            .padding(8.dp)
            .padding(top = 24.dp)
            .fillMaxWidth(1f)
            .fillMaxSize(1f),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = "Loading Cities Data...",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(0.3f)
                .align(Alignment.CenterHorizontally))
        }
        return
    }

    var textValue by remember { mutableStateOf("") }

    Column(Modifier
        .padding(8.dp)
        .padding(top = 24.dp)
        .fillMaxWidth(1f)
        .fillMaxSize(1f),
        verticalArrangement = Arrangement.Center) {
        Text(
            text = "The Barometer (India)",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        ShowWeatherData(weatherData, viewModel.city_name.value)

        Row(Modifier
            .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {

            TextField(value = textValue,
                onValueChange = {textValue = it},
                label = { Text("Enter City") },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.9f))
        }

        Row(Modifier
            .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {

            Button(onClick = {
                val city = textValue
                viewModel.updateCity(city, API_KEY)
            },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(0.9f)) {
                Text("Fetch Data")
            }
        }
    }
}

@Composable
fun ShowWeatherData(weatherData: WeatherForecastData?, city: String?) {
    if (weatherData == null || city == null) {
        return
    }
    Row(Modifier
        .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
        Text(city,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically))
    }

    Row(Modifier
        .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
        Text(
            text = "Date",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "Temperature",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
        )
        Text(
            text = "Weather",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(8.dp)
        )
    }

    for (data in weatherData.data) {
        Row(
            Modifier
                .fillMaxWidth(1f),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            Text(
                text = data.date,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = data.temperature,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = data.weatherCondition,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TheBarometerTheme {
        val viewModel = WeatherViewModel()
        Greeting(viewModel)
    }
}