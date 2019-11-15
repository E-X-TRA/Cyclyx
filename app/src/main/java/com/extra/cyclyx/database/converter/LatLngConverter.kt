package com.extra.cyclyx.database.converter

import androidx.room.TypeConverter
import com.mapbox.mapboxsdk.geometry.LatLng

class LatLngConverter{
    companion object{
        @TypeConverter
        @JvmStatic
        fun fromLatLng(value : LatLng) : String{
            val latString = value.latitude.toString()
            val lngString = value.longitude.toString()
            val altString = value.altitude.toString()
            val stringPoint = "${latString},${lngString},${altString}"
            return stringPoint
        }

        @TypeConverter
        @JvmStatic
        fun toLatLng(value : String) : LatLng{
            val location = value.split(",")
            val latitude = location[0].toDouble()
            val longitude = location[1].toDouble()
            val altitude = location[2].toDouble()
            return LatLng(latitude,longitude,altitude)
        }
    }
}