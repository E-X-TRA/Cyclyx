package com.extra.cyclyx.ui.pengenalan.registrasi


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.extra.cyclyx.MainActivity
import com.extra.cyclyx.R
import com.extra.cyclyx.SignUpActivity
import com.extra.cyclyx.ui.home.HomeFragment
import com.extra.cyclyx.utils.*
import kotlinx.android.synthetic.main.fragment_registrasi_gender.*

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiGenderFragment : Fragment() {

    lateinit var btnMale: Button
    lateinit var btnFemale: Button
    lateinit var dataGender: String
    lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_registrasi_gender, container, false)

        btnMale = view.findViewById(R.id.btn_lelaki)
        btnFemale = view.findViewById(R.id.btn_perempuan)
        btnNext = view.findViewById(R.id.btnNext)

        btnMale.setOnClickListener {
            selectGender(1)
        }

        btnFemale.setOnClickListener {
            selectGender(2)
        }

        btnNext.setOnClickListener {
            var sharedPreferences = activity!!.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

            var gender: String = dataGender

            var editor : SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString(USER_GENDER, gender)
            editor.putString(USER_TOKEN,RandomStringGenerator.getRandomString(30))

            editor.commit()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)

        }



        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



        @SuppressLint("ResourceAsColor")
        fun selectGender(btnClicked: Int){
            when(btnClicked){
                0 ->{
                    btn_lelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                    btn_perempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                }

                1 ->{
                    btn_lelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                    btn_perempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                    btn_lelaki.setTextColor(Color.WHITE)
                    btn_perempuan.setTextColor(R.color.font_light_blue)
                    dataGender = "Laki-Laki"
                }

                2 -> {
                    btn_perempuan.setBackgroundResource(R.drawable.rounded_btn_pengenalan)
                    btn_lelaki.setBackgroundResource(R.drawable.rounded_btn_pengenalan2)
                    btn_perempuan.setTextColor(Color.WHITE)
                    btn_lelaki.setTextColor(R.color.font_light_blue)
                    dataGender = "Perempuan"
                }
            }
        }


}
