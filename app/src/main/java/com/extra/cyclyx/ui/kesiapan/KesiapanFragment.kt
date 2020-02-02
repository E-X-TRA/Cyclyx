package com.extra.cyclyx.ui.kesiapan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.extra.cyclyx.BersepedaActivity
import com.extra.cyclyx.databinding.FragmentKesiapanBinding
import com.extra.cyclyx.entity.ReferenceItem
import com.google.firebase.database.DataSnapshot

class KesiapanFragment : Fragment() {
    private lateinit var viewModel : KesiapanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentKesiapanBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val app = requireNotNull(activity).application
        viewModel = ViewModelProvider(this, KesiapanViewModel.Factory(app)).get(KesiapanViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToKonfigurasi.observe(this, Observer {
            it?.let{
                this.findNavController().navigate(KesiapanFragmentDirections.navigateToKonfigurasiFromKesiapan())
                viewModel.doneNavigateToKonfigurasi()
            }
        })

        viewModel.navigateToBersepeda.observe(this, Observer {
            it?.let {
                val intent = Intent(activity, BersepedaActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigateToBersepeda()
            }
        })

        viewModel.motivasiLiveData.observe(this, Observer<DataSnapshot>{
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
