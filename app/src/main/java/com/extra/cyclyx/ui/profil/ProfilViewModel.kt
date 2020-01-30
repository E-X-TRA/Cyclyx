package com.extra.cyclyx.ui.profil

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


class ProfilViewModel(app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _profil = MutableLiveData<Profil>()
    val profil: LiveData<Profil>
        get() = _profil

    val unfinishedTantanganCount = repository.unfinishedCount

    private val _totalUserDistance = MutableLiveData<Double>()
    val totalUserDistance : LiveData<Double>
        get() = _totalUserDistance

    val displayUserDistance = Transformations.map(totalUserDistance){
        it?.let{
            formatDouble(it,"#.#")
        }
    }

    private val _totalUserUsage = MutableLiveData<Long>()
    val totalUserUsage : LiveData<Long>
        get() = _totalUserUsage

    val displayUserUsage = Transformations.map(totalUserUsage){
        it?.let{
            convertDurationToShortestString(it)
        }
    }

    val displayUserTantangan = Transformations.map(unfinishedTantanganCount){
        it?.let {
            "$it"
        }
    }

    private val tempProfil = Profil(
        repository.sharedPreferences.getString(USER_FIRST_NAME, "Sample"),
        repository.sharedPreferences.getString(USER_LAST_NAME, "User"),
        "L",
        repository.sharedPreferences.getInt(USER_BIRTHYEAR, 1999),
        repository.sharedPreferences.getInt(USER_HEIGHT, 50),
        repository.sharedPreferences.getInt(USER_WEIGHT, 170)
    )

    fun convertDateStringtoAge(stringDate: Int): String {
        //parse string to date
        return calculateAge(stringDate, Calendar.getInstance().get(Calendar.YEAR))
    }

    private fun calculateAge(birth: Int, now: Int): String {
        val age = now - birth

        Log.d("PROFIL", "age")
        return "$age"
    }

    init {
        _profil.value = tempProfil
        _totalUserDistance.value = repository.sharedPreferences.getDouble(USER_TOTAL_DISTANCE,0.0)
        _totalUserUsage.value = repository.sharedPreferences.getLong(USER_TOTAL_DURATION,0L)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
                return ProfilViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }

    data class Profil(
        val firstName: String?,
        val lastName: String?,
        val gender: String?,
        val birthDay: Int?,
        val height: Int?,
        val weight: Int?
    )
}