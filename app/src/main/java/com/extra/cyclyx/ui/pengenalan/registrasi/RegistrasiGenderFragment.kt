package com.extra.cyclyx.ui.pengenalan.registrasi


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.extra.cyclyx.R
import com.extra.cyclyx.utils.*
import kotlinx.android.synthetic.main.fragment_registrasi_gender.*

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiGenderFragment : Fragment() {

    lateinit var btnMale: Button
    lateinit var btnFemale: Button
    lateinit var dataGender: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_registrasi_gender, container, false)

        btn_lelaki.setOnClickListener {
            selectGender(1)
        }

        btn_perempuan.setOnClickListener {
            selectGender(2)
        }



        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



        fun selectGender(btnClicked: Int){
            when(btnClicked){
                0 ->{
                    btn_lelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                    btn_perempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                }

                1 ->{
                    btn_lelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                    dataGender = "Laki-Laki"
                }

                2 -> {
                    btn_perempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                    dataGender = "Perempuan"
                }
            }
        }


}
