package com.musala.weatherapp.main.data

import com.musala.weatherapp.network.helper.ApiHelper
import com.musala.weatherapp.util.Location
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    suspend fun getWeatherForLocation(location: Location) = if (location.isCity()) {
        apiHelper.getWeatherForCity(location.city)
    } else {
        apiHelper.getWeatherForLocation(location.lat, location.lng)
    }
}