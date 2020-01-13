package com.extra.cyclyx

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.extra.cyclyx.database.seeder.TantanganSeeder
import com.extra.cyclyx.repository.CyclyxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestingSeeder{
    lateinit var context: Context
    lateinit var repository : CyclyxRepository
    @Before
    fun setup(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun writeJSON(){
        repository = CyclyxRepository(context)
        val tantanganSeeder = TantanganSeeder(repository)

        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            tantanganSeeder.seedTantanganData()
        }
    }

    @After
    fun getRepoData(){
        repository = CyclyxRepository(context)
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            Log.d("SEEDING","DATA DB -> ${repository.allChallengeData.value}")
        }
    }
}