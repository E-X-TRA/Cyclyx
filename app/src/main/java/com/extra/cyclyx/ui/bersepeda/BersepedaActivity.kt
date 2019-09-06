package com.extra.cyclyx.ui.bersepeda

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.extra.cyclyx.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_bersepeda.*

class BersepedaActivity() : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {
    var handler: Handler? = null
    var displayStopWatch: TextView? = null

    internal var milliSecondT: Long = 0
    internal var startTime: Long = 0
    internal var buffTime: Long = 0
    internal var updateTime = 0L

    internal var Hours: Int = 0
    internal var Seconds: Int = 0
    internal var Minutes: Int = 0
    internal var MilliSecond: Int = 0

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

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

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)

                lastLocation = p0!!.lastLocation
//                placeMarkerOnMap(LatLng(lastLocation.latitude,lastLocation.longitude))
            }
        }
        createLocationRequest()
        //logic for stopwatch
        stopWatch()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
//        val home = LatLng(-6.914867, 107.653951)
//        map.addMarker(MarkerOptions().position(home).title("Home Sweet Home"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(home,12.0f))

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        setUpMap()
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        map.addMarker(markerOptions)
    }

    private fun startLocationUpdates() {
        //request permission
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
        //if location access granted, start receiving update
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000

        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@BersepedaActivity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    //ignore the error
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    fun stopWatch() {
        handler = Handler()
        displayStopWatch = findViewById(R.id._stopwatchTime)

        btnStop.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler?.postDelayed(runnable, 0)
        }

        btnPause.setOnClickListener {
            handler?.removeCallbacks(runnable)
        }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            milliSecondT = SystemClock.uptimeMillis() - startTime
            updateTime = buffTime + milliSecondT

            Seconds = (updateTime / 1000).toInt()
            Minutes = Seconds / 60
            Seconds = Seconds % 60

            MilliSecond = (updateTime % 1000).toInt()

            displayStopWatch?.text = Minutes.toString() + ":" + Seconds.toString()

            Log.d("STOPWATCH", "$Hours:$Minutes:$Seconds")
            handler?.postDelayed(this,0)
        }
    }
}
