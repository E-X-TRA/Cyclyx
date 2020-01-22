package com.extra.cyclyx.ui.konfigurasi


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.databinding.FragmentKonfigurasiBinding


/**
 * A simple [Fragment] subclass.
 */
class KonfigurasiFragment : Fragment() {
    private lateinit var viewModel: KonfigurasiFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentKonfigurasiBinding.inflate(inflater)

        return binding.root
    }

    private fun showLocationSettingsDialog() {
        AlertDialog.Builder(context)
            .setTitle("Hidupkan Pelacak Lokasi") // GPS not found
            .setMessage("Pada laman selanjutnya, bla bla bla") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                    Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            })
            .show()
    }

    private fun showBatteryOptimizationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Matikan Optimisasi Baterai") // GPS not found
            .setMessage("Pada laman selanjutnya, bla bla bla") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                    Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            })
            .show()
    }

    private fun showBatterySaverDialog(){
        AlertDialog.Builder(context)
            .setTitle("Matikan Optimisasi Baterai") // GPS not found
            .setMessage("Pada laman selanjutnya, bla bla bla") // Want to enable?
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                activity?.startActivity(
                    Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            })
            .show()
    }
}
