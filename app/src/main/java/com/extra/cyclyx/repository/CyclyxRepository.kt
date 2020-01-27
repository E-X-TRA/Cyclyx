package com.extra.cyclyx.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.extra.cyclyx.database.AppDatabase
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.entity.Tantangan
import com.extra.cyclyx.utils.SP_CYCLYX
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CyclyxRepository(val context: Context){
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

    fun checkBatteryOptimization() : Boolean{
        val pwrm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = context.packageName
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            !pwrm.isIgnoringBatteryOptimizations(name)
        }else{
            false
        }
    }

    fun checkPowerSaverMode() : Boolean{
        val pwrm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pwrm.isPowerSaveMode
        }else{
            false
        }
    }

    //return true if granted and false if not granted
    fun checkLocationPermission() : Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationSettings(context : Context) : Boolean{
        return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isLocationEnabled
        } else {
            // This is Deprecated in API 28
            @Suppress("DEPRECATION")
            val mode : Int = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }
}