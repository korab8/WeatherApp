package com.musala.weatherapp.main.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musala.weatherapp.R
import com.musala.weatherapp.main.data.MainRepository
import com.musala.weatherapp.main.data.models.Weather
import com.musala.weatherapp.main.data.models.WeatherModel
import com.musala.weatherapp.network.util.Resource
import com.musala.weatherapp.util.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val resources: Resources
) : ViewModel() {

    private val _resWeather = MutableLiveData<Resource<WeatherModel>>()

    val resWeather: LiveData<Resource<WeatherModel>>
        get() = _resWeather


    fun getWeather(location: Location) = _getWeather(location)

    private fun _getWeather(location: Location) = viewModelScope.launch {
        _resWeather.postValue(Resource.loading())

        try {
            mainRepository.getWeatherForLocation(location).let {
                if (it.isSuccessful) {
                    _resWeather.postValue(Resource.success(it.body()))
                } else {
                    _resWeather.postValue(Resource.error(it.message(), null))
                }
            }
        } catch (ex: Exception) {
            _resWeather.postValue(
                Resource.error(
                    resources.getString(R.string.check_your_connection),
                    null
                )
            )
        }
    }
}