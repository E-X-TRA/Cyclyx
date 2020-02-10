package com.extra.cyclyx.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.extra.cyclyx.entity.Tantangan

@Dao
interface TantanganDao {
    @Query("SELECT * FROM tantangan")
    fun getAllTantangan() : LiveData<List<Tantangan>>

    @Query("SELECT * FROM tantangan WHERE id_tantangan = :id")
    fun getTantangan(id :Long): LiveData<Tantangan>

    @Query("SELECT * FROM tantangan WHERE label_tantangan = :label AND progress_tantangan < 100 LIMIT 1")
    fun getLatestUnfinishedTantanganByLabel(label : String) : Tantangan

    @Query("SELECT * FROM tantangan WHERE progress_tantangan < 100")
    fun getUnfinishedTantangan() : LiveData<List<Tantangan>>

    @Query("SELECT COUNT(*) FROM tantangan")
    fun getAllTantanganCount() : LiveData<Int>

    @Query("SELECT COUNT(*) FROM tantangan WHERE progress_tantangan = 100")
    fun getFinishedTantanganCount() : LiveData<Int>

    @Query("SELECT COUNT(*) FROM tantangan WHERE progress_tantangan < 100")
    fun getUnfinishedTantanganCount() : LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTantangan(tantangan: Tantangan)

    @Query("UPDATE tantangan SET progress_tantangan = :newProgress WHERE id_tantangan = :idTantangan")
    fun updateProgressTantangan(idTantangan : Int,newProgress : Int)

    @Update
    fun updateTantangan(tantangan : Tantangan)
}