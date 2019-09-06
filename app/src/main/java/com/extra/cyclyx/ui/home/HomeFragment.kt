package com.extra.cyclyx.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.extra.cyclyx.R
import com.extra.cyclyx.ui.tantangan.TantanganActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnTtg.setOnClickListener{
            val pindah : Intent = Intent(activity?.applicationContext,TantanganActivity::class.java)
            startActivity(pindah)
        }

        btnGo.setOnClickListener{
            Toast.makeText(activity?.applicationContext,"Dalam Pengembangan!",Toast.LENGTH_SHORT).show()
        }
    }
}