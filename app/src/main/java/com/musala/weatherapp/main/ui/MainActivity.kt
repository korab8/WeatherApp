package com.musala.weatherapp.main.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.musala.weatherapp.R
import com.musala.weatherapp.databinding.ActivityMainBinding
import com.musala.weatherapp.databinding.ContentMainBinding
import com.musala.weatherapp.databinding.ErrorLayoutBinding
import com.musala.weatherapp.main.data.models.WeatherModel
import com.musala.weatherapp.main.viewmodel.MainViewModel
import com.musala.weatherapp.network.APIEndpoints
import com.musala.weatherapp.network.util.Status
import com.musala.weatherapp.util.Constants
import com.musala.weatherapp.util.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.musala.weatherapp.util.Constants.clearWeatherCode
import com.musala.weatherapp.util.Constants.cloudyWeatherRange
import com.musala.weatherapp.util.Constants.rainyWeatherRange
import com.musala.weatherapp.util.Constants.snowyWeatherRange
import com.musala.weatherapp.util.Location
import com.musala.weatherapp.util.SharedPreferencesManager
import com.musala.weatherapp.util.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contentMainBinding: ContentMainBinding
    private lateinit var errorLayoutBinding: ErrorLayoutBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    @Inject
    lateinit var endpoints: APIEndpoints

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        contentMainBinding = binding.contentMain
        errorLayoutBinding = binding.errorLayout
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {

        if (sharedPreferencesManager.getLocation().isNotInitialized()) {
            getCurrentLocation()
        } else {
            mainViewModel.getWeather(sharedPreferencesManager.getLocation())
        }

        binding.errorLayout.retryBtn.setOnClickListener {
            mainViewModel.getWeather(sharedPreferencesManager.getLocation())
        }

        subscribeUI()
    }

    private fun getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (isPermissionGranted()) {
            mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                Log.d("MainActivity", "Location: Success")
                sharedPreferencesManager.saveLocation(
                    Location(
                        location.latitude.toFloat(),
                        location.longitude.toFloat(),
                        ""
                    )
                )
                mainViewModel.getWeather(sharedPreferencesManager.getLocation())
            }.addOnFailureListener {
                Log.d("MainActivity", "Location: Failure")
                showErrorView(it.message ?: "Something went wrong!")
            }
        } else {
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            } else {
                showErrorView("Please allow location permission!")
            }

        }
    }

    private fun isPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            buildLocationPermissionArray(),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun buildLocationPermissionArray(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }


    private fun subscribeUI() {
        mainViewModel.resWeather.observe(this) {
            binding.progressBar.setVisible { it.status == Status.LOADING }
            when (it.status) {
                Status.SUCCESS -> {
                    sharedPreferencesManager.saveLocation(
                        Location(
                            it.data?.coord?.lat ?: 0.0f,
                            it.data?.coord?.lon ?: 0.0f,
                            it.data?.name ?: ""
                        )
                    )
                    updateUIWithModel(it.data)
                }
                Status.ERROR -> {
                    showErrorView(it.message ?: "Something went wrong!")
                }
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.home_menu, menu)
        val menuItem = menu.findItem(R.id.searchItem)
        initSearchView(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showErrorView(msg: String) {
        errorLayoutBinding.root.show()
        contentMainBinding.root.hide()
        errorLayoutBinding.errorMsg.text = msg
    }

    private fun updateUIWithModel(weatherModel: WeatherModel?) {
        if (weatherModel == null) {
            showErrorView("Failed to load data. Retry!")
        } else {
            contentMainBinding.root.show()
            errorLayoutBinding.root.hide()
            with(contentMainBinding) {
                city.text = weatherModel.name
                weatherConditions.text = weatherModel.weather.firstOrNull()?.main ?: "Unknown"
                when (weatherModel.weather.firstOrNull()?.id ?: 800) {
                    clearWeatherCode -> weatherIcon.setImageDrawable(getDrawable(R.drawable.ic_sunny))
                    in rainyWeatherRange -> weatherIcon.setImageDrawable(getDrawable(R.drawable.ic_rainy))
                    in cloudyWeatherRange -> weatherIcon.setImageDrawable(getDrawable(R.drawable.ic_cloudy))
                    in snowyWeatherRange -> weatherIcon.setImageDrawable(getDrawable(R.drawable.ic_snow))
                    else -> weatherIcon.setImageDrawable(getDrawable(R.drawable.ic_unknown))
                }
                temperature.text = "${weatherModel.main.temp}°"
                windValue.text = "${weatherModel.wind.speed} m/s"
                humidityValue.text = "${weatherModel.main.humidity} %"
                visibility.text = weatherModel.visibility.visibilityString()
            }
        }
    }

    private fun Int.visibilityString(): String =
        if (this == 10000) {
            "MAX"
        } else {
            "$this m"
        }


    private fun initSearchView(menuItem: MenuItem) {
        val searchView = menuItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                contentMainBinding.root.hide()
                sharedPreferencesManager.saveLocation(Location(city = query ?: ""))
                mainViewModel.getWeather(Location(city = query ?: ""))
                menuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //do nothing
                return false
            }

        })
    }
}