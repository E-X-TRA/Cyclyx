package com.extra.cyclyx.utils

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