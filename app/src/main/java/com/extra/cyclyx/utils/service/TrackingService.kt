package com.extra.cyclyx.utils.service

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.extra.cyclyx.R
import com.extra.cyclyx.ui.bersepeda.BersepedaActivity
import com.extra.cyclyx.utils.*
import com.mapbox.android.core.location.*
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import java.lang.ref.WeakReference

class TrackingService : Service(){
    private val CHANNEL_ID = "cyclyx notification"
    private lateinit var context: Context
    //location related init
    private lateinit var cyclyxLocationEngine: LocationEngine
    private val callback = LocationListeningCallback(this)
    private val pointList = ArrayList<Point>()

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun makeNotification(descriptionText: String): Notification? {
        val intent = Intent(this, BersepedaActivity::class.java)
        createNotificationChannel()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        Log.d("TRACKING", "Notification Compat")
        val bBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        val color = 0xFF297FC3
        bBuilder.setContentTitle("My Trip")
            .setContentText(descriptionText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(color.toInt())
            .setContentIntent(pendingIntent)
        return bBuilder.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                START_SERVICE -> {
                    initializeLocationEngine()
                    startForeground(1024, makeNotification("Tracking Your Trip"))
                    Toast.makeText(applicationContext, "Service Started!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("TRACKING", "Service Started!")
                }
                PAUSE_SERVICE -> {
                    startForeground(1024,makeNotification("Trip Is Paused"))
                    cyclyxLocationEngine.removeLocationUpdates(callback)
                    Log.d("TRACKING", "Service Paused!")
                }
                STOP_SERVICE -> {
                    cyclyxLocationEngine.removeLocationUpdates(callback)
                    stopForeground(true)
                    stopSelf()
                    Toast.makeText(applicationContext, "Service Stopped!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("TRACKING", "Service Stopped!")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //implement Service()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //LocationUpdate
    //start location engine
    @SuppressLint("MissingPermission")
    fun initializeLocationEngine() {
        cyclyxLocationEngine = LocationEngineProvider.getBestLocationEngine(this.applicationContext)
        val CYCLYX_INTERVAL = ONE_SECOND * 5 // custom interval
        val request =
            LocationEngineRequest.Builder(CYCLYX_INTERVAL) //interval per request
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(CYCLYX_INTERVAL) // fastest interval
                .setMaxWaitTime(CYCLYX_INTERVAL * 6) //max wait time for batch update
                .build()
        cyclyxLocationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }

    //callback that we use in request location update
    private class LocationListeningCallback internal constructor(service: TrackingService) :
        LocationEngineCallback<LocationEngineResult> {
        private val serviceWeakReference: WeakReference<TrackingService>

        init {
            this.serviceWeakReference =
                WeakReference(service) //create a weak reference of this service so it can access the service from inside
        }

        override fun onSuccess(result: LocationEngineResult?) {
            val service = serviceWeakReference.get()
            if (service != null) {
                val location = result?.lastLocation ?: return
                service.pointList.add(Point.fromLngLat(location.longitude, location.latitude))
                val routeString = service.encodePointToString(service.pointList)
                Log.d("TRACKING", "RouteString : $routeString")
                service.sendBroadcast(routeString)
            } else {
                Log.d("TRACKING", "Service Reference Null")
            }
        }

        override fun onFailure(exception: Exception) {
            Log.d("TRACKING", "Location Update Failed")
        }
    }

    //encode route using PolylineUtils
    private fun encodePointToString(points: List<Point>): String {
        return PolylineUtils.encode(points, 5)
    }

    //send the data via broadcast
    private fun sendBroadcast(route: String) {
        val intentSendLocationRoute = Intent("LocationUpdates")
        intentSendLocationRoute.putExtra(ENCODED_STRING, route)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentSendLocationRoute)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}