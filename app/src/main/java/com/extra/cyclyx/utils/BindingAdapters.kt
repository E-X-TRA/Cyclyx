package com.extra.cyclyx.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.extra.cyclyx.entity.Bersepeda


@BindingAdapter("timerValue")
fun TextView.timerValue(elapsedTime : Long){
    text = convertDurationToString(elapsedTime)
}

@BindingAdapter("formatDuration")
fun TextView.formatDuration(act : Bersepeda?){
    act?.let{
        val duration = act.endTime - act.startTime
        text = convertDurationToString(duration)
    }
}

@BindingAdapter("formatShortDuration")
fun TextView.formatShortDuration(act : Bersepeda?){
    act?.let{
        val duration = act.endTime - act.startTime
        text = convertDurationToShortString(duration)
    }
}

@BindingAdapter("formatDistance")
fun TextView.formatDistance(double : Double?){
    double?.let{
        text = "${formatDouble(double, "#.#")} KM"
    }
}

@BindingAdapter("formatSpeed")
fun TextView.formatSpeed(double : Double?){
    double?.let{
        text = "${formatDouble(double, "#.#")} KPH"
    }
}

@BindingAdapter("formatCalorie")
fun TextView.formatCalorie(double : Double?){
    double?.let {
        text = "${formatDouble(double,"#.#")} kcal"
    }
}

@BindingAdapter("formatElevation")
fun TextView.formatElevation(act : Bersepeda?){
    act?.let {
        text = "${formatDouble(act.elevationGain,"#.##")}+ || ${formatDouble(act.elevationLoss,"#.##")}-"
    }
}

@BindingAdapter("formatProgression")
fun TextView.formatProgression(progress : Int){
    progress?.let {
        text = "$progress"
    }
}

@BindingAdapter("formatDate")
fun TextView.formatDate(act : Bersepeda?){
    act?.let {
        text = cyclyxDateFormat.format(act.startTime)
    }
}

@BindingAdapter("showInt")
fun TextView.showInt(int : Int?){
    int?.let {
        text = "$int"
    }
}

@BindingAdapter("toggleVisibilityRiwayat")
fun View.toogleVisibilityRiwayat(flag : Boolean){
    if(flag){
        visibility = View.VISIBLE
    }else{
        visibility = View.GONE
    }
}
