package com.extra.cyclyx.ui.pengenalan


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.R

/**
 * A simple [Fragment] subclass.
 */
class PengenalanSelesaiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengenalan_selesai, container, false)
    }


}
