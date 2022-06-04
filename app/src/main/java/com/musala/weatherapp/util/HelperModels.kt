package com.musala.weatherapp.util

data class Location(
    var lat: Float = 0.0f,
    var lng: Float = 0.0f,
    var city: String = ""
) {
    fun isCity() = city.trim().isNotEmpty()
    fun isNotInitialized() = lat == 0.0f && lng == 0.0f && city.isEmpty()
}