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

    @Query("SELECT * FROM tantangan WHERE id_tantangan = :id")
    fun getTantangan(id :Long): LiveData<Tantangan>

    @Query("SELECT * FROM tantangan WHERE progress_tantangan < 100")
    fun getUnfinishedTantangan() : LiveData<List<Tantangan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTantangan(tantangan: Tantangan)
}