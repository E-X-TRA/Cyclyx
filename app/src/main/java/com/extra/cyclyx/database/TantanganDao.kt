package com.extra.cyclyx.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.extra.cyclyx.entity.Tantangan

@Dao
interface TantanganDao {
    @Query("SELECT * FROM tantangan")
    fun getAllTantangan() : LiveData<List<Tantangan>>
}