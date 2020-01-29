package com.extra.cyclyx


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.extra.cyclyx.utils.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment() {

    lateinit var firstName  : EditText
    lateinit var lastName   : EditText
    lateinit var birthYear  : EditText
    lateinit var btnSave    : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        firstName   = view.findViewById(R.id.edtNamaDepan)
        lastName    = view.findViewById(R.id.edtNamaBelakang)
        birthYear   = view.findViewById(R.id.edtTahunLahir)
        btnSave     = view.findViewById(R.id.btnSaveEdit)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave.setOnClickListener {
            var sharedPreferences = activity!!.getSharedPreferences(SP_CYCLYX, Context.MODE_PRIVATE)

            var first_name  : String = firstName.text.toString()
            var last_name   : String = lastName.text.toString()
            var birth_year  : String = birthYear.text.toString()

            var editor : SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString(USER_FIRST_NAME, first_name)
            editor.putString(USER_LAST_NAME, last_name)
            editor.putInt(USER_BIRTHYEAR, Integer.parseInt(birth_year))

            editor.apply()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
