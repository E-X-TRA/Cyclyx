package com.extra.cyclyx.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.mapbox.mapboxsdk.maps.Style
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun convertDurationToString(duration: Long): String {
    val durationInSeconds = (duration / 1000).toInt()
    val seconds = durationInSeconds % 60
    val minutes = (durationInSeconds / 60) % 60
    val hours = (minutes / 60) % 60
    val days = hours / 24
    val stringSeconds: String = convertTimeUnitToString(seconds)
    val stringMinutes: String = convertTimeUnitToString(minutes)
    val stringHours: String = convertTimeUnitToString(hours)

    return when {
        hours > 24 -> {
            if (days > 1) {
                "${days} days ${stringHours}:${stringMinutes}:${stringSeconds}"
            } else {
                "${days} day ${stringHours}:${stringMinutes}:${stringSeconds}"
            }
        }
        else -> {
            "${stringHours}:${stringMinutes}:${stringSeconds}"
        }
    }
}

fun convertDurationToShortestString(duration: Long): List<String> {
    val durationInSeconds = (duration / 1000).toInt()
    val seconds = durationInSeconds % 60
    val minutes = (durationInSeconds / 60) % 60
    val hours = (minutes / 60) % 60
    val days = hours / 24

    var usage = ""
    var timeUnit = ""
    when {
        days >= 1 -> {
            usage = "+$days"
            timeUnit = "Hari"
        }
        days < 1 -> {
            if (hours > 0) {
                usage = "+$hours"
                timeUnit = "Jam"
            } else {
                if (minutes > 0) {
                    usage = "+$minutes"
                    timeUnit = "Menit"
                } else {
                    if (seconds == 0) {
                        usage = "-"
                        timeUnit = "-"
                    } else {
                        usage = "$seconds"
                        timeUnit = "Detik"
                    }
                }
            }
        }
        else -> {
            "ERR"
        }
    }
    return listOf(usage, timeUnit)
}

fun convertDurationToShortString(duration: Long): String {
    val durationInSeconds = (duration / 1000).toInt()
    val seconds = durationInSeconds % 60
    val minutes = (durationInSeconds / 60) % 60
    val hours = (minutes / 60) % 60
    val days = hours / 24
    //the 00:00:00 format...
    val stringSeconds: String = convertTimeUnitToString(seconds)
    val stringMinutes: String = convertTimeUnitToString(minutes)
    val stringHours: String = convertTimeUnitToString(hours)

    return when {
        days >= 1 -> {
            "+${days} hari"
        }
        days < 1 -> {
            if (hours > 0) {
                "+${hours} jam"
            } else {
                if (minutes > 0) {
                    "+${minutes} menit"
                } else {
                    "${seconds} detik"
                }
            }
        }
        else -> {
            "${stringHours}:${stringMinutes}:${stringSeconds}"
        }
    }
}

// we use HH::MM::SS
//make readable string from time unit eg. 2 minutes -> 02 not 2
fun convertTimeUnitToString(unit: Int): String {
    if (unit.toString().length < 2) {
        return "0" + unit.toString()
    } else {
        return unit.toString()
    }
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)
    return format.format(date)
}

fun convertLongToSecond(time: Long): Double {
    return (time / 1000).toDouble()
}

fun convertLongToHour(time: Long): Double {
    return (time / 1000) / 3600.toDouble()
}

fun convertLongToMinute(time: Long): Double {
    return (time / 1000) / 60.toDouble()
}

fun determineMets(speed: Double): Double {
    return when {
        speed > 0.0 && speed <= 8.8 -> {
            3.0 //slow
        }
        speed >= 8.9 && speed <= 15.4 -> {
            5.8 //normal
        }
        speed >= 15.5 && speed <= 19.3 -> {
            6.8 //light effort
        }
        speed >= 19.4 && speed <= 22.5 -> {
            8.0 //moderate effort
        }
        speed >= 22.6 && speed <= 24.1 -> {
            10.0 //vigorous effort
        }
        speed >= 24.2 && speed <= 30.58 -> {
            12.0 //racing
        }
        else -> {
            if (speed > 30.58) {
                15.8 //top speed
            } else {
                0.0 //not moving
            }
        }
    }
}

fun determineMapStyle(stylePref: String?): String {
    Log.d("MAP", "Style : $stylePref")
    return when (stylePref) {
        MAPBOX_STYLE.OUTDOORS -> Style.OUTDOORS
        MAPBOX_STYLE.STREETS -> Style.MAPBOX_STREETS
        MAPBOX_STYLE.TRAFFIC -> Style.TRAFFIC_DAY
        else -> Style.LIGHT
    }
}

fun formatDouble(double: Double, pattern: String): String {
    val df = DecimalFormat(pattern)
    df.roundingMode = RoundingMode.CEILING
    return df.format(double)
}

fun determineZoomLevel(distanceBetweenPoint: Double): Double { //distance in km
    Log.d("TRACKING", "Rough Distance = $distanceBetweenPoint")
    return when {
        distanceBetweenPoint <= 0.5 -> 17.0
        distanceBetweenPoint > 0.5 && distanceBetweenPoint <= 5.0 -> 15.0
        distanceBetweenPoint > 5.0 && distanceBetweenPoint <= 20.0 -> 13.0
        distanceBetweenPoint > 20.0 && distanceBetweenPoint <= 80.0 -> 11.0
        distanceBetweenPoint > 80.0 && distanceBetweenPoint <= 320.0 -> 9.0
        distanceBetweenPoint > 320.0 && distanceBetweenPoint <= 640.0 -> 7.0
        else -> 6.0
    }
}

val indonesianLocale = Locale("id")
val birthDateFormat = SimpleDateFormat("DD-MM-YYYY", Locale.US)
val cyclyxDateFormat = SimpleDateFormat("DD MMMM YYYY", indonesianLocale)

fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

fun SharedPreferences.getDouble(key: String, default: Double) =
    java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))

class RandomDataGenerator {
    companion object {
        private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

        fun getRandomString(sizeOfRandomString: Int): String {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString)
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            return sb.toString()
        }

        fun getRandomListItem(listSize: Int): Int {
            return (0..listSize - 1).random()
        }
    }
}

object FirebaseHelpers{
    class FirebaseQueryLiveData : LiveData<DataSnapshot> {
        private val query: Query
        private val listener =
            MyValueEventListener()

        constructor(query: Query) {
            this.query = query
        }

        constructor(ref: DatabaseReference) {
            query = ref
        }

        override fun onActive() {
            query.addValueEventListener(listener)
        }

        override fun onInactive() {
            query.removeEventListener(listener)
        }

        private inner class MyValueEventListener : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                value = dataSnapshot
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    LOG_TAG,
                    "Can't listen to query $query",
                    databaseError.toException()
                )
            }
        }

        companion object {
            private const val LOG_TAG = "FirebaseQueryLiveData"
        }
    }
}