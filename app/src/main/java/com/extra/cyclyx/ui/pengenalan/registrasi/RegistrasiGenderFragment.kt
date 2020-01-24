package com.extra.cyclyx.ui.pengenalan.registrasi


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.R
import com.extra.cyclyx.utils.*
import kotlinx.android.synthetic.main.fragment_registrasi_gender.*

/**
 * A simple [Fragment] subclass.
 */
class RegistrasiGenderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrasi_gender, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var sharedPreferences = activity!!.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

        val firstname = sharedPreferences.getString(USER_FIRST_NAME, "")
        val lasstname = sharedPreferences.getString(USER_LAST_NAME, "")
        val birth = sharedPreferences.getString(USER_BIRTHDATE, "")
        val height = sharedPreferences.getString(USER_HEIGHT, "")
        val weight = sharedPreferences.getString(USER_WEIGHT, "")

        tvfirst.text = " $firstname "
        tvlast.text = " $lasstname "
        tvbirth.text = " $birth "
        tvheight.text = " $height "
        tvweight.text = " $weight "
    }


}
