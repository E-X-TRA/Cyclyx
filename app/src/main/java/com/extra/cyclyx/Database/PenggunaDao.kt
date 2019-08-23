package com.extra.cyclyx.Database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.extra.cyclyx.Entity.Pengguna

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