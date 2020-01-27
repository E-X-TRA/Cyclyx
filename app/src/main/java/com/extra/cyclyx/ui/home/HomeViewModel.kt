package com.extra.cyclyx.ui.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.indonesianLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    //init permission related
    var isLocationPermissionGranted: Boolean = false
    
    private val _navigateToKesiapan = MutableLiveData<Boolean>()
    val navigateToKesiapan : LiveData<Boolean>
        get() = _navigateToKesiapan

    private val _navigateToPengaturan = MutableLiveData<Boolean>()
    val navigateToPengaturan : LiveData<Boolean>
        get() = _navigateToPengaturan

    fun onBtnPengaturanClicked(){
        _navigateToPengaturan.value = true
    }

    fun onBtnGoClicked(){
        _navigateToKesiapan.value = true
    }

    fun doneNavigateToPengaturan(){
        _navigateToPengaturan.value = null
    }

    fun doneNavigateToKesiapan(){
        _navigateToKesiapan.value = null
    }

    init {
        //checkLocationPermission
        checkLocationPermission()
        //checkLocationSettings

        Log.d("TRACKING","Location Permission = ${isLocationPermissionGranted}")
    }

    //return true if granted and false if not granted
    private fun checkLocationPermission(){
        isLocationPermissionGranted =
            ContextCompat.checkSelfPermission(app.applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
                return HomeViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}