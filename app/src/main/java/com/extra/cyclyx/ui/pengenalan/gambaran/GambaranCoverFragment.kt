package com.extra.cyclyx.ui.pengenalan.gambaran


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.R
import kotlinx.android.synthetic.main.fragment_pengenalan_cover.*

/**
 * A simple [Fragment] subclass.
 */
class GambaranCoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengenalan_cover, container, false)
    }

    fun setTitle(title: String){
        fragment_title.text = title
    }


}
