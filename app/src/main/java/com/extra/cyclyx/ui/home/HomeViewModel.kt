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
    var isLocationSettingsEnabled : Boolean = false

    val stringMonth : String = (Calendar.getInstance()).getDisplayName(Calendar.MONTH,Calendar.LONG, indonesianLocale) ?: "ERROR"
    val recentActs = repository.recentsCyclingData
    private val _navigateToResult = MutableLiveData<Long>()
    val navigateToResult: LiveData<Long>
        get() = _navigateToResult

    private val _navigateToKonfigurasi = MutableLiveData<Int>()
    val navigateToKonfigurasi : LiveData<Int>
        get() = _navigateToKonfigurasi

    val firstRecentActs = Transformations.map(recentActs){act ->
        act?.let {
            it[0]
        }
    }
    val secondRecentActs = Transformations.map(recentActs){act ->
        act?.let {
            it[1]
        }
    }

    fun onBtnKonfigurasiClicked(){
        _navigateToKonfigurasi.value = 1
    }

    fun onActClicked(id: Long) {
        _navigateToResult.value = id
    }

    fun doneNavigateToHasilBersepeda() {
        _navigateToResult.value = null
    }

    fun doneNavigateToKonfigurasi() {
        _navigateToResult.value = null
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

    private fun checkLocationSettings(){

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