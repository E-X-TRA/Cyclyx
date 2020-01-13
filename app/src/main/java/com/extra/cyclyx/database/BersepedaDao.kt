package com.extra.cyclyx.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.extra.cyclyx.entity.Bersepeda

@Dao
interface BersepedaDao {
    @Query("SELECT * FROM bersepeda")
    fun getAll() : LiveData<List<Bersepeda>>

    @Query("SELECT * FROM bersepeda ORDER BY id DESC LIMIT 1")
    fun getLatestCycling() : Bersepeda?

    @Query("SELECT * FROM bersepeda ORDER BY id DESC LIMIT 2")
    fun getRecentsCycling() : LiveData<List<Bersepeda>>

    @Query("SELECT * FROM bersepeda WHERE id = :key")
    fun getCyclingAct(key :Long): LiveData<Bersepeda>

    @Insert(onConflict = REPLACE)
    fun insertCyclingAct(bersepeda : Bersepeda)

    @Update
    fun updateCyclingAct(bersepeda : Bersepeda)

    //deleting entry
    @Query("DELETE FROM bersepeda WHERE id = :key")
    fun deleteCyclingAct(key : Long)

    @Query("DELETE FROM bersepeda")
    fun deleteAllActs()
}