package com.extra.cyclyx.ui.pengenalan.registrasi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.R

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiBasicFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_registrasi_basic, container, false)
        }


    }
