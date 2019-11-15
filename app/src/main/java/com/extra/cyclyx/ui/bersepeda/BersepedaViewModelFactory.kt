package com.extra.cyclyx.ui.bersepeda

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.database.BersepedaDao

class BersepedaViewModelFactory(
    private val dataSource: BersepedaDao,
    private val app : Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(BersepedaViewModel::class.java)){
            return BersepedaViewModel(dataSource,app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}