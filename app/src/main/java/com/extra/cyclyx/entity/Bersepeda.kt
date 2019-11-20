package com.extra.cyclyx.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapbox.geojson.Point

@Entity(tableName = "bersepeda")
data class Bersepeda(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id : Long = 0L,
    @ColumnInfo(name = "start_point") var startPoint : Point = Point.fromLngLat(107.628550,-6.941557),
    @ColumnInfo(name = "end_point") var endPoint: Point = startPoint,
    @ColumnInfo(name = "start_time") val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time") var endTime: Long = startTime,
    @ColumnInfo(name = "distance") var distance : Double = 0.0,
    @ColumnInfo(name = "speed") var speed: Double = 0.0,
    @ColumnInfo(name = "calories") var calories: Double = 0.0,
    @ColumnInfo(name = "peak_speed") var peakSpeed : Double = 0.0,
    @ColumnInfo(name = "elevation_gain") var elevationGain : Double = 0.0,
    @ColumnInfo(name = "elevation_loss") var elevationLoss : Double = 0.0,
    @ColumnInfo(name = "route_string") var routeString : String = "",
    @ColumnInfo(name = "finished") var finished : Boolean = false
)