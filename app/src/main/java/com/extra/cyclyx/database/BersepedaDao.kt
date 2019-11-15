package com.extra.cyclyx.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.extra.cyclyx.entity.Bersepeda

@Dao
interface BersepedaDao {
    @Query("SELECT * FROM bersepeda")
    fun getAll() : List<Bersepeda>

    @Query("SELECT * FROM bersepeda ORDER BY id_sesi DESC LIMIT 1")
    fun getLatestCycling() : Bersepeda

    @Insert(onConflict = REPLACE)
    fun insert(bersepeda : Bersepeda)

    @Delete
    fun delete(bersepeda: Bersepeda)

    @Query("DELETE FROM bersepeda")
    fun deleteAll()
}