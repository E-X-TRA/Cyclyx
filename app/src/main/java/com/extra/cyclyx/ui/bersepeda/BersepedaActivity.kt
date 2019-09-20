package com.extra.cyclyx.ui.bersepeda

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.extra.cyclyx.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_bersepeda.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class BersepedaActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var locationRequest: LocationRequest
//    private var locationUpdateState = false

    lateinit var markerPoints : ArrayList<LatLng>

    //request code location
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    //request permission code
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20f))
            }
        }
    }

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var lastLocation: Location
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bersepeda)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //all the map nibba
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult?) {
//                super.onLocationResult(p0)
//
//                lastLocation = p0!!.lastLocation
////                placeMarkerOnMap(LatLng(lastLocation.latitude,lastLocation.longitude))
//            }
//        }
//        createLocationRequest()
        //logic for stopwatch
        stopWatch()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = false
        map.setOnMarkerClickListener(this)

        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5f)

        markerPoints = ArrayList<LatLng>()

        map.setOnMapClickListener {
            if(markerPoints.size > 1){
                markerPoints.clear()
                map.clear()
            }

            markerPoints.add(it)
            placeMarkerOnMap(it)

            if(markerPoints.size>=2){
                var origin : LatLng = markerPoints[0]
                var dest : LatLng = markerPoints[1]

                val url = getUrl(origin,dest)
                doAsync{
                    val result = URL(url).readText()
                    uiThread {
                        val parser : Parser = Parser.default()
                        val stringBuilder : StringBuilder = StringBuilder(result)
                        val json : JsonObject = parser.parse(stringBuilder) as JsonObject
                        val routes = json.array<JsonObject>("routes")
                        val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                        val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }
                        options.add(origin)
                        for (point in polypts) options.add(point)
                        options.add(dest)
                        map.addPolyline(options)
                    }
                }
            }
        }
        setUpMap()
    }

    private fun getUrl(from : LatLng,to : LatLng) : String{
        val origin = "origin="+from.latitude+","+from.longitude
        val dest = "destination="+to.latitude+","+to.longitude
        val sensor = "sensor=false"
        val key = applicationContext.resources.getString(R.string.google_maps_key)
        val params = "$origin&$dest&$sensor&key=$key"

        Log.d("DIRECTION","https://maps.googleapis.com/maps/api/directions/json?$params")
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        map.addMarker(markerOptions)
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

//    private fun startLocationUpdates() {
//        //request permission
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//            return
//        }
//        //if location access granted, start receiving update
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            null /* Looper */
//        )
//    }

//    private fun createLocationRequest() {
//        locationRequest = LocationRequest()
//        locationRequest.interval = 10000
//
//        locationRequest.fastestInterval = 5000
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//        val client = LocationServices.getSettingsClient(this)
//        val task = client.checkLocationSettings(builder.build())
//
//        task.addOnSuccessListener {
//            locationUpdateState = true
//            startLocationUpdates()
//        }
//        task.addOnFailureListener { e ->
//            if (e is ResolvableApiException) {
//                try {
//                    e.startResolutionForResult(this@BersepedaActivity, REQUEST_CHECK_SETTINGS)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    //ignore the error
//                }
//            }
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CHECK_SETTINGS) {
//            if (resultCode == Activity.RESULT_OK) {
//                locationUpdateState = true
//                startLocationUpdates()
//            }
//        }
//    }

//    override fun onPause() {
//        super.onPause()
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (!locationUpdateState) {
//            startLocationUpdates()
//        }
//    }
    fun stopWatch() {
        var pauseOffset: Long = 0
        var running: Boolean = false
        chronometer.base = SystemClock.elapsedRealtime()

        if (!running) {
            Toast.makeText(this, "Started!", Toast.LENGTH_SHORT).show()
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            running = true
        }

        imgStop.setOnClickListener {
            chronometer.stop()
            this.finish()
            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show()
        }

        imgPause.setOnClickListener {
            if (running) {
                chronometer.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
                imgPause.setBackgroundResource(R.drawable.play)
                Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show()
            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
                imgPause.setBackgroundResource(R.drawable.pause)
                Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
