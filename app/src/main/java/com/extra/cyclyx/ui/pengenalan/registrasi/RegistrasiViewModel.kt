package com.extra.cyclyx.ui.pengenalan.registrasi

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.database.seeder.TantanganSeeder
import com.extra.cyclyx.repository.CyclyxRepository
import com.extra.cyclyx.utils.SP_CYCLYX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegistrasiViewModel(val app: Application) : AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val sharedPreferences = app.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

    init {

    }

    private fun saveNewUserData(){
        //simpan user data ke shared preferences

    }

    private fun seedTantanganData(){
        val tantanganSeeder = TantanganSeeder(repository)

        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            tantanganSeeder.seedTantanganData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(RegistrasiViewModel::class.java)){
                return RegistrasiViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}