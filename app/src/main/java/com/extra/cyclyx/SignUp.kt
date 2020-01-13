package com.extra.cyclyx

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        var sharedPreferences = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)

        //save data in shared preferences

        btn_signUp.setOnClickListener {
            var email = edt_email.text.toString().trim()
            var username = edt_username.text.toString().trim()
            var password = edt_password.text.toString().trim()
            var confirmPassword = edt_confirm_password.text.toString().trim()

            val editor = sharedPreferences.edit()

            editor.putString("EMAIL",email)
            editor.putString("USERNAME", username)
            editor.putString("PASSWORD", password)

            editor.apply()
        }

        showdata.setOnClickListener {
            val mail = sharedPreferences.getString("EMAIL", "")
            val uname = sharedPreferences.getString("USERNAME", "")
            val ps = sharedPreferences.getString("PASSWORD", "")

            infoTv.text = "Email: $mail \nUsername: $uname \nPassword: $ps"
        }
    }
}
