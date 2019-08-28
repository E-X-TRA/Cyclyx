package com.extra.cyclyx.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.extra.cyclyx.entity.Bersepeda

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