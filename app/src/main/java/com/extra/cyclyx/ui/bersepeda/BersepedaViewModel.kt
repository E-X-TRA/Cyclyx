package com.extra.cyclyx.ui.bersepeda

import android.app.Application
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import kotlinx.coroutines.*

class BersepedaViewModel(
    val app : Application
) : AndroidViewModel(app){
    val repository = CyclyxRepository(app.applicationContext)
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
    private val altitudeList = ArrayList<Double>()

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
            var cycling = repository.getLatestCyclingData()
            if (cycling?.endTime != cycling?.startTime) {
                cycling = null
            }
            cycling
        }
    }

    private suspend fun insert(cycling: Bersepeda) {
        repository.insertCyclingData(cycling)
    }

    private suspend fun update(cycling: Bersepeda){
        repository.updateCyclingData(cycling)
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
        Log.d("TRACKING","Stopped!")
        uiScope.launch {
            stopTimer()
            val latestAct = thisAct.value ?: return@launch

            latestAct.endTime = System.currentTimeMillis()
            latestAct.startPoint = _locationPoints.value?.first() ?: Point.fromLngLat(107.628550, -6.941557)
            latestAct.endPoint = _locationPoints.value?.last() ?: latestAct.startPoint
            latestAct.speed = _speed.value!!
            latestAct.distance = _totalDistance.value!!
            latestAct.calories = _calories.value!!
            latestAct.peakSpeed = _peakSpeed
            latestAct.elevationGain = _elevationGain
            latestAct.elevationLoss = _elevationLoss
            latestAct.routeString = thisActRoute
            latestAct.finished = true
            Log.d("CALCULATIONS","$latestAct")

            update(latestAct)
//            modifyUserCyclingData(latestAct)

            thisAct.value = latestAct

            _navigateToResult.value = latestAct
        }
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

    //decodePolylineString from service and process the altitude
    fun processLocationUpdate(route: String,alt : Double) {
        uiScope.launch {
            thisActRoute = route
            timeLog.add(System.currentTimeMillis())
            val tempListLatLng = PolylineUtils.decode(route, 5)
            _locationPoints.value = tempListLatLng
            altitudeList.add(alt)

            viewModelScope.launch {
                processMapsData()
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
                //assuming this was KILOMETRES
                distanceBetweenLastTwoPoints = TurfMeasurement.distance(oldPoint, newPoint,TurfConstants.UNIT_KILOMETRES)
                //calculate speed (for peak/max speed) in km/h
                val duration = timeLog.get(timeLog.size - 1) - timeLog.get(timeLog.size - 2)
//                val speed = distanceBetweenLastTwoPoints / duration //in m/s
                val speed = (distanceBetweenLastTwoPoints/1000) / convertLongToSecond(duration) //in m/s
                if (_peakSpeed < speed) {
                    _peakSpeed = speed
                }
                //elevation loss and gain
                val oldPointAlt = elevationHelper.getOffset(oldPoint)
                val newPointAlt = elevationHelper.getOffset(newPoint)
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
                //assuming this was in HOURS
                val durationUntilThis : Long = timeLog.last() - timeLog.first()
                //total distance this point
                _totalDistance.value = _totalDistance.value?.plus(distanceBetweenLastTwoPoints)
                //average speed until this point
                val averageSpeed = _totalDistance.value?.div(convertLongToHour(durationUntilThis))
                _speed.value = averageSpeed
                //calories burned until this point
                // using https://captaincalculator.com/health/calorie/calories-burned-cycling-calculator/
                val weightInKg = 50
                val mets = determineMets(averageSpeed!!)
                val caloriesCalc = ((mets * weightInKg * 3.5) / 200) * (convertLongToMinute(durationUntilThis))
                _calories.value = caloriesCalc
            }
        }
    }

    //timer related
    private fun startTimer() {
        uiScope.launch {
            Log.d("TRACKING","Timer Started")
            startTime.value = System.currentTimeMillis() //set starttime value
            _trackingStatus.value = TRACKING_STARTED

            _pauseOffset.value = SystemClock.uptimeMillis()
            handler?.postDelayed(runnable,0)
        }
    }

    private fun pauseTimer(){
        uiScope.launch {
            Log.d("TRACKING","Timer Paused")
            _trackingStatus.value = TRACKING_PAUSED

            handler?.removeCallbacks(runnable)
            _pauseOffset.value = SystemClock.uptimeMillis() - _elapsedTime.value!!
        }
    }

    private fun resumeTimer(){
        uiScope.launch {
            Log.d("TRACKING","Timer Resumed")
            _trackingStatus.value = TRACKING_RESUMED

            handler?.postDelayed(runnable,0)
        }
    }

    private fun stopTimer(){
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

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(BersepedaViewModel::class.java)){
                return BersepedaViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}