package com.extra.cyclyx.ui.selesaiSepeda

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SelesaiBersepedaViewModel(actId: Long = 0L, val app : Application) : AndroidViewModel(app){
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val act = repository.getCyclingData(actId)

    private val _goToHome = MutableLiveData<Boolean>()
    val goToHome: LiveData<Boolean>
        get() = _goToHome

    private val _goToHasil = MutableLiveData<Bersepeda>()
    val goToHasil: LiveData<Bersepeda>
        get() = _goToHasil

    fun onBtnHomeClick(){
        _goToHome.value = true
    }

    fun onBtnHasilClick(){
        _goToHasil.value = act.value
    }

    fun onActLoaded(act : Bersepeda){
        Log.d("TRACKING","Act Loaded")
        modifyUserCyclingData(act)
    }

    //adding save data to shared preferences
    private fun modifyUserCyclingData(act : Bersepeda){
        val editor = repository.sharedPreferences.edit()

        //sum total distance
        val existingDistance = repository.sharedPreferences.getDouble(USER_TOTAL_DISTANCE, 0.0)
        val thisActDistance = act.distance
        val newDistance = existingDistance + thisActDistance
        //put new total in SP
        editor.putDouble(USER_TOTAL_DISTANCE,newDistance)

        //sum total duration
        val existingDuration = repository.sharedPreferences.getLong(USER_TOTAL_DURATION, 0L)
        val thisActDuration = act.endTime - act.startTime
        val newDuration = existingDuration + thisActDuration
        //put new total in SP
        editor.putLong(USER_TOTAL_DURATION,newDuration)

        //compare max speed
        val existingPeakSpeed = repository.sharedPreferences.getDouble(USER_MAX_PEAK_SPEED,0.0)
        val thisActPeakSpeed = act.peakSpeed
        val newPeakSpeed = if(thisActPeakSpeed > existingPeakSpeed){
            thisActPeakSpeed
        }else{
            existingPeakSpeed
        }
        //put new max speed
        editor.putDouble(USER_MAX_PEAK_SPEED,newPeakSpeed)

        //sum total distance
        val existingCalories = repository.sharedPreferences.getDouble(USER_TOTAL_CALORIES, 0.0)
        val thisActCalories = act.calories
        val newCalories = existingCalories + thisActCalories
        //put new total in SP
        editor.putDouble(USER_TOTAL_CALORIES,newCalories)


        editor.apply()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val actId : Long,val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(SelesaiBersepedaViewModel::class.java)){
                return SelesaiBersepedaViewModel(
                    actId,
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}