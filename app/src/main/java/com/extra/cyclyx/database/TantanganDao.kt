package com.extra.cyclyx.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extra.cyclyx.entity.Tantangan

@Dao
interface TantanganDao {
    @Query("SELECT * FROM tantangan")
    fun getAllTantangan() : LiveData<List<Tantangan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTantangan(tantangan: Tantangan)
}