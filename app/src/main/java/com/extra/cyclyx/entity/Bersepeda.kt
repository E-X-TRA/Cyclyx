package com.extra.cyclyx.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

import java.sql.Time
import java.util.*

@Entity(tableName = "Bersepeda")
data class Bersepeda(
    @PrimaryKey @ColumnInfo(name = "id_sesi") var id: Int,
    @ColumnInfo(name = "pengguna") var pengguna: String,
    @ColumnInfo(name = "titik_mulai") var titikMulai: String,
    @ColumnInfo(name = "titik_berhenti") var titikBerhenti: String,
    @ColumnInfo(name = "waktu_bersepeda") var waktuBersepeda: String,
    @ColumnInfo(name = "tanggal_bersepeda") var tanggalBersepeda: String,
    @ColumnInfo(name = "kecepatan") var kecepatan: Double,
    @ColumnInfo(name = "jarak") var jarak: Double,
    @ColumnInfo(name = "kalori") var kalori: Double
)