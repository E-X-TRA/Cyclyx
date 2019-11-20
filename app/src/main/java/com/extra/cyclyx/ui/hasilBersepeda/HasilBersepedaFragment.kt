package com.extra.cyclyx.ui.hasilBersepeda


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.extra.cyclyx.R
import com.extra.cyclyx.database.AppDatabase
import com.extra.cyclyx.databinding.FragmentHasilBersepedaBinding
import com.extra.cyclyx.utils.*
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

/**
 * A simple [Fragment] subclass.
 */
class HasilBersepedaFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentHasilBersepedaBinding
    private lateinit var viewModel: HasilBersepedaViewModel
    private lateinit var map: MapboxMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(context!!, resources.getString(R.string.mapbox_token))
        binding = FragmentHasilBersepedaBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application
        val arguments = HasilBersepedaFragmentArgs.fromBundle(arguments!!)
        val dataSource = AppDatabase.getInstance(application).bersepedaDAO
        val viewModelFactory = HasilBersepedaViewModelFactory(arguments.bersepedaKey,dataSource)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HasilBersepedaViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.routeList.observe(this, Observer {route ->
            route?.let{
                if(::map.isInitialized) {
                    addPointToMap(route)
                    moveCamera(route.first(), map)
                }
            }
        })

        viewModel.backToMenu.observe(this, Observer {status ->
            status?.let{
                if(it){
                    this.findNavController().navigate(HasilBersepedaFragmentDirections.navigateToRiwayatFromHasilBersepeda())
                    viewModel.doneNavigating()
                }
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setStyle(
            Style.MAPBOX_STREETS
        ) {
            it.addSource(GeoJsonSource(SOURCE_ID))
            it.addLayer(
                CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
                    PropertyFactory.circleColor(CIRCLE_COLOR),
                    PropertyFactory.circleRadius(CIRCLE_RADIUS)
                )
            )
            it.addLayerBelow(
                LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                    PropertyFactory.lineColor(LINE_COLOR),
                    PropertyFactory.lineWidth(LINE_WIDTH),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                ), CIRCLE_LAYER_ID
            )
            viewModel.onMapAsyncFinished()
        }
    }

    private fun moveCamera(point: Point, map : MapboxMap){
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(point.latitude(),point.longitude()))
            .zoom(18.0)
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        Log.i("TRACKING", "ONSTART!")
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        Log.i("TRACKING", "ONRESUME!")
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        Log.i("TRACKING", "ONPAUSE!")
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        Log.i("TRACKING", "ONSTOP!")
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        Log.i("TRACKING", "ONDESTROY!")
        super.onDestroy()
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
