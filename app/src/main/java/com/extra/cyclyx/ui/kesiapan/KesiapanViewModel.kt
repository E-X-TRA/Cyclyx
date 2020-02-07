package com.extra.cyclyx.ui.kesiapan

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.entity.ReferenceItem
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.FIREBASE_CONSTANTS
import com.extra.cyclyx.utils.RandomDataGenerator
import com.extra.cyclyx.utils.WARNING_TYPES.IS_REFRESHING
import com.extra.cyclyx.utils.WARNING_TYPES.NOT_ELIGIBLE_BERSEPEDA
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.Job


class KesiapanViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    //init permission related

    private val _isPowerSaverModeOn = MutableLiveData<Boolean>()
    val isPowerSaverModeOn : LiveData<Boolean>
        get() = _isPowerSaverModeOn

    private val _isBatteryOptimized = MutableLiveData<Boolean>()
    val isBatteryOptimized : LiveData<Boolean>
        get() = _isBatteryOptimized

    private val _isLocationSettingsEnabled = MutableLiveData<Boolean>()
    val isLocationSettingsEnabled : LiveData<Boolean>
        get() = _isLocationSettingsEnabled


    //init navigation live data
    private val _navigateToBersepeda = MutableLiveData<Boolean>()
    val navigateToBersepeda: LiveData<Boolean>
        get() = _navigateToBersepeda

    private val _navigateToKonfigurasi = MutableLiveData<Boolean>()
    val navigateToKonfigurasi: LiveData<Boolean>
        get() = _navigateToKonfigurasi

    private val _showWarning = MutableLiveData<Int>()
    val showWarning : LiveData<Int>
        get() = _showWarning

    val startButtonEnabled = Transformations.map(navigateToBersepeda){
        true == it
    }

    val motivasiLiveData : LiveData<DataSnapshot> = repository.getLiveDataByType(FIREBASE_CONSTANTS.MOTIVASI_ITEM)
    private val _motivasiList = MutableLiveData<List<ReferenceItem>>()
    val motivasiItem = Transformations.map(_motivasiList){
        it?.let{
            if (it.size != 0) {
                it[RandomDataGenerator.getRandomListItem(it.size)]
            } else {
                ReferenceItem(content = "Tidak Ada Tips Untuk Ditampilkan")
            }
        }
    }

    private fun initializePrerequisite(){
        _isPowerSaverModeOn.value = repository.checkPowerSaverMode()
        _isBatteryOptimized.value = repository.checkBatteryOptimization()
        _isLocationSettingsEnabled.value = repository.checkLocationSettings(app.applicationContext)
        _showWarning.value = IS_REFRESHING
    }

    init {
        initializePrerequisite()
        Log.d("TRACKING","Location Settings = ${isLocationSettingsEnabled}")
        Log.d("TRACKING","Power Saver Mode  = ${isPowerSaverModeOn}")
        Log.d("TRACKING","Battery Optimized = ${isBatteryOptimized}")
    }

    fun onRefresh(){
        initializePrerequisite()
    }

    fun addItemsToList(item : ArrayList<ReferenceItem>){
        _motivasiList.value = item
    }

    fun onBtnKonfigurasiClicked(){
        _navigateToKonfigurasi.value = true
    }

    fun doneNavigateToKonfigurasi(){
        _navigateToKonfigurasi.value = null
    }

    fun onBtnMulaiClicked(){
        if(isLocationSettingsEnabled.value!! && !isBatteryOptimized.value!! && !isPowerSaverModeOn.value!!){
            _navigateToBersepeda.value = true
        }else{
            _showWarning.value = NOT_ELIGIBLE_BERSEPEDA
        }
    }

    fun doneNavigateToBersepeda(){
        _navigateToBersepeda.value = null
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