package com.extra.cyclyx.database

import androidx.room.Dao
import androidx.room.Query
import com.extra.cyclyx.entity.Tantangan

@Dao
interface TantanganDao {
    @Query("SELECT * FROM tantangan")
    fun getAllTantangan() : List<Tantangan>
}