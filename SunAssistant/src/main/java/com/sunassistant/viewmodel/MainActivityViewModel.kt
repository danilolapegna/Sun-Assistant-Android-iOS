package com.sunassistant.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.sunassistant.coderesources.CommonStrings_User
import com.sunassistant.rest.meteo.UvIndexListener
import com.sunassistant.rest.meteo.BaseUvIndexSourceModel
import com.sunassistant.rest.meteo.model.FullUVIndexResponse
import com.sunassistant.rest.meteo.model.ForecastItem
import com.sunassistant.util.AirQualityLevel
import com.sunassistant.util.LocationUtils
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel(), UvIndexListener {

    enum class AirQualityIndexType {
        USA,
        EU
    }

    data class AirQualityIndex(val index: Double?, val type: AirQualityIndexType) {
        val qualityLevel: AirQualityLevel
            get() = AirQualityLevel.getLevelByIndex(index, type == AirQualityIndexType.USA)
    }

    val isLoading = MutableLiveData<Boolean>()
    val uvIndexString = MutableLiveData<String>()
    val uvIndex = MutableLiveData<Double?>()
    val currentLocation = MutableLiveData<Location?>()
    val hasUvFetchError = MutableLiveData<Boolean>()
    var isDay = MutableLiveData<Boolean?>()
    var lastFetchedTime = MutableLiveData<Long?>()
    var locationReadable = MutableLiveData<String>()
    var airQualityIndex = MutableLiveData<AirQualityIndex?>()
    var currentWeather = MutableLiveData<Int?>()
    var currentTemperature = MutableLiveData<Double?>()
    var currentSunshineDuration = MutableLiveData<Double?>()
    var forecastPreviews = mutableListOf<ForecastItem>()

    var lastFetchedLocation: Location? = null

    @SuppressLint("StaticFieldLeak")
    private var globalViewModelContext: Context? = null

    private val uvIndexViewModel = BaseUvIndexSourceModel().apply {
        listener = this@MainActivityViewModel
    }

    private var locationPermissionGranted: Boolean = false

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private fun fetchUvData(latitude: Double, longitude: Double, ignoreCache: Boolean) {
        uvIndexViewModel.fetchUvIndex(latitude, longitude, ignoreCache)
    }

    private fun handleLocationUpdate(
        location: Location?,
        forceUpdate: Boolean = true
    ) {
        if (location != null && (location != lastFetchedLocation || forceUpdate)) {
            lastFetchedLocation = location
            fetchUvData(location.latitude, location.longitude, ignoreCache = forceUpdate)
        } else {
            stopLoading()
        }
    }

    fun refresh(
        activity: ComponentActivity,
        forceUpdate: Boolean = true,
        canRegisterForPermissions: Boolean = false
    ) {
        globalViewModelContext = activity
        checkLocationPermission(activity, forceUpdate, canRegisterForPermissions)
    }

    private fun checkLocationPermission(
        activity: ComponentActivity,
        forceUpdate: Boolean,
        canRegisterForPermissions: Boolean
    ) {
        val isGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        locationPermissionGranted = isGranted
        if (isGranted) {
            initLocationClientAndFetchData(activity, forceUpdate)
        } else {
            if (canRegisterForPermissions) {
                requestLocationPermissions(activity, forceUpdate)
            } else {
                setError(CommonStrings_User.location_authorization_denied, activity)
            }
        }
    }

    private fun initLocationClientAndFetchData(context: Context, forceUpdate: Boolean) {
        if (fusedLocationClient == null) fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
        getLocationAndFetchData(forceUpdate, context)
    }

    private fun requestLocationPermissions(activity: ComponentActivity, forceUpdate: Boolean) {
        val locationPermissionRequest: ActivityResultLauncher<Array<String>> =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        locationPermissionGranted = true
                        initLocationClientAndFetchData(activity, forceUpdate)
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        locationPermissionGranted = true
                        initLocationClientAndFetchData(activity, forceUpdate)
                    }

                    else -> {
                        setError(CommonStrings_User.location_authorization_denied, activity)
                    }
                }
            }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun stopLoading() {
        isLoading.postValue(false)
    }

    private fun startLoading() {
        isLoading.postValue(true)
    }

    private fun getLocationAndFetchData(forceUpdate: Boolean, context: Context) {
        startLoading()
        if (!locationPermissionGranted) {
            setError(CommonStrings_User.location_authorization_denied, context)
            return
        }

        viewModelScope.launch {
            try {
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    processLocationResult(task, forceUpdate, context)
                }
            } catch (e: SecurityException) {
                setError(CommonStrings_User.location_retrieval_error, context)
            }
        }
    }

    private fun processLocationResult(task: Task<Location>, forceUpdate: Boolean, context: Context) {
        if (!task.isSuccessful || task.result == null) {
            setError(CommonStrings_User.location_retrieval_error, context)
            return
        }

        lastFetchedLocation = task.result
        if ((lastFetchedLocation != currentLocation.value) || forceUpdate) {
            currentLocation.postValue(lastFetchedLocation)
            handleLocationUpdate(lastFetchedLocation, forceUpdate = forceUpdate)
        } else {
            stopLoading()
        }
    }

    override fun onDataLoaded(data: FullUVIndexResponse) {
        stopLoading()
        processError(data)
        processUvIndexData(data)
    }

    private fun processError(data: FullUVIndexResponse) {
        hasUvFetchError.postValue(data.exception != null)
        data.exception?.let { setError(it.message ?: CommonStrings_User.unknown_error, globalViewModelContext) }
    }

    private fun processUvIndexData(data: FullUVIndexResponse) {
        data.uvIndexCurrent().let { currentUv ->
            if (uvIndex.value == null || currentUv != null) {
                uvIndex.postValue(currentUv)
                lastFetchedTime.postValue(data.getUvIndexFetchedTimeInEpochMs())
                uvIndexString.postValue(uvIndex.value.toString())
            }
        }

        val newAirQuality = data.EUAirQualityCurrent()?.let {
            AirQualityIndex(it, AirQualityIndexType.EU)
        } ?: data.USAirQualityCurrent()?.let {
            AirQualityIndex(it, AirQualityIndexType.USA)
        }

        newAirQuality.let {
            if (airQualityIndex.value == null || newAirQuality != null) {
                airQualityIndex.postValue(it)
            }
        }

        locationReadable.postValue(
            data.locationName ?: LocationUtils.latLonString(
                lastFetchedLocation?.latitude,
                lastFetchedLocation?.longitude
            )
        )

        isDay.postValue(data.isDay)

        data.forecastPreviews?.let {
            if (it.isNotEmpty()) {
                forecastPreviews.clear()
                forecastPreviews.addAll(it)
            }
        }

        currentTemperature.postValue(data.currentTemperature)
        currentSunshineDuration.postValue(data.todaySunshineDuration)
        currentWeather.postValue(data.currentWeather)
    }

    override fun onLoadStart() {
        hasUvFetchError.postValue(false)
        uvIndexString.value = CommonStrings_User.progress_loading
    }

    override fun onLoadEnd() {
        stopLoading()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {

        }
    }

    override fun onException(e: Exception) {
        stopLoading()
        setError("Error: ${e.message}", globalViewModelContext)
    }

    private fun setError(errorMessage: String, context: Context?) {
        hasUvFetchError.postValue(true)
        uvIndexString.value = errorMessage
        context?.let { Toast.makeText(it, errorMessage, Toast.LENGTH_LONG) }
        stopLoading()
    }
}
