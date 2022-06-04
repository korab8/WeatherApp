package com.musala.weatherapp.util

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

/**
 * Created by KMUHADRI on 6/2/21.
 */
class SharedPreferencesManager @Inject constructor(var sharedPreferences: SharedPreferences) {

    fun getLocation(): Location =
        Location(
            lat = sharedPreferences.getFloat(Constants.LOCATION_KEY_LATITUDE, 0.0f),
            lng = sharedPreferences.getFloat(Constants.LOCATION_KEY_LONGITUDE, 0.0f),
            city = sharedPreferences.getString(Constants.LOCATION_KEY_CITY, "") ?: ""
        )


    fun saveLocation(location: Location) {
        val sharedPrefs = sharedPreferences.edit()
        sharedPrefs.putFloat(Constants.LOCATION_KEY_LATITUDE, location.lat)
        sharedPrefs.putFloat(Constants.LOCATION_KEY_LONGITUDE, location.lng)
        sharedPrefs.putString(Constants.LOCATION_KEY_CITY, location.city)
        sharedPrefs.apply()
    }

}