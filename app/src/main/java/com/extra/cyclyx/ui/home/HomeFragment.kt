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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.extra.cyclyx.R
import com.extra.cyclyx.SettingsActivity
import com.extra.cyclyx.databinding.FragmentHomeBinding
import com.extra.cyclyx.entity.ReferenceItem
import com.extra.cyclyx.utils.PERMISSION_FINE_LOCATION_REQUEST
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        val app = requireNotNull(activity).application
        viewModel = ViewModelProvider(this,HomeViewModel.Factory(app)).get(HomeViewModel::class.java)

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

        viewModel.navigateToKesiapan.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(HomeFragmentDirections.navigateToKesiapanFromHome())
                viewModel.doneNavigateToKesiapan()
            }
        })


        viewModel.navigateToPengaturan.observe(viewLifecycleOwner, Observer {
            it?.let {
                val intent = Intent(activity, SettingsActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigateToPengaturan()
            }
        })

        viewModel.tipsLiveData.observe(viewLifecycleOwner, Observer<DataSnapshot>{
            it?.let {
                val arrayItem = ArrayList<ReferenceItem>()
                for(i in it.children){
                    val model = i.getValue(ReferenceItem::class.java)
                    model?.let {
                        arrayItem.add(it)
                    }
                }
                viewModel.addItemsToList(arrayItem)
            }
        })

        return binding.root
    }

}