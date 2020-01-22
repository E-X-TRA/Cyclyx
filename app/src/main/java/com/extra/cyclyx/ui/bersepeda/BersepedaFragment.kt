package com.extra.cyclyx.ui.bersepeda


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentBersepedaBinding
import com.extra.cyclyx.utils.*
import com.extra.cyclyx.utils.service.TrackingService
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * A simple [Fragment] subclass.
 */
class BersepedaFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentBersepedaBinding
    private lateinit var map: MapboxMap
    private lateinit var viewModel: BersepedaViewModel
    private lateinit var ctx: Context

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(
            Style.OUTDOORS
        ) {
            it.addSource(GeoJsonSource(SOURCE_ID))
            it.addLayer(
                CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
                    circleColor(CIRCLE_COLOR),
                    circleRadius(CIRCLE_RADIUS)
                )
            )
            it.addLayerBelow(
                LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                    lineColor(LINE_COLOR),
                    lineWidth(LINE_WIDTH),
                    lineJoin(LINE_JOIN_ROUND)
                ), CIRCLE_LAYER_ID
            )
            initializeLocationComponent(it)
        }
    }

    private fun initializeLocationComponent(loadedMapStyle: Style) {
        val locationComponent = map.locationComponent
        locationComponent.activateLocationComponent(
            LocationComponentActivationOptions.builder(
                context!!, loadedMapStyle
            ).build()
        )
        locationComponent.isLocationComponentEnabled = true
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.NORMAL
    }

    private fun addPointToMap(points: List<Point>) {
        map.getStyle {
            val geoJsonSource = it.getSourceAs<GeoJsonSource>(SOURCE_ID)
            if (geoJsonSource != null) {
                val lineString = LineString.fromLngLats(points)
                geoJsonSource.setGeoJson(
                    FeatureCollection.fromFeature(
                        Feature.fromGeometry(
                            lineString
                        )
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //init maps and binding
        Mapbox.getInstance(context!!, com.extra.cyclyx.BuildConfig.apikey)
        binding = FragmentBersepedaBinding.inflate(inflater)

        binding.lifecycleOwner = this
        //init context for use within fragment
        ctx = activity?.applicationContext!!
        //init viewModel
        val application = requireNotNull(this.activity).application
        viewModel =
            ViewModelProviders.of(this, BersepedaViewModel.Factory(application)).get(BersepedaViewModel::class.java)

        //bind viewmodel
        binding.viewModel = viewModel
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        //observe changes
        viewModel.locationPoints.observe(this, Observer {
            it?.let {
                addPointToMap(it)
            }
        })

        modifyTrackingService(START_SERVICE)
        viewModel.onStart()

        viewModel.trackingStatus.observe(this, Observer { status ->
            when (status) {
                TRACKING_STARTED, TRACKING_RESUMED -> {
                    binding.imgPause.setBackgroundResource(R.drawable.pause)
                    binding.imgPause.setOnClickListener {
                        modifyTrackingService(PAUSE_SERVICE)
                        viewModel.onPause()
                    }

                    binding.imgStop.setOnClickListener {
                        modifyTrackingService(STOP_SERVICE)
                        viewModel.onStop()
                    }
                }
                TRACKING_PAUSED -> {
                    binding.imgPause.setBackgroundResource(R.drawable.play)
                    binding.imgPause.setOnClickListener {
                        modifyTrackingService(START_SERVICE)
                        viewModel.onResume()
                    }

                    binding.imgStop.setOnClickListener {
                        viewModel.onStop()

                    }
                }
                TRACKING_STOPPED -> {
                    binding.imgPause.visibility = View.INVISIBLE
                    binding.imgStop.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.navigateToResult.observe(this, Observer {act ->
            act?.let{
                navigateToResult(act.id)
                viewModel.doneNavigatingToResult()
            }
        })
            // Inflate the layout for this fragment
        return binding.root
    }

    private fun modifyTrackingService(act: String) {
        val intent = Intent(context, TrackingService::class.java)
        intent.action = act
        context?.startService(intent)
    }

    private fun navigateToResult(id : Long){
        this.findNavController().navigate(BersepedaFragmentDirections.navigateToHasilBersepedaFromBersepeda(id))
    }

    //receive service location update
    private val locationUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val stringRoute = intent.getStringExtra(EXTRA_ROUTE)
                val doubleAlt = intent.getDoubleExtra(EXTRA_ALT,0.0)
                viewModel.processLocationUpdate(stringRoute!!,doubleAlt)
            }
        }
    }

    //necessary for mapbox mapview to adapt lifecycle
    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        //register broadcast receiver
        LocalBroadcastManager.getInstance(ctx).registerReceiver(
            locationUpdateReceiver,
            IntentFilter("LocationUpdates")
        )
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        LocalBroadcastManager.getInstance(ctx)
            .unregisterReceiver(locationUpdateReceiver) // and stop receiving broadcast
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        modifyTrackingService(STOP_SERVICE) //dont forget to stop service
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

}
