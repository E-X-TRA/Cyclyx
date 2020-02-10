package com.extra.cyclyx.ui.statistik.riwayat

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.extra.cyclyx.databinding.FragmentRiwayatBinding
import com.extra.cyclyx.ui.adapter.RiwayatAdapter
import com.extra.cyclyx.ui.statistik.StatistikFragmentDirections
import com.extra.cyclyx.utils.DELETE_ITEM
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
        viewModel = ViewModelProvider(this, RiwayatViewModel.Factory(application)).get(RiwayatViewModel::class.java)

        binding.viewModel = viewModel

        binding.rvRiwayat.layoutManager = LinearLayoutManager(context)
        val adapter = RiwayatAdapter(RiwayatAdapter.RiwayatClickListener { actId, action ->
            when (action) {
                DETAIL_ITEM -> {
                    viewModel.onActClicked(actId)
                }
                DELETE_ITEM -> {
                    showDeleteItemConfirmation(actId)
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
                this.findNavController().navigate(StatistikFragmentDirections.navigateToHasilBersepedaFromStatistik(it))
                viewModel.doneNavigateToHasilBersepeda()
            }
        })

        return binding.root
    }

    private fun showDeleteItemConfirmation(actId :Long){
        AlertDialog.Builder(context)
            .setTitle("Hapus Item Ini") // GPS not found
            .setMessage("Anda Yakin untuk Menghapus Item Ini ?") // Want to enable?
            .setPositiveButton("Ya", DialogInterface.OnClickListener { _, _->
                viewModel.onDeleteClicked(actId)
            })
            .setNegativeButton("Tidak",DialogInterface.OnClickListener { _, _ -> })
            .show()
    }
}