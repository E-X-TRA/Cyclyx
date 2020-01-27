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

    private val tempProfil = Profil(
        repository.sharedPreferences.getString(USER_FIRST_NAME, "."),
        repository.sharedPreferences.getString(USER_LAST_NAME, "."),
        "L",
        repository.sharedPreferences.getInt(USER_BIRTHYEAR, 2001),
        repository.sharedPreferences.getInt(USER_HEIGHT, 0),
        repository.sharedPreferences.getInt(USER_WEIGHT, 0)
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