package com.extra.cyclyx.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModelFactory (val app : Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}