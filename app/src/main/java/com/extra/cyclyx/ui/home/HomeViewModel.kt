package com.extra.cyclyx.ui.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
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

        Log.d("TRACKING","Location Permission = ${isLocationPermissionGranted}")
        Log.d("TRACKING","Battery Optimized = ${isBatteryOptimized}")
    }
    /**
     * return false if in settings "Not optimized" and true if "Optimizing battery use"
     */
    private fun checkBatteryOptimization(){
        val pwrm = app.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = app.applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isBatteryOptimized = !pwrm.isIgnoringBatteryOptimizations(name)
        }else{
            isBatteryOptimized = false
        }
    }

    //return true if granted and false if not granted
    private fun checkLocationPermission(){
        isLocationPermissionGranted =
            ContextCompat.checkSelfPermission(app.applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationSettings(){

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}