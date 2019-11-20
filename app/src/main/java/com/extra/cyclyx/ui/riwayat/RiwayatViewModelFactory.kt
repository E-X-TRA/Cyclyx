package com.extra.cyclyx.ui.riwayat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.database.BersepedaDao

class RiwayatViewModelFactory (
    private val dataSource: BersepedaDao,
    private val app : Application
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RiwayatViewModel::class.java)) {
            return RiwayatViewModel(dataSource,app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}