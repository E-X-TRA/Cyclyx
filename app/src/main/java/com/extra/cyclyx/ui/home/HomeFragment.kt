package com.extra.cyclyx.ui.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.extra.cyclyx.R
import com.extra.cyclyx.SettingsActivity
import com.extra.cyclyx.databinding.FragmentHomeBinding
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
        viewModel = ViewModelProviders.of(this,HomeViewModel.Factory(app)).get(HomeViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        if(!viewModel.isLocationPermissionGranted){
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(requireView(),getString(R.string.permission_location_explanation),Snackbar.LENGTH_INDEFINITE).show()
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_FINE_LOCATION_REQUEST)
            }else{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_FINE_LOCATION_REQUEST)
            }
        }

        viewModel.navigateToKesiapan.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(HomeFragmentDirections.navigateToKesiapanFromHome())
                viewModel.doneNavigateToKesiapan()
            }
        })


        viewModel.navigateToPengaturan.observe(this, Observer {
            it?.let {
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigateToPengaturan()
            }
        })

        return binding.root
    }

}