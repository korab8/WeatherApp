package com.musala.weatherapp.network.helper

import com.musala.weatherapp.main.data.models.WeatherModel
import retrofit2.Response

interface ApiHelper {
    suspend fun getWeatherForLocation(lat: Float, lon: Float): Response<WeatherModel>

    suspend fun getWeatherForCity(city: String): Response<WeatherModel>
}