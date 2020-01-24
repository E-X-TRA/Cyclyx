package com.extra.cyclyx.ui.kesiapan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.databinding.FragmentKesiapanBinding

class KesiapanFragment : Fragment() {
    private lateinit var binding : FragmentKesiapanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKesiapanBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }
}
