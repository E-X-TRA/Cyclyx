package com.extra.cyclyx.ui.konfigurasi


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.extra.cyclyx.databinding.FragmentKonfigurasiBinding
import com.extra.cyclyx.ui.home.KonfigurasiViewModel


/**
 * A simple [Fragment] subclass.
 */
class KonfigurasiFragment : Fragment() {
    private lateinit var viewModel: KonfigurasiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentKonfigurasiBinding.inflate(inflater)
        val app = requireNotNull(activity).application
        viewModel = ViewModelProvider(this, KonfigurasiViewModel.Factory(app)).get(KonfigurasiViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.intentForLocation.observe(viewLifecycleOwner, Observer {
            it?.let{
                showLocationDialog()
                viewModel.doneIntentLocation()
            }
        })

        viewModel.intentForBatteryOptimization.observe(viewLifecycleOwner, Observer {
            it?.let{
                showBatteryOptimizationDialog()
                viewModel.doneIntentBatteryOptimization()
            }
        })

        viewModel.intentForPowerSaver.observe(viewLifecycleOwner, Observer {
            it?.let{
                showPowerSaverDialog()
                viewModel.doneIntentPowerSaver()
            }
        })

        return binding.root
    }

    private fun showLocationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Hidupkan Pelacak Lokasi") // GPS not found
            .setMessage("Hidupkan Pengaturan Lokasi supaya CYCLYX bisa mengetahui posisi anda saat bersepeda") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            })
            .show()
    }

    private fun showBatteryOptimizationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Matikan Optimisasi Baterai") // GPS not found
            .setMessage("Hilangkan CYCLYX dari Optimisasi Baterai supaya aplikasi dapat berjalan dengan baik") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                    Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                )
            })
            .show()
    }

    private fun showPowerSaverDialog(){
        AlertDialog.Builder(context)
            .setTitle("Matikan Mode Hemat Daya") // GPS not found
            .setMessage("Matikan Mode Hemat Daya supaya CYCLYX dapat berjalan dengan baik") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                        Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
                )
            })
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }
}
