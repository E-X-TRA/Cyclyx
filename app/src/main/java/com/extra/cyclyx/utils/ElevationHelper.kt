package com.extra.cyclyx.utils

import android.content.Context
import android.util.Log
import com.extra.cyclyx.utils.matthiaszimmerman.LocationPoint
import com.extra.cyclyx.utils.matthiaszimmerman.egm96.Geoid
import com.mapbox.geojson.Point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ElevationHelper(val context: Context) {
    init{
        CoroutineScope(Dispatchers.Default).launch {
            initializeGeoid()
        }
    }

    suspend fun initializeGeoid(){
        withContext(Dispatchers.IO){
            try{
                Geoid.init()
            } catch (e : IOException) {
                Log.e("TRACKING", "Altitude correction $e");
            }
        }
    }

    fun getOffset(point : Point) : Double{
        return Geoid.getOffset(LocationPoint(point.latitude(),point.longitude()))
    }
}