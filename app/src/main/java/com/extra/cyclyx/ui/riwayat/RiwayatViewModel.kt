package com.extra.cyclyx.ui.riwayat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.extra.cyclyx.database.BersepedaDao
import kotlinx.coroutines.*

class RiwayatViewModel(dataSource: BersepedaDao, application: Application) :
    AndroidViewModel(application) {
    val database = dataSource
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToResult = MutableLiveData<Long>()
    val navigateToResult: LiveData<Long>
        get() = _navigateToResult

    val acts = database.getAll()

    fun onActClicked(id: Long) {
        _navigateToResult.value = id
    }

    fun onDeleteClicked(id: Long) {
        uiScope.launch {
            deleteAct(id)
        }
    }

    private suspend fun deleteAct(id: Long) {
        withContext(Dispatchers.IO) {
            database.deleteCyclingAct(id)
        }
    }

    fun doneNavigateToHasilBersepeda() {
        _navigateToResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}