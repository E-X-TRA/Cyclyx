package com.extra.cyclyx.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.extra.cyclyx.database.AppDatabase
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.entity.Tantangan
import com.extra.cyclyx.utils.SP_CYCLYX
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CyclyxRepository(context: Context){
    //init
    private val database = AppDatabase.getInstance(context)
    val sharedPreferences = context.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)
    val firebaseDB = FirebaseDatabase.getInstance()
    val firebaseReference : DatabaseReference = firebaseDB.reference

    //cycling related
    val allCyclingData : LiveData<List<Bersepeda>> = database.bersepedaDAO.getAll()

    val recentsCyclingData : LiveData<List<Bersepeda>> = database.bersepedaDAO.getRecentsCycling()

    fun getLatestCyclingData() = database.bersepedaDAO.getLatestCycling()

    fun getCyclingData(id : Long) : LiveData<Bersepeda> = database.bersepedaDAO.getCyclingAct(id)

    suspend fun removeCyclingData(id : Long){
        withContext(Dispatchers.IO){
            database.bersepedaDAO.deleteCyclingAct(id)
        }
    }

    suspend fun resetCyclingData(){
        withContext(Dispatchers.IO){
            database.bersepedaDAO.deleteAllActs()
        }
    }

    suspend fun insertCyclingData(data : Bersepeda){
        withContext(Dispatchers.IO){
            database.bersepedaDAO.insertCyclingAct(data)
        }
    }

    suspend fun updateCyclingData(data : Bersepeda){
        withContext(Dispatchers.IO){
            database.bersepedaDAO.updateCyclingAct(data)
        }
    }

    //challenge related
    val allChallengeData : LiveData<List<Tantangan>> = database.tantanganDAO.getAllTantangan()



    suspend fun insertTantanganData(data : Tantangan){
        withContext(Dispatchers.IO){
            database.tantanganDAO.insertTantangan(data)
            Log.d("DB","Inserted -> $data")
        }
    }
}