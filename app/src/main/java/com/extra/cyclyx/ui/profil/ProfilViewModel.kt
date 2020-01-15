package com.extra.cyclyx.ui.profil

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.cyclyxDateFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*


class ProfilViewModel(app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)
    val sharedPreferences = app.getSharedPreferences("CYCLYX_PROFILE", Context.MODE_PRIVATE)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _profil = MutableLiveData<Profil>()
    val profil : LiveData<Profil>
        get() = _profil

    private val tempProfil = Profil(
        "Raihan",
        "Putra",
        "puput",
        "L",
        "31-12-2001",
        172,
    50
    )

    fun convertDateStringtoAge(stringDate : String) : String{
        //parse string to date
        val birthdate = cyclyxDateFormat.parse(stringDate)
        val calendarBirth = Calendar.getInstance()
        calendarBirth.time = birthdate!!
        return calculateAge(calendarBirth, Calendar.getInstance())
    }

    private fun calculateAge(birth : Calendar, now : Calendar) : String{
        var days: Int
        var months: Int
        var years: Int

        val currMonth = now.get(Calendar.MONTH) + 1
        val birthMonth = birth.get(Calendar.MONTH) + 1
        months = currMonth - birthMonth

        years = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if(months < 0){
            years--
            months = 12 - birthMonth + currMonth
            if(now.get(Calendar.DATE) < birth.get(Calendar.DATE)) months--
        }else{
            years--
            months = 11
        }

        if(now.get(Calendar.DATE) > birth.get(Calendar.DATE)){
            days = now.get(Calendar.DATE) - birth.get(Calendar.DATE)
        }else if(now.get(Calendar.DATE) < birth.get(Calendar.DATE)){
            val today = now.get(Calendar.DAY_OF_MONTH)
            now.add(Calendar.MONTH,-1)
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birth.get(Calendar.DAY_OF_MONTH) + today
        }else{
            days = 0
            if(months == 12){
                years ++
                months = 0
            }
        }

        Log.d("PROFIL","$years:$months:$days")
        return "$years"
    }

    init {
        _profil.value = tempProfil
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(ProfilViewModel::class.java)){
                return ProfilViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }

    data class Profil(
        val firstName : String,
        val lastName : String,
        val userName : String,
        val gender : String,
        val birthDay : String,
        val height : Int,
        val weight : Int
    )
}