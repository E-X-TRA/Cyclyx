package com.extra.cyclyx.ui.bersepeda

import android.app.Application
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.extra.cyclyx.database.BersepedaDao
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.utils.TRACKING_PAUSED
import com.extra.cyclyx.utils.TRACKING_RESUMED
import com.extra.cyclyx.utils.TRACKING_STARTED
import com.extra.cyclyx.utils.TRACKING_STOPPED
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BersepedaViewModel(
    dataSource : BersepedaDao,
    application : Application
) : AndroidViewModel(application){
    val database = dataSource
    //coroutine
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var thisAct = MutableLiveData<Bersepeda?>()
    private var _locationPoints = MutableLiveData<List<Point>>()
    val locationPoints: LiveData<List<Point>>
        get() = _locationPoints
    private var _elapsedTime = MutableLiveData<Long>()
    val elapsedTime : LiveData<Long>
        get() =  _elapsedTime
    private var startTime = MutableLiveData<Long>()
    private var endTime = MutableLiveData<Long>()
    private var _trackingStatus = MutableLiveData<String>()
    val trackingStatus: LiveData<String>
        get() = _trackingStatus
    private var _pauseOffset = MutableLiveData<Long>()

    var handler : Handler?=null

    init {
        handler = Handler()
        _trackingStatus.value = TRACKING_STARTED
    }

    //initialize this cycling act that being tracked
    private fun initializeCyclingAct() {
        uiScope.launch {
            thisAct.value = database.getLatestCycling()
        }
    }

    //decodePolylineString from fragments
    fun decodePolyLine(route: String) {
        uiScope.launch {
            val tempListLatLng = PolylineUtils.decode(route, 5)
            _locationPoints.value = tempListLatLng
        }
    }

    //timer related
    fun startTimer() {
        uiScope.launch {
            Log.d("TRACKING","Timer Started")
            startTime.value = System.currentTimeMillis() //set starttime value
            _trackingStatus.value = TRACKING_STARTED

            _pauseOffset.value = SystemClock.uptimeMillis()
            handler?.postDelayed(runnable,0)
        }
    }

    fun pauseTimer(){
        uiScope.launch {
            Log.d("TRACKING","Timer Paused")
            _trackingStatus.value = TRACKING_PAUSED

            handler?.removeCallbacks(runnable)
            _pauseOffset.value = SystemClock.uptimeMillis() - _elapsedTime.value!!
        }
    }

    fun resumeTimer(){
        uiScope.launch {
            Log.d("TRACKING","Timer Resumed")
            _trackingStatus.value = TRACKING_RESUMED

            handler?.postDelayed(runnable,0)
        }
    }

    fun stopTimer(){
        uiScope.launch {
            _trackingStatus.value = TRACKING_STOPPED

            handler?.removeCallbacks(runnable)
            endTime.value = System.currentTimeMillis() //set endtime value
        }
    }

    var runnable : Runnable = object : Runnable{
        override fun run() {
            _elapsedTime.value = SystemClock.uptimeMillis() - _pauseOffset.value!!
            handler?.postDelayed(this,0)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}