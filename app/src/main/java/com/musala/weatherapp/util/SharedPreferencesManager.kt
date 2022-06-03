package com.musala.weatherapp.util

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by KMUHADRI on 6/2/21.
 */
class SharedPreferencesManager @Inject constructor(var sharedPreferences: SharedPreferences) {

    fun getLocation(): LatLng =
        LatLng(sharedPreferences.getFloat(Constants.LOCATION_KEY_LATITUDE, 0.0f), sharedPreferences.getFloat(Constants.LOCATION_KEY_LONGITUDE, 0.0f))


    fun saveLocation(location: LatLng) {
        val sharedPrefs = sharedPreferences.edit()
        sharedPrefs.putFloat(Constants.LOCATION_KEY_LATITUDE, location.lat)
        sharedPrefs.putFloat(Constants.LOCATION_KEY_LONGITUDE, location.lng)
        sharedPrefs.apply()
    }

}