package com.extra.cyclyx.ui.hasilBersepeda

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.repository.CyclyxRepository
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

    init {
        act = repository.getCyclingData(actId)
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