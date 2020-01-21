package com.extra.cyclyx.database.seeder

import android.util.Log
import com.extra.cyclyx.entity.Tantangan
import com.extra.cyclyx.repository.CyclyxRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader


class TantanganSeeder(val repo : CyclyxRepository){
    companion object{
        val FILE_JSON = "/TantanganJSON.cyx"
    }

    private lateinit var tantanganList : ArrayList<Tantangan>

    init {
        try {
            val inputStream =
                TantanganSeeder::class.java.getResourceAsStream(FILE_JSON)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            this.readJSONFile(bufferedReader)
        } catch (e: Exception) {
            Log.d("TRACKING", "TANTANGANJSON READ!")
        }
    }

    suspend fun seedTantanganData(){
            Log.d("SEEDING",".:: JSON -> $tantanganList ::.")
            for(tantangan in tantanganList){
                repo.insertTantanganData(tantangan)
            }
    }



    private fun readJSONFile(br : BufferedReader){
        val inputString = br.use { it.readText() }

        tantanganList = Gson().fromJson(inputString,object:
            TypeToken<List<Tantangan>>(){}.type)
    }
}