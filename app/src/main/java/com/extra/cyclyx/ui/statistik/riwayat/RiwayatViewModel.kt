package com.extra.cyclyx.ui.statistik.riwayat

import android.app.Application
import androidx.lifecycle.*
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.repository.CyclyxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RiwayatViewModel(app: Application) :
    AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToResult = MutableLiveData<Long>()
    val navigateToResult: LiveData<Long>
        get() = _navigateToResult

    val acts : LiveData<List<Bersepeda>> = repository.allCyclingData

    val actCounts = Transformations.map(acts){actList ->
        actList?.let {
            "Tercatat ${it.size} kali anda melacak..."
        }
    }

    fun onActClicked(id: Long) {
        _navigateToResult.value = id
    }

    fun onDeleteClicked(id: Long) {
        uiScope.launch {
            deleteAct(id)
        }
    }

    private suspend fun deleteAct(id: Long) {
        repository.removeCyclingData(id)
    }

    fun doneNavigateToHasilBersepeda() {
        _navigateToResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(RiwayatViewModel::class.java)){
                return RiwayatViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }

    }
}