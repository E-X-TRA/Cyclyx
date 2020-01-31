package com.extra.cyclyx.ui.hasilBersepeda


import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.turf.TurfMeasurement

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
        Log.d("RESULT","${arguments.bersepedaKey}")

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        viewModel =
            ViewModelProviders.of(this, HasilBersepedaViewModel.Factory(arguments.bersepedaKey, application)).get(HasilBersepedaViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.routeList.observe(this, Observer { route ->
            route?.let {
                Log.d("RESULT","OBSERVED ROUTE LIST")
                if (::map.isInitialized) {
                    Log.d("RESULT","UPDATE MAPS FROM ROUTE LIST")
                    addPointToMap(route)
                    val envelope = TurfMeasurement.envelope(
                        FeatureCollection.fromFeature(
                            Feature.fromGeometry(LineString.fromLngLats(route))
                        )
                    )
                    moveCamera(
                        TurfMeasurement.midpoint(route.first(), route.last()),
                        map,
                        determineZoomLevel(TurfMeasurement.length(envelope, "kilometers"))
                    )
                }
            }
        })

        viewModel.backToMenu.observe(this, Observer { status ->
            status?.let {
                if (it) {
                    this.findNavController()
                        .navigate(HasilBersepedaFragmentDirections.navigateToStatistikFromHasilBersepeda())
                    viewModel.doneNavigating()
                }
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        map.setMaxZoomPreference(18.0)
        map.setStyle(
            determineMapStyle(viewModel.mapStyle)
        ) {
            it.addSource(GeoJsonSource(SOURCE_ID))
            it.addImage(ICON_ID, BitmapFactory.decodeResource(
                resources,R.drawable.mapbox_marker_icon_default
            ))
            it.addLayer(
                LineLayer(LINE_LAYER_ID, SOURCE_ID).withProperties(
                    PropertyFactory.lineColor(LINE_COLOR),
                    PropertyFactory.lineWidth(LINE_WIDTH),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                )
            )
            it.addLayer(
                SymbolLayer(SYMBOL_LAYER_RED_ID, SOURCE_ID).withProperties(
                    PropertyFactory.iconImage(ICON_ID),
                    PropertyFactory.iconColor(Color.RED)
                )
            )
            it.addLayer(
                SymbolLayer(SYMBOL_LAYER_BLUE_ID, SOURCE_ID).withProperties(
                    PropertyFactory.iconImage(ICON_ID),
                    PropertyFactory.iconColor(Color.BLUE)
                )
            )

            viewModel.act.observe(this, Observer {act ->
                act?.let{
                    viewModel.onMapAsyncFinished()
                }
            })
        }
    }

    private fun moveCamera(point: Point,map : MapboxMap,zoom : Double){
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(point.latitude(),point.longitude()))
            .zoom(zoom)
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun addPointToMap(points: List<Point>) {
        map.getStyle {
            val geoJsonSource = it.getSourceAs<GeoJsonSource>(SOURCE_ID)
            if (geoJsonSource != null) {
                val listFeature = ArrayList<Feature>()
                val lineString = LineString.fromLngLats(points)
                listFeature.add(Feature.fromGeometry(
                    lineString
                ))
                listFeature.add(Feature.fromGeometry(
                    points.first()
                ))
                listFeature.add(Feature.fromGeometry(
                    points.last()
                ))
                geoJsonSource.setGeoJson(
                    FeatureCollection.fromFeatures(
                        listFeature
                    )
                )
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
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
