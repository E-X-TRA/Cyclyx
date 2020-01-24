package com.extra.cyclyx.ui.kesiapan

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import kotlinx.coroutines.Job


class KesiapanViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    //init permission related
    var isPowerSaverModeOn : Boolean = repository.checkPowerSaverMode()
    var isBatteryOptimized: Boolean = repository.checkBatteryOptimization()
    var isLocationSettingsEnabled : Boolean = repository.checkLocationSettings(app.applicationContext)

    //init navigation live data
    private val _navigateToBersepeda = MutableLiveData<Boolean>()
    val navigateToBersepeda: LiveData<Boolean>
        get() = _navigateToBersepeda

    private val _navigateToKonfigurasi = MutableLiveData<Boolean>()
    val navigateToKonfigurasi: LiveData<Boolean>
        get() = _navigateToKonfigurasi

    private val _showWarning = MutableLiveData<Boolean>()
    val showWarning : LiveData<Boolean>
        get() = _showWarning

    val startButtonEnabled = Transformations.map(navigateToBersepeda){
        true == it
    }

    init {
        Log.d("TRACKING","Location Settings = ${isLocationSettingsEnabled}")
        Log.d("TRACKING","Power Saver Mode  = ${isPowerSaverModeOn}")
        Log.d("TRACKING","Battery Optimized = ${isBatteryOptimized}")
    }

    fun onBtnKonfigurasiClicked(){
        _navigateToKonfigurasi.value = true
    }

    fun doneNavigateToKonfigurasi(){
        _navigateToKonfigurasi.value = null
    }

    fun onBtnMulaiClicked(){
        if(isLocationSettingsEnabled && !isBatteryOptimized && !isPowerSaverModeOn){
            _navigateToBersepeda.value = true
        }else{
            _showWarning.value = true
        }
    }

    fun doneNavigateToBersepeda(){
        _navigateToBersepeda.value = null
    }

    fun doneShowWarning(){
        _showWarning.value = null
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