package com.extra.cyclyx.database.converter

import androidx.room.TypeConverter
import com.mapbox.geojson.Point

class LocationConverter {
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromPoint(value : Point) : String{
            val latString = value.latitude().toString()
            val lngString = value.longitude().toString()
            val altString = value.altitude().toString()
            val stringPoint = "${latString},${lngString},${altString}"
            return stringPoint
        }

        @TypeConverter
        @JvmStatic
        fun toPoint(value : String) : Point{
            val location = value.split(",")
            val latitude = location[0].toDouble()
            val longitude = location[1].toDouble()
            val altitude = location[2].toDouble()
            return Point.fromLngLat(longitude, latitude, altitude)
        }
    }
}