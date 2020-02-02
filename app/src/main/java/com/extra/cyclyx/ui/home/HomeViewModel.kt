package com.extra.cyclyx.ui.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.extra.cyclyx.entity.ReferenceItem
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.FIREBASE_CONSTANTS
import com.extra.cyclyx.utils.RandomDataGenerator
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class HomeViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    //init permission related
    var isLocationPermissionGranted: Boolean = false

    private val _finishedTantanganCount = repository.finishedCount
    private val _unfinisehdTantanganCount = repository.unfinishedCount

    val finishedCount = Transformations.map(_finishedTantanganCount){
        it?.let {
            "$it"
        }
    }
    val unfinishedCount = Transformations.map(_unfinisehdTantanganCount){
        it?.let {
            "$it"
        }
    }

    val tipsLiveData : LiveData<DataSnapshot> = repository.getLiveDataByType(FIREBASE_CONSTANTS.TIPS_ITEM)
    private val _tipsList = MutableLiveData<List<ReferenceItem>>()
    val tipsItem = Transformations.map(_tipsList){
        it?.let{
            if (it.size != 0) {
                it[RandomDataGenerator.getRandomListItem(it.size)]
            } else {
                ReferenceItem(content = "Tidak Ada Tips Untuk Ditampilkan")
            }
        }
    }
    
    private val _navigateToKesiapan = MutableLiveData<Boolean>()
    val navigateToKesiapan : LiveData<Boolean>
        get() = _navigateToKesiapan

    private val _navigateToPengaturan = MutableLiveData<Boolean>()
    val navigateToPengaturan : LiveData<Boolean>
        get() = _navigateToPengaturan

    fun addItemsToList(item : ArrayList<ReferenceItem>){
        _tipsList.value = item
    }

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