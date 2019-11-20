package com.extra.cyclyx.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.extra.cyclyx.adapter.RiwayatAdapter
import com.extra.cyclyx.adapter.RiwayatClickListener
import com.extra.cyclyx.database.AppDatabase
import com.extra.cyclyx.databinding.FragmentRiwayatBinding
import com.extra.cyclyx.utils.DETAIL_ITEM

class RiwayatFragment : Fragment() {
    private lateinit var binding: FragmentRiwayatBinding
    private lateinit var viewModel: RiwayatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRiwayatBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).bersepedaDAO
        val viewModelFactory = RiwayatViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RiwayatViewModel::class.java)

        binding.viewModel = viewModel

        binding.rvRiwayat.layoutManager = LinearLayoutManager(context)
        val adapter = RiwayatAdapter(RiwayatClickListener{ actId, action ->
            when(action){
                DETAIL_ITEM -> {
                    viewModel.onActClicked(actId)
                }
            }
        })
        binding.rvRiwayat.adapter = adapter


        viewModel.acts.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToResult.observe(this, Observer {actId ->
            actId?.let{
                this.findNavController().navigate(RiwayatFragmentDirections.navigateToHasilBersepedaFromRiwayat())
                viewModel.doneNavigateToHasilBersepeda()
            }
        })

        return binding.root
    }
}