package com.musala.weatherapp.network.helper

import com.musala.weatherapp.main.data.models.WeatherModel
import com.musala.weatherapp.network.APIEndpoints
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiEndpoints: APIEndpoints): ApiHelper {
    override suspend fun getWeatherForLocation(lat: Float, lon: Float): Response<WeatherModel> = apiEndpoints.getWeatherForLocation(lat, lon)
    override suspend fun getWeatherForCity(city: String): Response<WeatherModel> = apiEndpoints.getWeatherForCity(city)
}