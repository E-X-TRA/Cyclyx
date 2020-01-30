package com.extra.cyclyx.ui.hasilBersepeda

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HasilBersepedaViewModel(
    actId: Long = 0L,
    app: Application
) : AndroidViewModel(app){
    val repository = CyclyxRepository(app.applicationContext)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val act: LiveData<Bersepeda>

    private val _routeList = MutableLiveData<List<Point>>()
    val routeList: LiveData<List<Point>>
        get() = _routeList

    private val _backToMenu = MutableLiveData<Boolean>()
    val backToMenu: LiveData<Boolean>
        get() = _backToMenu

    val mapStyle = repository.settingsPreferences.getString("tipe_peta","Default")

    init {
        Log.d("MAP","Style : $mapStyle")
        act = repository.getCyclingData(actId)
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

    fun decodeRoute(act: Bersepeda?) {
        uiScope.launch {
            act?.let {
                Log.d("RESULT","${act.routeString}")
                val decodedRoute = PolylineUtils.decode(act.routeString, 5)
                _routeList.value = PolylineUtils.simplify(decodedRoute,0.00005)
            }
        }
    }

    fun onMapAsyncFinished() {
        decodeRoute(act.value)
        Log.d("RESULT","MapAync : ${act.value}")
    }

    fun onBackClicked() {
        _backToMenu.value = true
    }

    fun doneNavigating() {
        _backToMenu.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val actId : Long,val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(HasilBersepedaViewModel::class.java)){
                return HasilBersepedaViewModel(
                    actId,
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}