package com.extra.cyclyx.ui.pendaftaran


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.extra.cyclyx.R

/**
 * A simple [Fragment] subclass.
 */
class PendaftaranDataDiri : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendaftaran_data_diri, container, false)
    }


}
