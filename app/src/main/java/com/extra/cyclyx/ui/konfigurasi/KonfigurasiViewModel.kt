package com.extra.cyclyx.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import kotlinx.coroutines.Job


class KonfigurasiViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    //init permission related
    var isPowerSaverModeOn : Boolean = repository.checkPowerSaverMode()
    var isBatteryOptimized: Boolean = repository.checkBatteryOptimization()
    var isLocationSettingsEnabled : Boolean = repository.checkLocationSettings(app.applicationContext)

    private val _intentForLocation = MutableLiveData<Boolean>()
    val intentForLocation : LiveData<Boolean>
        get() = _intentForLocation

    private val _intentForBatteryOptimization = MutableLiveData<Boolean>()
    val intentForBatteryOptimization : LiveData<Boolean>
        get() = _intentForBatteryOptimization

    private val _intentForPowerSaver = MutableLiveData<Boolean>()
    val intentForPowerSaver : LiveData<Boolean>
        get() = _intentForPowerSaver

    fun onBtnLocationClicked(){
        _intentForLocation.value = true
    }

    fun onBtnBatteryOptimizationClicked(){
        _intentForBatteryOptimization.value = true
    }

    fun onBtnPowerSaverClicked(){
        _intentForPowerSaver.value = true
    }

    fun doneIntentLocation(){
        _intentForLocation.value = null
    }

    fun doneIntentBatteryOptimization(){
        _intentForBatteryOptimization.value = null
    }

    fun doneIntentPowerSaver(){
        _intentForPowerSaver.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(KonfigurasiViewModel::class.java)){
                return KonfigurasiViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}