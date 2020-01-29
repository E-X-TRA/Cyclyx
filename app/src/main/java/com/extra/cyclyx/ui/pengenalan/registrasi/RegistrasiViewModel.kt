package com.extra.cyclyx.ui.pengenalan.registrasi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import com.extra.cyclyx.database.seeder.TantanganSeeder
import com.extra.cyclyx.entity.User
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.*
import com.extra.cyclyx.utils.GENDER.UNSELECTED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegistrasiViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val sharedPreferences = app.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String>
        get() = _gender

    private val _navigateNext = MutableLiveData<Boolean>()
    val navigateNext: LiveData<Boolean>
        get() = _navigateNext

    init {
        _gender.value = UNSELECTED
    }

    fun onSaveUserData(model: User) {
        //simpan user data ke shared preferences
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putString(USER_FIRST_NAME, model.firstName)
        editor.putString(USER_LAST_NAME, model.lastName)
        editor.putInt(USER_BIRTHYEAR, model.birthYear)
        editor.putInt(USER_WEIGHT, model.weight)
        editor.putInt(USER_HEIGHT, model.height)


        editor.apply()
    }

    fun onClickGenderButton(value: String) {
        if (value == _gender.value) {
            _gender.value = UNSELECTED
        } else {
            _gender.value = value
        }
    }

    fun onSaveGenderUserData() {
        if (gender.value != UNSELECTED) {
            //simpan gender data ke shared preferences
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString(USER_GENDER, gender.value)
            editor.putString(USER_TOKEN, RandomDataGenerator.getRandomString(30))

            editor.apply()
            this.seedTantanganData()
            _navigateNext.value = true
        }
    }

    private fun seedTantanganData() {
        val tantanganSeeder = TantanganSeeder(repository)

        uiScope.launch {
            tantanganSeeder.seedTantanganData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if (modelClass.isAssignableFrom(RegistrasiViewModel::class.java)) {
                return RegistrasiViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}