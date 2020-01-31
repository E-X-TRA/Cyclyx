package com.extra.cyclyx.ui.statistik.tantangan

import android.app.Application
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TantanganViewModel(app : Application) :
    AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val challenges = repository.allChallengeData

    val arrayDescription = arrayOf(
        "bersepeda sejauh",
        "bersepeda selama",
        "bersepeda paling cepat",
        "membakar kalori"
    )

    private val _randomDescription = MutableLiveData<String>()
    val randomDescription : LiveData<String>
        get() = _randomDescription

    private val _randomUserRecords = MutableLiveData<String>()
    val randomUserRecords : LiveData<String>
        get() = _randomUserRecords

    init {
        val random = (0..3).random()
        _randomUserRecords.value = when(random){
            0 -> {
                formatDouble(repository.sharedPreferences.getDouble(USER_TOTAL_DISTANCE,0.0),"#.#") +
                " km"
            }
            1 -> {
                val durationShort =  convertDurationToShortestString(repository.sharedPreferences.getLong(
                    USER_TOTAL_DURATION,0L))
                "${durationShort[0]} ${durationShort[1]}"
            }
            2 -> {
                formatDouble(repository.sharedPreferences.getDouble(USER_MAX_PEAK_SPEED,0.0),"#.#") +
                " km/h"
            }
            3 -> {
                formatDouble(repository.sharedPreferences.getDouble(USER_TOTAL_CALORIES,0.0),"#.#") + " kcal"
            }
            else ->{
                "ERR"
            }
        }
        _randomDescription.value = arrayDescription[random]
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(TantanganViewModel::class.java)){
                return TantanganViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}