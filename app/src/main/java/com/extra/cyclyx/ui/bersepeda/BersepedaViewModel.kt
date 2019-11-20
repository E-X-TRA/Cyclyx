package com.extra.cyclyx.ui.bersepeda

import android.app.Application
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.extra.cyclyx.database.BersepedaDao
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.utils.*
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.*

class BersepedaViewModel(
    dataSource : BersepedaDao,
    val app : Application
) : AndroidViewModel(app){
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

    private val _speed = MutableLiveData<Double>()
    val speed: LiveData<Double>
        get() = _speed
    private val _totalDistance = MutableLiveData<Double>()
    val totalDistance: LiveData<Double>
        get() = _totalDistance
    private val _calories = MutableLiveData<Double>()
    val calories: LiveData<Double>
        get() = _calories

    private var _peakSpeed = 0.0
    private var _elevationLoss = 0.0
    private var _elevationGain = 0.0
    private var thisActRoute = ""
    private var timeLog = ArrayList<Long>()

    private val _navigateToResult = MutableLiveData<Bersepeda>()
    val navigateToResult: LiveData<Bersepeda>
        get() = _navigateToResult

    fun doneNavigatingToResult() {
        _navigateToResult.value = null
    }

    var handler : Handler?=null
    val elevationHelper = ElevationHelper(app.applicationContext)

    init {
        handler = Handler()
        _trackingStatus.value = TRACKING_STARTED

        initMapData()
        initializeBersepeda()
    }

    private fun initMapData() {
        _totalDistance.value = 0.0
        _speed.value = 0.0
        _calories.value = 0.0
    }

    private fun initializeBersepeda() {
        uiScope.launch {
            thisAct.value = getLatestBersepeda()
        }
    }

    private suspend fun getLatestBersepeda(): Bersepeda? {
        return withContext(Dispatchers.IO) {
            var cycling = database.getLatestCycling()
            if (cycling?.endTime != cycling?.startTime) {
                cycling = null
            }
            cycling
        }
    }

    private suspend fun insert(cycling: Bersepeda) {
        withContext(Dispatchers.IO) {
            database.insert(cycling)
        }
    }

    private suspend fun update(cycling: Bersepeda) {
        withContext(Dispatchers.IO) {
            database.update(cycling)
        }
    }

    fun onStart() {
        uiScope.launch {
            startTimer()
            val newAct = Bersepeda()
            insert(newAct)
            thisAct.value = getLatestBersepeda()
        }
    }

    fun onPause() {
        uiScope.launch {
            pauseTimer()
        }
    }

    fun onResume() {
        uiScope.launch {
            resumeTimer()
        }
    }

    fun onStop() {
        uiScope.launch {
            stopTimer()
            val oldAct = thisAct.value ?: return@launch

            oldAct.endTime = System.currentTimeMillis()
            oldAct.startPoint = _locationPoints.value?.first() ?: Point.fromLngLat(107.628550, -6.941557)
            oldAct.endPoint = _locationPoints.value?.last() ?: oldAct.startPoint
            oldAct.speed = _speed.value!!
            oldAct.distance = _totalDistance.value!!
            oldAct.calories = _calories.value!!
            oldAct.peakSpeed = _peakSpeed
            oldAct.elevationGain = _elevationGain
            oldAct.elevationLoss = _elevationLoss
            oldAct.calories = _calories.value!!
            oldAct.routeString = thisActRoute
            oldAct.finished = true
            Log.d("TRACKING",oldAct.toString())
            update(oldAct)

            thisAct.value = oldAct

            _navigateToResult.value = oldAct
        }
    }

    //decodePolylineString from fragments
    fun decodePolyLine(route: String) {
        uiScope.launch {
            thisActRoute = route
            timeLog.add(System.currentTimeMillis())
            val tempListLatLng = PolylineUtils.decode(route, 5)
            _locationPoints.value = tempListLatLng

            viewModelScope.launch {
                processMapsData()
                Log.d("TRACKING","Speed : ${_speed.value}")
            }
        }
    }

    private fun processMapsData() {
        val pointsList = _locationPoints.value
        pointsList?.let {
            var distanceBetweenLastTwoPoints = 0.0
            //calculation
            if (pointsList.size >= 2) {
                val oldPoint = pointsList.get(pointsList.size - 2) //one request before this location data
                val newPoint = pointsList.get(pointsList.size - 1) //this request location data
                //calculate distance
                distanceBetweenLastTwoPoints = TurfMeasurement.distance(oldPoint, newPoint)
                //calculate speed (for peak/max speed) in km/h
                val duration = timeLog.get(timeLog.size - 1) - timeLog.get(timeLog.size - 2)
                val speed = distanceBetweenLastTwoPoints / duration
                if (_peakSpeed < speed) {
                    _peakSpeed = speed
                }
                //elevation loss and gain
                val oldPointAlt = oldPoint.altitude() - elevationHelper.getOffset(oldPoint)
                val newPointAlt = newPoint.altitude() - elevationHelper.getOffset(newPoint)
                when {
                    oldPointAlt < newPointAlt -> { //means elevation gain
                        val elevationChange = newPointAlt - oldPointAlt
                        _elevationGain += elevationChange
                    }
                    oldPointAlt > newPointAlt -> { //means elevation loss
                        val elevationChange = oldPointAlt - newPointAlt
                        _elevationLoss += elevationChange
                    }
                    else -> {
                    }
                }
            }
            //summing things when moving for total sum and update UI
            if (pointsList.size >= 2 && distanceBetweenLastTwoPoints > 0) {
                //duration until this point
                val durationUntilThis = convertLongToHour(timeLog.last() - timeLog.first())
                //total distance this point
                _totalDistance.value = _totalDistance.value?.plus(distanceBetweenLastTwoPoints)
                //average speed until this point
                val averageSpeed = _totalDistance.value?.div(durationUntilThis)
                _speed.value = averageSpeed
                //calories burned until this point
                // using https://captaincalculator.com/health/calorie/calories-burned-cycling-calculator/
                val weightInKg = 50
                val mets = determineMets(averageSpeed!!)
                val caloriesCalc = ((mets * weightInKg * 3.5) / 200) * (durationUntilThis / 60)
                _calories.value = caloriesCalc
            }
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