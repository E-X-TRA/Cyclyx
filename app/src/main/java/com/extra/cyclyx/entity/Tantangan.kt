package com.extra.cyclyx.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tantangan")
data class Tantangan(
    @PrimaryKey @ColumnInfo(name = "id_tantangan") var id: Int,
    @ColumnInfo(name = "tipe_tantangan") var tipeTantangan: String,
    @ColumnInfo(name = "label_tantangan") var labelTantangan: String,
    @ColumnInfo(name = "progress_tantangan") var progressTantangan : Int
)