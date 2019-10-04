package com.extra.cyclyx.ui.bersepeda

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_bersepeda.*


class BersepedaActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener, OnCameraTrackingChangedListener
{
    override fun onCameraTrackingChanged(currentMode: Int) {
       //empty on purpose
    }

    override fun onCameraTrackingDismissed() {
        isInTrackingMode=false
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "Location Permission Needed", Toast.LENGTH_LONG).show();
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show()
            mapboxMap.getStyle {
                enableLocationComponent(it)
            }
        }else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        val cameraZoom = CameraPosition.Builder()
            .zoom(16.0)
            .build()
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraZoom),100)
        mapboxMap.setStyle(Style.LIGHT) {
            enableLocationComponent(it)
        }
    }

    private fun enableLocationComponent(loadedMapStyle : Style){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            Toast.makeText(this,"Permission Granted!",Toast.LENGTH_LONG).show()
            var customLocationMarker = LocationComponentOptions.builder(this)
                .elevation(5F)
                .build()

            locationComponent = mapboxMap.locationComponent

            var markerActivationOptions = LocationComponentActivationOptions.builder(this,loadedMapStyle)
                .locationComponentOptions(customLocationMarker)
                .build()

            locationComponent.activateLocationComponent(markerActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = CameraMode.TRACKING
            locationComponent.renderMode = RenderMode.COMPASS
            locationComponent.addOnCameraTrackingChangedListener(this)
        }else{
            permissionsManager = PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationComponent: LocationComponent
    private var isInTrackingMode : Boolean = true

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize this first before setContentView
        Mapbox.getInstance(this,applicationContext.resources.getString(com.extra.cyclyx.R.string.mapbox_token)+"");
        setContentView(com.extra.cyclyx.R.layout.activity_bersepeda)
        //map set
        mapView = findViewById(com.extra.cyclyx.R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        //logic for stopwatch
        stopWatch()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

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
                imgPause.setBackgroundResource(com.extra.cyclyx.R.drawable.play)
                Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show()
            } else {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer.start()
                running = true
                imgPause.setBackgroundResource(com.extra.cyclyx.R.drawable.pause)
                Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
