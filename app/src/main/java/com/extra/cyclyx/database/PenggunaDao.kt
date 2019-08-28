package com.extra.cyclyx.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.extra.cyclyx.entity.Pengguna

@Dao
interface PenggunaDao {
    @Query("SELECT * FROM Pengguna")
    fun getAll() : List<Pengguna>

    @Insert(onConflict = REPLACE)
    fun insertAll(pengguna : Pengguna)

    @Update
    fun updateAll(pengguna: Pengguna)

    @Delete
    fun deleteAll(pengguna: Pengguna)
}