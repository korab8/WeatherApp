package com.musala.weatherapp.network

import com.musala.weatherapp.main.data.models.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIEndpoints {
    @GET("weather")
    suspend fun getWeatherForLocation(
        @Query("lat") lat: Float,
        @Query("lon") lng: Float
    ): Response<WeatherModel>

    @GET("weather")
    suspend fun getWeatherForCity(
        @Query("q") city: String
    ): Response<WeatherModel>
}