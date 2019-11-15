package com.extra.cyclyx.ui.home

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentHomeBinding
import com.extra.cyclyx.ui.bersepeda.BersepedaActivity
import com.extra.cyclyx.utils.PERMISSION_FINE_LOCATION_REQUEST
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        val app = requireNotNull(activity).application
        val viewModelFactory = HomeViewModelFactory(app)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        if(!viewModel.isLocationPermissionGranted){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity!!,Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(view!!,getString(R.string.permission_location_explanation),Snackbar.LENGTH_INDEFINITE).show()
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_FINE_LOCATION_REQUEST)
            }else{
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_FINE_LOCATION_REQUEST)
            }
        }

        if(viewModel.isBatteryOptimized){ //if optimized
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val name = resources.getString(R.string.app_name)
                Toast.makeText(
                    app.applicationContext,
                    "Battery optimization -> All apps -> $name -> Don't optimize",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                startActivity(intent)
            }
        }

        binding.btnGo.setOnClickListener {
            val intent = Intent(activity,BersepedaActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}