package com.extra.cyclyx.ui.statistik.tantangan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.extra.cyclyx.repository.CyclyxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class TantanganViewModel(app : Application) :
    AndroidViewModel(app) {
    val repository = CyclyxRepository(app.applicationContext)

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val challenges = repository.allChallengeData

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}