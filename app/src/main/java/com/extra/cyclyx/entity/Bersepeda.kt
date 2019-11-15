package com.extra.cyclyx.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mapbox.mapboxsdk.geometry.LatLng

import java.sql.Time
import java.util.*

@Entity(tableName = "bersepeda")
data class Bersepeda(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id_sesi") var id: Int,
    @ColumnInfo(name = "titik_mulai") var titikMulai: LatLng,
    @ColumnInfo(name = "titik_berhenti") var titikBerhenti: LatLng,
    @ColumnInfo(name = "waktu_mulai") var waktuMulai: Long,
    @ColumnInfo(name = "waktu_selesai") var waktuSelesai: Long,
    @ColumnInfo(name = "kecepatan") var kecepatan: Double,
    @ColumnInfo(name = "jarak") var jarak: Double,
    @ColumnInfo(name = "kalori") var kalori: Double
)