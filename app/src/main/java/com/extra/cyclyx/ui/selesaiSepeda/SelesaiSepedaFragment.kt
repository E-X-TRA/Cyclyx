package com.extra.cyclyx.ui.selesaiSepeda


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.extra.cyclyx.R
import kotlinx.android.synthetic.main.fragment_selesai_sepeda.*

/**
 * A simple [Fragment] subclass.
 */
class SelesaiSepedaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selesai_sepeda, container, false)

        roundback.visibility = View.VISIBLE
        roundback.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open))
    }


}
