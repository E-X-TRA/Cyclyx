package com.extra.cyclyx.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tantangan")
data class Tantangan(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_tantangan") var id: Int,
    @SerializedName("tipe_tantangan") @ColumnInfo(name = "tipe_tantangan") var tipeTantangan: String,
    @SerializedName("label_tantangan") @ColumnInfo(name = "label_tantangan") var labelTantangan: String,
    @SerializedName("progress_tantangan") @ColumnInfo(name = "progress_tantangan") var progressTantangan : Int = 0
)