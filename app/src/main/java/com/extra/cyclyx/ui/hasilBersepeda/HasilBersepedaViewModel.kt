package com.extra.cyclyx.ui.hasilBersepeda

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.extra.cyclyx.database.BersepedaDao
import com.extra.cyclyx.entity.Bersepeda
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HasilBersepedaViewModel(
    actId: Long = 0L,
    dataSource: BersepedaDao
) : ViewModel(){
    val database = dataSource
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val act: LiveData<Bersepeda>

    private val _routeList = MutableLiveData<List<Point>>()
    val routeList: LiveData<List<Point>>
        get() = _routeList

    private val _backToMenu = MutableLiveData<Boolean>()
    val backToMenu: LiveData<Boolean>
        get() = _backToMenu

    init {
        act = database.getCyclingAct(actId)
    }

    fun decodeRoute(act: Bersepeda?) {
        uiScope.launch {
            act?.let {
                Log.d("TRACKING","${act.toString()}")
                val decodedRoute = PolylineUtils.decode(act.routeString, 5)
                _routeList.value = PolylineUtils.simplify(decodedRoute,0.00005)
            }
        }
    }

    fun onMapAsyncFinished() {
        decodeRoute(act.value)
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
}