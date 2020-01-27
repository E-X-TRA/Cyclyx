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
import androidx.fragment.app.FragmentTransaction
import com.extra.cyclyx.R
import com.extra.cyclyx.utils.*

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiDataDiriFragment : Fragment() {

    lateinit var firstName : EditText
    lateinit var lastName : EditText
    lateinit var birthYear : EditText
    lateinit var weight : EditText
    lateinit var height : EditText
    lateinit var btnRegister : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registrasi_data_diri, container, false)

        firstName   = view.findViewById(R.id.edt_nama_depan)
        lastName    = view.findViewById(R.id.edt_nama_belakang)
        birthYear   = view.findViewById(R.id.edt_thn_lahir)
        weight      = view.findViewById(R.id.edt_berat)
        height      = view.findViewById(R.id.edt_tinggi)
        btnRegister = view.findViewById(R.id.btnNext)


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister.setOnClickListener {
            var sharedPreferences = activity!!.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

            var first_name: String = firstName.text.toString()
            var last_name: String = lastName.text.toString()
            var birth_year: String = birthYear.text.toString()
            var user_weight: String = weight.text.toString()
            var user_height: String = height.text.toString()

            var editor : SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString(USER_FIRST_NAME, first_name)
            editor.putString(USER_LAST_NAME, last_name)
            editor.putInt(USER_BIRTHYEAR, Integer.parseInt(birth_year))
            editor.putInt(USER_WEIGHT, Integer.parseInt(user_weight))
            editor.putInt(USER_HEIGHT, Integer.parseInt(user_height))
            editor.putString(USER_TOKEN,RandomStringGenerator.getRandomString(30))

            editor.commit()

            val registrasiGenderFragment = RegistrasiGenderFragment()

            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, registrasiGenderFragment)
                ?.addToBackStack(null)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.commit()

        }
    }

}

