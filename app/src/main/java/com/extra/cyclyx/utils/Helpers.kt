package com.extra.cyclyx.utils

import android.util.Log
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun convertDurationToString(duration: Long) : String{
    val durationInSeconds = (duration/1000).toInt()
    val seconds = durationInSeconds % 60
    val minutes = (durationInSeconds/60) % 60
    val hours = (minutes/60) % 60
    val days = hours/24
    val stringSeconds : String = convertTimeUnitToString(seconds)
    val stringMinutes : String = convertTimeUnitToString(minutes)
    val stringHours : String = convertTimeUnitToString(hours)

    return when{
        hours > 24 ->{
            if(days > 1){
                "${days} days ${stringHours}:${stringMinutes}:${stringSeconds}"
            }else{
                "${days} day ${stringHours}:${stringMinutes}:${stringSeconds}"
            }
        }
        else ->{
            "${stringHours}:${stringMinutes}:${stringSeconds}"
        }
    }
}

// we use HH::MM::SS
//make readable string from time unit eg. 2 minutes -> 02 not 2
fun convertTimeUnitToString(unit: Int) : String{
    if(unit.toString().length < 2){
        return "0" + unit.toString()
    }else{
        return unit.toString()
    }
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm",Locale.US)
    return format.format(date)
}

fun convertLongToSecond(time : Long) : Double{
    return (time/1000).toDouble()
}

fun convertLongToHour(time : Long) : Double{
    return (time/3600000).toDouble()
}

fun determineMets(speed : Double) : Double{
    return when{
        speed > 0.0 && speed <= 8.8 -> {
            3.0 //slow
        }
        speed >= 8.9&& speed <= 15.4 -> {
            5.8 //normal
        }
        speed >= 15.5&& speed <= 19.3 -> {
            6.8 //light effort
        }
        speed >= 19.4&& speed <= 22.5 -> {
            8.0 //moderate effort
        }
        speed >= 22.6&& speed <= 24.1 -> {
            10.0 //vigorous effort
        }
        speed >= 24.2&& speed <= 30.58 -> {
            12.0 //racing
        }
        else -> {
            if(speed > 30.58){
                15.8 //top speed
            }else{
                0.0 //not moving
            }
        }
    }
}

fun formatDouble(double : Double,pattern : String) : String{
    val df = DecimalFormat(pattern)
    df.roundingMode = RoundingMode.CEILING
    return df.format(double)
}

fun determineZoomLevel(distanceBetweenPoint : Double) : Double{ //distance in km
    Log.d("TRACKING","Rough Distance = $distanceBetweenPoint")
    return when{
        distanceBetweenPoint <= 0.5 -> 17.0
        distanceBetweenPoint > 0.5 && distanceBetweenPoint <= 5.0 -> 15.0
        distanceBetweenPoint > 5.0 && distanceBetweenPoint <= 20.0 -> 13.0
        distanceBetweenPoint > 20.0 && distanceBetweenPoint <= 80.0 -> 11.0
        distanceBetweenPoint > 80.0 && distanceBetweenPoint <= 320.0 -> 9.0
        distanceBetweenPoint > 320.0 && distanceBetweenPoint <= 640.0 -> 7.0
        else -> 6.0
    }
}