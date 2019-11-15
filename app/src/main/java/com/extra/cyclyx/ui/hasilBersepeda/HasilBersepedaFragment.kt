package com.extra.cyclyx.ui.hasilBersepeda


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.extra.cyclyx.R

/**
 * A simple [Fragment] subclass.
 */
class HasilBersepedaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hasil_bersepeda, container, false)
    }


}
