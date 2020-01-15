package com.extra.cyclyx.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.extra.cyclyx.entity.Bersepeda


@BindingAdapter("timerValue")
fun TextView.timerValue(elapsedTime : Long){
    text = convertDurationToString(elapsedTime)
}

@BindingAdapter("durationToString")
fun TextView.durationToString(act : Bersepeda?){
    act?.let{
        val duration = act.endTime - act.startTime
        text = convertDurationToString(duration)
    }
}

@BindingAdapter("formatDouble")
fun TextView.formatDouble(double : Double?){
    double?.let{
        text = "${formatDouble(double, "#.#")} KM"
    }
}

@BindingAdapter("showInt")
fun TextView.showInt(int : Int?){
    int?.let {
        text = "$int"
    }
}
