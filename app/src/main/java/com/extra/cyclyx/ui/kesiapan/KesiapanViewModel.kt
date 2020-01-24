package com.extra.cyclyx.ui.kesiapan

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


class KesiapanViewModel(val app: Application) : AndroidViewModel(app) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    //init permission related
    var isLocationPermissionGranted: Boolean = false
    var isBatteryOptimized: Boolean = false
    var isLocationSettingsEnabled : Boolean = false
    //init navigation live data
    private var _navigateToBersepeda = MutableLiveData<Boolean>()
    val navigateToBersepeda: LiveData<Boolean>
        get() = _navigateToBersepeda

    val stringMonth : String = (Calendar.getInstance()).getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.US) ?: "ERROR"

    init {
        //checkLocationPermission
        checkLocationPermission()
        //checkBatteryOptimization
        checkBatteryOptimization()
        //checkLocationService
        checkLocationSettings(app.applicationContext)

        Log.d("TRACKING","Location Settings = ${isLocationSettingsEnabled}")
        Log.d("TRACKING","Location Permission = ${isLocationPermissionGranted}")
        Log.d("TRACKING","Battery Optimized = ${isBatteryOptimized}")
    }
    /**
     * return false if in settings "Not optimized" and true if "Optimizing battery use"
     */
    private fun checkBatteryOptimization(){
        val pwrm = app.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = app.applicationContext.packageName
        isBatteryOptimized = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !pwrm.isIgnoringBatteryOptimizations(name)
        }else{
            false
        }
    }

    //return true if granted and false if not granted
    private fun checkLocationPermission(){
        isLocationPermissionGranted =
                ContextCompat.checkSelfPermission(app.applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationSettings(context : Context){
        isLocationSettingsEnabled =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isLocationEnabled
        } else {
            // This is Deprecated in API 28
            @Suppress("DEPRECATION")
            val mode : Int = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(KesiapanViewModel::class.java)){
                return KesiapanViewModel(
                        app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}