package com.extra.cyclyx.ui.hasilBersepeda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.database.BersepedaDao

class HasilBersepedaViewModelFactory (
    private val actKey : Long,
    private val dataSource: BersepedaDao
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HasilBersepedaViewModel::class.java)) {
            return HasilBersepedaViewModel(actKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}