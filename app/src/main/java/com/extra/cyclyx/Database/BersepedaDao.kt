package com.extra.cyclyx.Database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.extra.cyclyx.Entity.Bersepeda

@Dao
interface BersepedaDao {
    @Query("SELECT * FROM bersepeda")
    fun getAll() : List<Bersepeda>

    @Insert(onConflict = REPLACE)
    fun insertAll(bersepeda : Bersepeda)

    @Update
    fun updateAll(bersepeda: Bersepeda)

    @Delete
    fun deleteAll(bersepeda: Bersepeda)
}